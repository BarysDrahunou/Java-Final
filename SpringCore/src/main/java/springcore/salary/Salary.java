package springcore.salary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import springcore.currency.Usd;
import springcore.employee.Employee;

import java.math.BigDecimal;

import static springcore.constants.LogMessages.*;
import static springcore.constants.VariablesConstants.*;


public class Salary {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Usd salaryRate;
    private final Usd bonusSum;
    private final Usd experienceBonus;

    public Salary(Usd salaryRate, int timeWorked, BigDecimal personalBonus) {
        this.salaryRate = salaryRate;
        this.bonusSum = salaryRate.multiplication(personalBonus.intValue()).division(PERCENT_BASE);
        this.experienceBonus = salaryRate
                .multiplication(timeWorked / MONTHS_TO_RAISE_EXPERIENCE_BONUS)
                .multiplication(EXPERIENCE_BONUS_MULTIPLIER)
                .division(PERCENT_BASE);
    }

    public static Usd changeSalaryFromInflation(int inflationRate, Usd oldSalaryRate) {
        return oldSalaryRate.multiplication(inflationRate + PERCENT_BASE).division(PERCENT_BASE);
    }

    private Usd getSalaryWithBonuses(Usd bonusSum, Usd experienceBonus) {
        return salaryRate.addition(bonusSum).addition(experienceBonus);
    }

    private Usd getSalaryWithBonus(Usd bonusSum) {
        return salaryRate.addition(bonusSum);
    }

    public void paySalary(Employee employee) {
        if (experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(EXPERIENCE_BONUS_INFO_MESSAGE,
                    employee, experienceBonus));
        }
        if (bonusSum.getValue() > DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(ALL_BONUSES_MESSAGE, employee,
                    salaryRate, bonusSum, experienceBonus,
                    getSalaryWithBonuses(bonusSum, experienceBonus)));
        }
        if (bonusSum.getValue() < DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(FINE_AND_EXPERIENCE_BONUS_MESSAGE, employee,
                    salaryRate, bonusSum, experienceBonus,
                    getSalaryWithBonuses(bonusSum, experienceBonus)));
        }
        if (bonusSum.getValue() == DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(ONLY_SALARY_MESSAGE, employee, salaryRate));
        }
        if (bonusSum.getValue() > DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(BONUS_MESSAGE, employee, salaryRate, bonusSum,
                    getSalaryWithBonus(bonusSum)));
        }
        if (bonusSum.getValue() < DECIMAL_BASE && experienceBonus.getValue() == DECIMAL_BASE) {
            LOGGER.info(String.format(FINE_MESSAGE, employee, salaryRate, bonusSum,
                    getSalaryWithBonus(bonusSum)));
        }
        if (bonusSum.getValue() == DECIMAL_BASE && experienceBonus.getValue() > DECIMAL_BASE) {
            LOGGER.info(String.format(EXPERIENCE_BONUS_MESSAGE, employee,
                    salaryRate, experienceBonus,
                    getSalaryWithBonus(experienceBonus)));
        }
    }
}
