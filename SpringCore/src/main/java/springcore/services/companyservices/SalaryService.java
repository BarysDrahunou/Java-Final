package springcore.services.companyservices;


import springcore.dao.EmployeesImplDb;
import springcore.dao.PositionsImplDb;

/**
 * The interface Salary service to work with salaries into the company.
 */
public interface SalaryService {

    /**
     * Assign salaries to current opened positions
     *
     * @throws Exception if salaries cannot be assigned
     */
    void assignSalaries() throws Exception;

    /**
     * Pay salary to all active workers
     *
     * @throws Exception if salary cannot be paid
     */
    void paySalary() throws Exception;

    /**
     * Assign bonuses to all active workers in random order
     *
     * @throws Exception if bonuses cannot be assigned
     */
    void assignBonuses() throws Exception;

    /**
     * Increase salaries due to inflation.
     *
     * @throws Exception if salaries cannot be increased
     */
    void increaseSalariesDueToInflation() throws Exception;

    /**
     * Gets positions impl db.
     *
     * @return the positions impl db
     */
    PositionsImplDb getPositionsImplDb();

    /**
     * Gets employees impl db.
     *
     * @return the employees impl db
     */
    EmployeesImplDb getEmployeesImplDb();
}
