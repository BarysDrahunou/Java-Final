package springcore.employee;

import springcore.annotations.*;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;
import java.util.Objects;

public class Employee {

    private int id;
    private int timeWorked;
    @RandomName
    private String name;
    @RandomSurname
    private String surname;
    private EmployeeStatus status;
    private Position position;
    private BigDecimal personalBonuses = BigDecimal.ZERO;

    public Employee() {
    }

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
        return Objects.equals(name, employee.name) &&
                Objects.equals(surname, employee.surname) &&
                Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, position);
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, surname);
    }
}

