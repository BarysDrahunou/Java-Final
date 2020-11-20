package springcore.services;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import springcore.annotations.InjectRandomInt;
import springcore.company.Company;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static springcore.constants.LogMessages.*;
import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;

@Component
public class PositionServiceImplementation implements PositionService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> jobs;
    private final PositionsImplDb positionsImplDb;
    private final EmployeesImplDb employeesImplDb;
    private Company company;
    @InjectRandomInt(max = 10)
    private int positionsToClose;
    @InjectRandomInt(max = 20)
    private int positionsToOpen;
    @InjectRandomInt(max = 10)
    private int employeesToChangeWork;

    @Autowired
    public PositionServiceImplementation(PositionsImplDb positionsImplDb, EmployeesImplDb employeesImplDb,
                                         @Value("${jobs.path}") String path) throws IOException {
        this.positionsImplDb = positionsImplDb;
        this.employeesImplDb = employeesImplDb;
        this.jobs = Files.readAllLines(Paths.get(path));
    }

    @Override
    public void addPositions() throws SQLException {
        int positionsToAddAmount = new Random().nextInt(positionsToOpen + 1);
        List<Position> positionsToAdd = new ArrayList<>();
        List<Position> positionsToUpdate = new ArrayList<>();
        List<Position> positions = positionsImplDb.getAllPositions();

        for (int i = 0; i < positionsToAddAmount; i++) {
            int jobsScopeSize = jobs.size();
            Position position = jobsScopeSize > 0
                    ? new Position(jobs.get(new Random().nextInt(jobs.size())))
                    : new Position(DEFAULT_JOB);

            if (positions.contains(position)) {
                position = positions.get(positions.indexOf(position));
                position.setVacancies(position.getVacancies() + INITIAL_VACANCIES);
                positions.remove(position);
                positionsToUpdate.add(position);
            } else {
                position.setVacancies(INITIAL_VACANCIES);
                positionsToAdd.add(position);
            }

            positions.add(position);
            company.openVacancy();

            LOGGER.info(String.format(ADDED_POSITION_MESSAGE,
                    position));
        }

        positionsImplDb.updatePositions(positionsToUpdate);
        positionsImplDb.addPositions(positionsToAdd);
    }

    @Override
    public void assignPositions() throws SQLException {
        List<Employee> newEmployees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.NEW);
        List<Position> positions = positionsImplDb.getPositions(OPENED_VACANCIES_QUERY,
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

        employeesImplDb.updateEmployees(newEmployees);
        positionsImplDb.updatePositions(positionsToUpdate);
    }

    @Override
    public void clearPositions() throws SQLException {
        List<Position> positions = employeesImplDb.getEmployeesByStatus(EmployeeStatus.FIRED)
                .stream()
                .map(Employee::getPosition)
                .collect(Collectors.toList());
        List<Position> positionsToUpdate = new ArrayList<>();

        employeesImplDb.updateEmployeesStatusByStatus(EmployeeStatus.WENT_OUT,
                EmployeeStatus.FIRED);

        for (Position position : positions) {
            Position oldPosition = positionsImplDb
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

        positionsImplDb.updatePositions(positionsToUpdate);
    }

    @Override
    public void closePositions() throws SQLException {
        List<Position> positions = positionsImplDb.getPositions(OPENED_VACANCIES_QUERY, DECIMAL_BASE);
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
        positionsImplDb.updatePositions(positionsToUpdate);
    }

    @Override
    public void changePosition() throws SQLException {
        List<Position> allPositionList = positionsImplDb.getAllPositions();
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

        employeesImplDb.updateEmployees(employees);
        positionsImplDb.updatePositions(new ArrayList<>(positionsToUpdate));
    }

    private List<Employee> getEmployeesList(List<Position> allPositionList)
            throws SQLException {
        int amountEmployeesToChangeWork = new Random()
                .nextInt(employeesToChangeWork + 1);
        List<Employee> employees = employeesImplDb
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
        employee.setTimeWorked(DECIMAL_BASE);

        LOGGER.info(String.format(CLEAR_EXPERIENCE_MESSAGE, employee));

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

    @Override
    public void setCompany(Company company) {
        this.company = company;
    }
}