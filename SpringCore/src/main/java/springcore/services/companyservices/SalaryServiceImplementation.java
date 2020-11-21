package springcore.services.companyservices;

import org.apache.logging.log4j.*;
import springcore.annotations.InjectRandomInt;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.salary.Salary;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import static springcore.constants.LogMessages.*;
import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;


/**
 * The type Salary service implementation.
 */
public class SalaryServiceImplementation
        implements SalaryService<List<Position>, List<Employee>> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final PositionsDao<List<Position>> positionsDao;
    private final EmployeesDao<List<Employee>> employeesDao;
    @InjectRandomInt(max = 1000)
    private int salaryValueMax;
    @InjectRandomInt(max = 16)
    private int percentageOfIndexing;

    /**
     * Instantiates a new Salary service implementation.
     *
     * @param positionsDao positionsDAO instance
     * @param employeesDao employeesDAO instance
     */
    public SalaryServiceImplementation(PositionsDao<List<Position>> positionsDao,
                                       EmployeesDao<List<Employee>> employeesDao) {
        this.positionsDao = positionsDao;
        this.employeesDao = employeesDao;
    }

    /**
     * Assign salaries to current opened positions
     */
    @Override
    public void assignSalaries() {
        List<Position> positions = positionsDao.getPositions(SALARY_QUERY, DECIMAL_BASE);

        for (Position position : positions) {
            Usd salary = new Usd(new Random().nextInt(salaryValueMax) + 1);

            position.setSalary(salary);

            LOGGER.info(String.format(ASSIGNED_SALARY_QUERY,
                    position, salary.toString()));
        }

        positionsDao.updatePositions(positions);
    }

    /**
     * Pay salary to all active workers
     */
    @Override
    public void paySalary() {
        List<Employee> employees = employeesDao.getEmployeesByStatus(EmployeeStatus.WORKS);
        List<Position> positions = positionsDao.getAllPositions();

        for (Employee employee : employees) {
            Position employeePosition = positions.get(positions.indexOf(employee.getPosition()));
            Usd salaryRate = employeePosition.getSalary();
            int timeWorked = employee.getTimeWorked();
            BigDecimal personalBonuses = employee.getPersonalBonuses();
            Salary salary = new Salary(salaryRate, timeWorked, personalBonuses);

            payIndividualSalary(employee, salary);
        }
    }

    private void payIndividualSalary(Employee employee, Salary salary) {
        Usd salaryRate = salary.getSalaryRate();
        Usd experienceBonus = salary.getExperienceBonus();
        Usd bonusSum = salary.getBonusSum();

        if (experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(EXPERIENCE_BONUS_INFO_MESSAGE, employee, experienceBonus));
        }

        if (bonusSum.getValue() > DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(ALL_BONUSES_MESSAGE, employee, salaryRate, bonusSum, experienceBonus,
                    salary.getSalaryWithBonuses(bonusSum, experienceBonus)));
        }

        if (bonusSum.getValue() < DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(FINE_AND_EXPERIENCE_BONUS_MESSAGE, employee, salaryRate, bonusSum,
                    experienceBonus, salary.getSalaryWithBonuses(bonusSum, experienceBonus)));
        }

        if (bonusSum.getValue() == DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(ONLY_SALARY_MESSAGE, employee, salaryRate));
        }

        if (bonusSum.getValue() > DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(BONUS_MESSAGE, employee, salaryRate, bonusSum
                    , salary.getSalaryWithBonus(bonusSum)));
        }

        if (bonusSum.getValue() < DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(FINE_MESSAGE, employee, salaryRate, bonusSum
                    , salary.getSalaryWithBonus(bonusSum)));
        }

        if (bonusSum.getValue() == DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(EXPERIENCE_BONUS_MESSAGE, employee, salaryRate
                    , experienceBonus, salary.getSalaryWithBonus(experienceBonus)));
        }
    }

    /**
     * Assign bonuses to all active workers in random order
     */
    @Override
    public void assignBonuses() {
        List<Employee> employees = employeesDao.getEmployeesByStatus(EmployeeStatus.WORKS);
        int count = 0;

        for (Employee employee : employees) {
            if (count % BONUS_CONSTANT == DECIMAL_BASE) {
                int randPositive =
                        getRandomBonusOrFineValue(LOWER_BONUS_BOUND, UPPER_BONUS_BOUND);

                setBonusOrFine(employee, randPositive, ASSIGNED_BONUS_QUERY);
            } else {
                if (count % FINE_CONSTANT == DECIMAL_BASE) {
                    int randNegative =
                            getRandomBonusOrFineValue(LOWER_FINE_BOUND, UPPER_FINE_BOUND);

                    setBonusOrFine(employee, randNegative, ASSIGNED_FINE_QUERY);
                }
            }

            count++;
        }

        employeesDao.updateEmployees(employees);
    }

    private int getRandomBonusOrFineValue(int lowerBound, int upperBound) {
        return IntStream.range(lowerBound, upperBound)
                .boxed()
                .collect(Collectors.toList())
                .get(new Random().nextInt(upperBound - lowerBound));
    }

    private void setBonusOrFine(Employee employee, int value, String message) {
        employee.setPersonalBonuses(BigDecimal.valueOf(value));

        LOGGER.info(String.format(message,
                employee, employee.getPersonalBonuses()));
    }

    /**
     * Increase salaries due to inflation.
     */
    @Override
    public void increaseSalariesDueToInflation() {
        List<Position> positions = positionsDao.getAllPositions();
        int inflationRate = new Random().nextInt(percentageOfIndexing);

        LOGGER.info(String.format(INFLATION_MESSAGE, inflationRate));
        LOGGER.info(START_INDEXING_MESSAGE);

        for (Position position : positions) {
            Usd oldSalary = position.getSalary();
            Usd newSalary = Salary.changeSalaryFromInflation(inflationRate, oldSalary);

            position.setSalary(newSalary);

            LOGGER.info(String.format(INFLATION_SALARIES_INCREASE_MESSAGE,
                    position, oldSalary, newSalary));
        }

        LOGGER.info(END_INDEXING_MESSAGE);

        positionsDao.updatePositions(positions);
    }

    /**
     * Gets positions impl db.
     *
     * @return the positions impl db
     */
    @Override
    public PositionsDao<List<Position>> getPositionsDao() {
        return positionsDao;
    }

    /**
     * Gets employees impl db.
     *
     * @return the employees impl db
     */
    @Override
    public EmployeesDao<List<Employee>> getEmployeesDao() {
        return employeesDao;
    }
}
