package springcore.services.companyservices;

import springcore.company.Company;
import springcore.dao.EmployeesImplDb;
import springcore.services.EmployeeCreator;


public interface EmployeeService {

    void hireEmployees(EmployeeCreator employeeCreator) throws Exception;

    void fireEmployees() throws Exception;

    void releaseEmployees() throws Exception;

    void increaseExperience() throws Exception;

    void setCompany(Company company);

    void setEmployeesImplDb(EmployeesImplDb employeesImplDb);
}
