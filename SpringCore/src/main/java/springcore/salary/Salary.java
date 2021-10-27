package springcore.salary;

import springcore.currency.Usd;

import java.math.BigDecimal;

import static springcore.constants.VariablesConstants.*;


/**
 * The type Salary.
 */
public class Salary {

    private final Usd salaryRate;
    private final Usd bonusSum;
    private final Usd experienceBonus;

    /**
     * Instantiates a new Salary.
     *
     * @param salaryRate    the salary rate
     * @param timeWorked    the time worked
     * @param personalBonus the personal bonus
     */
    public Salary(Usd salaryRate, int timeWorked, BigDecimal personalBonus) {
        this.salaryRate = salaryRate;
        this.bonusSum = salaryRate.multiplication(personalBonus.intValue()).division(PERCENT_BASE);
        this.experienceBonus = salaryRate
                .multiplication(timeWorked / MONTHS_TO_RAISE_EXPERIENCE_BONUS)
                .multiplication(EXPERIENCE_BONUS_MULTIPLIER)
                .division(PERCENT_BASE);
    }

    /**
     * Change salary from inflation usd.
     *
     * @param inflationRate the inflation rate
     * @param oldSalaryRate the old salary rate
     * @return new usd rate according to inflation
     */
    public static Usd changeSalaryFromInflation(int inflationRate, Usd oldSalaryRate) {
        return oldSalaryRate
                .multiplication(inflationRate + PERCENT_BASE)
                .division(PERCENT_BASE);
    }

    /**
     * Gets salary with bonuses.
     *
     * @param bonusSum        random bonus parameter
     * @param experienceBonus the experience bonus
     * @return the cumulative sum of salary, random bonus and experience bonus
     */
    public Usd getSalaryWithBonuses(Usd bonusSum, Usd experienceBonus) {
        return salaryRate.addition(bonusSum).addition(experienceBonus);
    }

    /**
     * Gets salary with bonus.
     *
     * @param bonusSum the bonus sum
     * @return the salary with bonus
     */
    public Usd getSalaryWithBonus(Usd bonusSum) {
        return salaryRate.addition(bonusSum);
    }

    /**
     * Gets salary rate.
     *
     * @return the salary rate
     */
    public Usd getSalaryRate() {
        return salaryRate;
    }

    /**
     * Gets bonus sum.
     *
     * @return the bonus sum
     */
    public Usd getBonusSum() {
        return bonusSum;
    }

    /**
     * Gets experience bonus.
     *
     * @return the experience bonus
     */
    public Usd getExperienceBonus() {
        return experienceBonus;
    }
}
