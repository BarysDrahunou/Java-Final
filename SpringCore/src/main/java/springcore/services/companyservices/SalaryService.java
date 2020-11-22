package springcore.services.companyservices;

/**
 * The interface Salary service to work with salaries into the company.
 */
public interface SalaryService {

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
}
