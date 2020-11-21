package springcore.dao;

import springcore.employee.Employee;
import springcore.statuses.EmployeeStatus;

import java.util.List;

public interface EmployeesDao {

    void addEmployees(List<Employee> employees) throws Exception;

    List<Employee> getEmployeesByStatus(EmployeeStatus status) throws Exception;

    void updateEmployees(List<Employee> employees) throws Exception;
}
