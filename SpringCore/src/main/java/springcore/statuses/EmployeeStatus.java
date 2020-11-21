package springcore.statuses;

/**
 * The enum Employee status.
 */
public enum EmployeeStatus {
    /**
     * New employee status - sets if an employee has been just hired but hasn't assigned
     */
    NEW,
    /**
     * Works employee status - sets if an employee has been assigned
     */
    WORKS,
    /**
     * Fired employee status - sets if an employee has been hired but hasn't received his salary
     */
    FIRED,
    /**
     * Went out employee status - sets if an employee has been hired and has received his salary
     */
    WENT_OUT
}
