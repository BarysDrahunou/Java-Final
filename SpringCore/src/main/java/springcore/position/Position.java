package springcore.position;

import springcore.currency.Usd;

import java.util.Objects;

import static springcore.constants.VariablesConstants.*;

/**
 * The type Position.
 */
public class Position {

    private final String positionName;
    private int vacancies;
    private int activeWorkers;
    private Usd salary = new Usd(DECIMAL_BASE);

    /**
     * Instantiates a new Position.
     *
     * @param positionName the position name
     */
    public Position(String positionName) {
        this.positionName = positionName;
    }

    /**
     * Sets vacancies.
     *
     * @param vacancies the vacancies for this position
     */
    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    /**
     * Sets active workers.
     *
     * @param activeWorkers the active workers for this position
     */
    public void setActiveWorkers(int activeWorkers) {
        this.activeWorkers = activeWorkers;
    }

    /**
     * Sets salary.
     *
     * @param salary the salary for this position
     */
    public void setSalary(Usd salary) {
        this.salary = salary;
    }

    /**
     * Gets position name.
     *
     * @return the position name
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * Gets vacancies.
     *
     * @return the vacancies
     */
    public int getVacancies() {
        return vacancies;
    }

    /**
     * Gets active workers.
     *
     * @return the active workers
     */
    public int getActiveWorkers() {
        return activeWorkers;
    }

    /**
     * Gets salary.
     *
     * @return the salary
     */
    public Usd getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(positionName, position.positionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionName);
    }

    @Override
    public String toString() {
        return positionName;
    }
}
