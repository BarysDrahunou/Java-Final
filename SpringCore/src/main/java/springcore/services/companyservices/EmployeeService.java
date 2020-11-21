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
     */
    void hireEmployees(EmployeeCreator employeeCreator);

    /**
     * Fire employees from a company
     */
    void fireEmployees();

    /**
     * Release employees from a company after they have received their salaries.
     */
    void releaseEmployees();

    /**
     * Increase experience of current company's employees
     */
    void increaseExperience();

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
