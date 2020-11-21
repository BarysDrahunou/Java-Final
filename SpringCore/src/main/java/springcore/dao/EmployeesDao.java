package springcore.dao;

import springcore.services.ConnectTemporary;
import springcore.statuses.EmployeeStatus;

/**
 * The interface EmployeesDao to working with employees
 */
public interface EmployeesDao<T> {

    /**
     * Add employees.
     *
     * @param employees the bundle of employees
     */
    void addEmployees(T employees);

    /**
     * Gets employees by status.
     *
     * @param status the status by which employees will be retrieved
     * @return the bundle of employees
     */
    T getEmployeesByStatus(EmployeeStatus status);

    /**
     * Update employees.
     *
     * @param employees the bundle of employees which should be updated
     */
    void updateEmployees(T employees);

    /**
     * Gets connect temporary.
     *
     * @return the connect temporary
     */
    ConnectTemporary getConnectTemporary();
}
