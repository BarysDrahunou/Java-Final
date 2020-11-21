package springcore.services;


public interface SalaryService {

    void assignSalaries() throws Exception;

    void paySalary() throws Exception;

    void assignBonuses() throws Exception;

    void increaseSalariesDueToInflation() throws Exception;
}
