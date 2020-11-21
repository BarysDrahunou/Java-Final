package springcore.services;

import org.apache.logging.log4j.*;
import springcore.annotations.InjectRandomInt;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.salary.Salary;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.*;

import static springcore.constants.LogMessages.*;
import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;


public class SalaryServiceImplementation implements SalaryService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final PositionsImplDb positionsImplDb;
    private final EmployeesImplDb employeesImplDb;
    @InjectRandomInt(max = 1000)
    private int salaryValueMax;
    @InjectRandomInt(max = 16)
    private int percentageOfIndexing;

    public SalaryServiceImplementation(PositionsImplDb positionsImplDb,
                                       EmployeesImplDb employeesImplDb) {
        this.positionsImplDb = positionsImplDb;
        this.employeesImplDb = employeesImplDb;
    }

    public void assignSalaries() throws SQLException {
        List<Position> positions = positionsImplDb.getPositions(SALARY_QUERY, DECIMAL_BASE);

        for (Position position : positions) {
            Usd salary = new Usd(new Random().nextInt(salaryValueMax) + 1);

            position.setSalary(salary);

            LOGGER.info(String.format(ASSIGNED_SALARY_QUERY,
                    position, salary.toString()));
        }

        positionsImplDb.updatePositions(positions);
    }

    public void paySalary() throws SQLException {
        List<Employee> employees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS);
        List<Position> positions=positionsImplDb.getAllPositions();

        for (Employee employee : employees) {
            Position employeePosition=positions.get(positions.indexOf(employee.getPosition()));
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

    public void assignBonuses() throws SQLException {
        List<Employee> employees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS);
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

        employeesImplDb.updateEmployees(employees);
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

    public void increaseSalariesDueToInflation() throws SQLException {
        List<Position> positions = positionsImplDb.getAllPositions();
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

        positionsImplDb.updatePositions(positions);
    }
}
