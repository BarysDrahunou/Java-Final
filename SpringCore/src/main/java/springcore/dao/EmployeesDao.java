package springcore.dao;

import springcore.employee.Employee;
import springcore.statuses.EmployeeStatus;

import java.util.List;

/**
 * The interface EmployeesDao to working with employees
 */
public interface EmployeesDao {

    /**
     * Add employees.
     *
     * @param employees the list of employees
     */
    void addEmployees(List<Employee> employees);

    /**
     * Gets employees by status.
     *
     * @param status the status by which employees will be retrieved
     * @return the list of employees
     */
    List<Employee> getEmployeesByStatus(EmployeeStatus status);

    /**
     * Update employees.
     *
     * @param employees the list of employees which should be updated
     */
    void updateEmployees(List<Employee> employees);
}
