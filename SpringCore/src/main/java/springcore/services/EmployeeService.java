package springcore.services;

import springcore.company.Company;
import springcore.dao.EmployeesImplDb;
import springcore.employee.EmployeeCreator;

import java.sql.SQLException;

public interface EmployeeService {

    void hireEmployees(EmployeeCreator employeeCreator) throws SQLException;

    void fireEmployees() throws SQLException;

    void increaseExperience() throws SQLException;

    void setCompany(Company company);

    void setEmployeesImplDb(EmployeesImplDb employeesImplDb);
}
