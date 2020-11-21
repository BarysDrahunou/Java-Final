package springcore.employee;

import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;

public class Employee {

    private int id;
    private int timeWorked;
    private final String name;
    private final String surname;
    private EmployeeStatus status;
    private Position position;
    private BigDecimal personalBonuses = BigDecimal.ZERO;

    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPersonalBonuses(BigDecimal personalBonuses) {
        this.personalBonuses = personalBonuses;
    }

    public void setTimeWorked(int timeWorked) {
        this.timeWorked = timeWorked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public Position getPosition() {
        return position;
    }

    public BigDecimal getPersonalBonuses() {
        return personalBonuses;
    }

    public int getTimeWorked() {
        return timeWorked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return getId() == employee.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, surname);
    }
}

