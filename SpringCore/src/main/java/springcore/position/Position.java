package springcore.position;

import springcore.currency.Usd;

import java.util.Objects;

import static springcore.constants.VariablesConstants.*;

public class Position {

    private final String positionName;
    private int vacancies;
    private int activeWorkers;
    private Usd salary = new Usd(DECIMAL_BASE);

    public Position(String positionName) {
        this.positionName = positionName;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    public void setActiveWorkers(int activeWorkers) {
        this.activeWorkers = activeWorkers;
    }

    public void setSalary(Usd salary) {
        this.salary = salary;
    }

    public String getPositionName() {
        return positionName;
    }

    public int getVacancies() {
        return vacancies;
    }

    public int getActiveWorkers() {
        return activeWorkers;
    }

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
