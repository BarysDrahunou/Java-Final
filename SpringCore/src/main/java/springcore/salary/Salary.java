package springcore.salary;

import springcore.currency.Usd;

import java.math.BigDecimal;

import static springcore.constants.VariablesConstants.*;


public class Salary {

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
        return oldSalaryRate
                .multiplication(inflationRate + PERCENT_BASE)
                .division(PERCENT_BASE);
    }

    public Usd getSalaryWithBonuses(Usd bonusSum, Usd experienceBonus) {
        return salaryRate.addition(bonusSum).addition(experienceBonus);
    }

    public Usd getSalaryWithBonus(Usd bonusSum) {
        return salaryRate.addition(bonusSum);
    }

    public Usd getSalaryRate() {
        return salaryRate;
    }

    public Usd getBonusSum() {
        return bonusSum;
    }

    public Usd getExperienceBonus() {
        return experienceBonus;
    }
}
