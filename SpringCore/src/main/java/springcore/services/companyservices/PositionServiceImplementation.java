package springcore.services.companyservices;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import springcore.annotations.InjectRandomInt;
import springcore.company.Company;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.services.positioncreator.PositionCreator;
import springcore.statuses.EmployeeStatus;

import java.util.*;
import java.util.stream.Collectors;

import static springcore.constants.LogMessages.*;
import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;
import static springcore.statuses.EmployeeStatus.*;

/**
 * The type Position service implementation.
 */
@Service
public class PositionServiceImplementation implements PositionService{

    private static final Logger LOGGER = LogManager.getLogger();
    private final PositionsDao positionsDao;
    private final EmployeesDao employeesDao;
    private Company company;
    @InjectRandomInt(max = 10)
    private int positionsToClose;
    @InjectRandomInt(max = 20)
    private int positionsToOpen;
    @InjectRandomInt(max = 10)
    private int employeesToChangeWork;

    /**
     * Instantiates a new Position service implementation.
     *
     * @param positionsDao positionsDAO instance
     * @param employeesDao employeesDAO instance
     */
    @Autowired
    public PositionServiceImplementation(PositionsDao positionsDao,
                                         EmployeesDao employeesDao) {
        this.positionsDao = positionsDao;
        this.employeesDao = employeesDao;
    }

    /**
     * Create new positions via positionCreator and add them to a company.
     *
     * @param positionCreator the position creator
     */
    @Override
    public void addPositions(PositionCreator positionCreator) {
        int positionsToAddAmount = new Random().nextInt(positionsToOpen + 1);
        List<Position> positionsToAdd = new ArrayList<>();
        List<Position> positionsToUpdate = new ArrayList<>();
        List<Position> positions = positionsDao.getAllPositions();

        for (int i = 0; i < positionsToAddAmount; i++) {
            Position position = positionCreator.createPositionAndGet();

            if (positions.contains(position)) {
                position = positions.remove(positions.indexOf(position));
                position.setVacancies(position.getVacancies() + INITIAL_VACANCIES);

                positionsToUpdate.remove(position);
                positionsToUpdate.add(position);
            } else {
                positionsToAdd.remove(position);
                positionsToAdd.add(position);
            }

            positions.add(position);
            company.openVacancy();

            LOGGER.info(String.format(ADDED_POSITION_MESSAGE,
                    position));
        }

        positionsDao.updatePositions(positionsToUpdate);
        positionsDao.addPositions(positionsToAdd);
    }

    /**
     * Assign employees to opened positions into a company
     */
    @Override
    public void assignPositions() {
        List<Employee> newEmployees = employeesDao.getEmployeesByStatus(NEW);
        List<Position> positions = positionsDao.getPositions(OPENED_VACANCIES_QUERY,
                DECIMAL_BASE);
        List<Position> positionsToUpdate = new ArrayList<>();

        for (Employee employee : newEmployees) {
            Position position = positions
                    .get(new Random().nextInt(positions.size()));

            position.setVacancies(position.getVacancies() - 1);
            position.setActiveWorkers(position.getActiveWorkers() + 1);

            if (position.getVacancies() == DECIMAL_BASE) {
                positions.remove(position);
            }

            positionsToUpdate.remove(position);
            positionsToUpdate.add(position);

            employee.setPosition(position);
            employee.setStatus(EmployeeStatus.WORKS);

            LOGGER.info(String.format(ASSIGNED_EMPLOYEE_MESSAGE,
                    employee, position));
        }

        employeesDao.updateEmployees(newEmployees);
        positionsDao.updatePositions(positionsToUpdate);
    }

    /**
     * Clear positions after employees has been fired
     */
    @Override
    public void clearPositions() {
        List<Position> positions = employeesDao.getEmployeesByStatus(FIRED)
                .stream()
                .map(Employee::getPosition)
                .collect(Collectors.toList());
        List<Position> positionsToUpdate = new ArrayList<>();

        for (Position position : positions) {
            Position oldPosition = positionsDao
                    .getPositions(POSITION_QUERY, position.getPositionName())
                    .get(DECIMAL_BASE);

            if (positionsToUpdate.contains(oldPosition)) {
                oldPosition = positionsToUpdate.get(positionsToUpdate.indexOf(oldPosition));
            }

            oldPosition.setVacancies(oldPosition.getVacancies() + 1);
            oldPosition.setActiveWorkers(oldPosition.getActiveWorkers() - 1);

            positionsToUpdate.add(oldPosition);
            company.openVacancy();

            LOGGER.info(String.format(OPENED_VACANCY_MESSAGE, position));
        }

        positionsDao.updatePositions(positionsToUpdate);
    }

