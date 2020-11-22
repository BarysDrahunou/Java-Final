package springcore.services.employeecreator;

import springcore.employee.Employee;

/**
 * The interface Employee creator.
 */
public interface EmployeeCreator {

    /**
     * Create employee and get employee.
     *
     * @return the employee who was created
     */
    Employee createEmployeeAndGet();
}
