package springcore.services.companyservices;

import springcore.company.Company;
import springcore.dao.EmployeesDao;
import springcore.services.EmployeeCreator;


/**
 * The interface Employee service to work with employees into the company.
 */
public interface EmployeeService<T> {

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
     * Sets employeesDao.
     *
     * @param employeesDao employeesDao
     */
    void setEmployeesDao(EmployeesDao<T> employeesDao);

    /**
     * Gets employeesDao.
     *
     * @return employeesDao
     */
    EmployeesDao<T> getEmployeesDao();
}
