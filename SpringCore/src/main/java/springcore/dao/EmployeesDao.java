package springcore.dao;

import springcore.statuses.EmployeeStatus;

/**
 * The interface EmployeesDao to working with employees
 */
public interface EmployeesDao<SOURCE, TARGET> {

    /**
     * Add employees.
     *
     * @param employees the bundle of employees
     * @throws Exception if employees cannot be inserted
     */
    void addEmployees(SOURCE employees) throws Exception;

    /**
     * Gets employees by status.
     *
     * @param status the status by which employees will be retrieved
     * @return the bundle of employees
     * @throws Exception if employees cannot be retrieved
     */
    TARGET getEmployeesByStatus(EmployeeStatus status) throws Exception;

    /**
     * Update employees.
     *
     * @param employees the bundle of employees which should be updated
     * @throws Exception if employees cannot be updated
     */
    void updateEmployees(SOURCE employees) throws Exception;
}
