package springcore.employee;

import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;

/**
 * The type Employee.
 */
public class Employee {

    private int id;
    private int timeWorked;
    private final String name;
    private final String surname;
    private EmployeeStatus status;
    private Position position;
    private BigDecimal personalBonuses = BigDecimal.ZERO;

    /**
     * Instantiates a new Employee.
     *
     * @param name    the name of the employee
     * @param surname the surname of the employee
     */
    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * Sets id.
     *
     * @param id the id of each employee
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets status.
     *
     * @param status the status of each employee
     */
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    /**
     * Sets position.
     *
     * @param position the position of each employee
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets personal bonuses.
     *
     * @param personalBonuses the personal bonuses of each employee
     */
    public void setPersonalBonuses(BigDecimal personalBonuses) {
        this.personalBonuses = personalBonuses;
    }

    /**
     * Sets time worked.
     *
     * @param timeWorked the time worked of each employee
     */
    public void setTimeWorked(int timeWorked) {
        this.timeWorked = timeWorked;
    }

    /**
     * Gets id.
     *
     * @return the id of each employee
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name of each employee
     */
    public String getName() {
        return name;
    }

    /**
     * Gets surname.
     *
     * @return the surname of each employee
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Gets status.
     *
     * @return the status of each employee
     */
    public EmployeeStatus getStatus() {
        return status;
    }

    /**
     * Gets position.
     *
     * @return the position of each employee
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets personal bonuses.
     *
     * @return the personal bonuses of each employee
     */
    public BigDecimal getPersonalBonuses() {
        return personalBonuses;
    }

    /**
     * Gets time worked.
     *
     * @return the time worked of each employee
     */
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