    /**
     * Close positions if company doesn't need any more of current position .
     */
    @Override
    public void closePositions() {
        List<Position> positions = positionsDao.getPositions(OPENED_VACANCIES_QUERY, DECIMAL_BASE);
        List<Position> positionsToUpdate = new ArrayList<>();
        int positionsToCloseAmount = new Random().nextInt(positionsToClose + 1);

        for (int i = 0; i < positionsToCloseAmount && !positions.isEmpty(); i++) {
            Position position = positions.get(new Random().nextInt(positions.size()));

            position.setVacancies(position.getVacancies() - 1);

            if (position.getVacancies() == DECIMAL_BASE) {
                positions.remove(position);
            }

            positionsToUpdate.remove(position);
            positionsToUpdate.add(position);

            company.closeVacancy();

            LOGGER.info(String.format(CLOSED_POSITION_MESSAGE, position));
        }
        positionsDao.updatePositions(positionsToUpdate);
    }

    /**
     * Change position for employee
     */
    @Override
    public void changePosition() {
        List<Position> allPositionList = positionsDao.getAllPositions();
        List<Employee> employees = getEmployeesList(allPositionList);

        List<Position> positionsWithVacancies = allPositionList
                .stream()
                .filter(position -> position.getVacancies() > DECIMAL_BASE)
                .collect(Collectors.toList());
        Set<Position> positionsToUpdate = new HashSet<>();

        for (int i = 0; i < employees.size() && !positionsWithVacancies.isEmpty(); i++) {
            Employee employee = employees.get(i);
            Position oldPosition = allPositionList
                    .get(allPositionList.indexOf(employee.getPosition()));
            Position newPosition = positionsWithVacancies
                    .get(new Random().nextInt(positionsWithVacancies.size()));

            if (!oldPosition.equals(newPosition)) {
                updatePositions(employee, oldPosition,
                        newPosition, allPositionList, positionsWithVacancies);
                positionsToUpdate.add(oldPosition);
                positionsToUpdate.add(newPosition);
            }
        }

        employeesDao.updateEmployees(employees);
        positionsDao.updatePositions(new ArrayList<>(positionsToUpdate));
    }

    private List<Employee> getEmployeesList(List<Position> allPositionList) {
        int amountEmployeesToChangeWork = new Random()
                .nextInt(employeesToChangeWork + 1);
        List<Employee> employees = employeesDao
                .getEmployeesByStatus(EmployeeStatus.WORKS)
                .stream()
                .peek(employee -> {
                    Position position = employee.getPosition();
                    Usd positionSalary = allPositionList
                            .get(allPositionList.indexOf(position))
                            .getSalary();
                    position.setSalary(positionSalary);
                })
                .collect(Collectors.toList());

        Collections.shuffle(employees);

        return employees.subList(0, Math.min(amountEmployeesToChangeWork
                , employees.size()));
    }

    private void updatePositions(Employee employee, Position oldPosition,
                                 Position newPosition, List<Position> allPositionList,
                                 List<Position> positionsWithVacancies) {
        employee.setPosition(newPosition);

        oldPosition.setActiveWorkers(oldPosition.getActiveWorkers() - 1);
        oldPosition.setVacancies(oldPosition.getVacancies() + 1);

        newPosition.setActiveWorkers(newPosition.getActiveWorkers() + 1);
        newPosition.setVacancies(newPosition.getVacancies() - 1);

        allPositionList.remove(oldPosition);
        allPositionList.remove(newPosition);
        allPositionList.add(newPosition);
        allPositionList.add(oldPosition);

        positionsWithVacancies.remove(newPosition);

        if (newPosition.getVacancies() > DECIMAL_BASE) {
            positionsWithVacancies.add(newPosition);
        }

        if (oldPosition.getSalary().getValue() > newPosition.getSalary().getValue()) {
            LOGGER.info(String.format(DEMOTED_EMPLOYEE_MESSAGE, employee, oldPosition,
                    newPosition, oldPosition.getSalary(), newPosition.getSalary()));
        } else {
            LOGGER.info(String.format(PROMOTED_EMPLOYEE_MESSAGE, employee, oldPosition,
                    newPosition, oldPosition.getSalary(), newPosition.getSalary()));
        }
    }

    /**
     * Sets company to this service
     *
     * @param company company for which current service will operate
     */
    @Override
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Gets positionsDao.
     *
     * @return positionsDao
     */

    public PositionsDao getPositionsDao() {
        return positionsDao;
    }

    /**
     * Gets employeesDao.
     *
     * @return employeesDao
     */

    public EmployeesDao getEmployeesDao() {
        return employeesDao;
    }
}