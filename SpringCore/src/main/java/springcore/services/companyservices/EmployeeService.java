package springcore.services.companyservices;

import springcore.company.Company;
import springcore.dao.EmployeesImplDb;
import springcore.services.EmployeeCreator;


/**
 * The interface Employee service to work with employees into the company.
 */
public interface EmployeeService {

    /**
     * Creates new employees via employeeCreator and hired them into a company
     *
     * @param employeeCreator the instance of EmployeeCreator.class to hire new employees
     * @throws Exception if new employees cannot be hired
     */
    void hireEmployees(EmployeeCreator employeeCreator) throws Exception;

    /**
     * Fire employees from a company
     *
     * @throws Exception if employees cannot be fired
     */
    void fireEmployees() throws Exception;

    /**
     * Release employees from a company after they have received their salaries.
     *
     * @throws Exception if employees cannot be released
     */
    void releaseEmployees() throws Exception;

    /**
     * Increase experience of current company's employees
     *
     * @throws Exception if an experience cannot be increased
     */
    void increaseExperience() throws Exception;

    /**
     * Sets company to this service
     *
     * @param company company for which current service will operate
     */
    void setCompany(Company company);

    /**
     * Sets employees impl db.
     *
     * @param employeesImplDb the employees impl db
     */
    void setEmployeesImplDb(EmployeesImplDb employeesImplDb);

    /**
     * Gets employees impl db.
     *
     * @return the employees impl db
     */
    EmployeesImplDb getEmployeesImplDb();
}
