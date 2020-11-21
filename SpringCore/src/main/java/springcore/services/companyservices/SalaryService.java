package springcore.services.companyservices;


import springcore.dao.EmployeesDao;
import springcore.dao.PositionsDao;

/**
 * The interface Salary service to work with salaries into the company.
 */
public interface SalaryService<T, U> {

    /**
     * Assign salaries to current opened positions
     */
    void assignSalaries();

    /**
     * Pay salary to all active workers
     */
    void paySalary();

    /**
     * Assign bonuses to all active workers in random order
     */
    void assignBonuses();

    /**
     * Increase salaries due to inflation.
     */
    void increaseSalariesDueToInflation();

    /**
     * Gets positionsDao.
     *
     * @return positionsDao
     */
    PositionsDao<T> getPositionsDao();

    /**
     * Gets employeesDao.
     *
     * @return employeesDao
     */
    EmployeesDao<U> getEmployeesDao();
}
