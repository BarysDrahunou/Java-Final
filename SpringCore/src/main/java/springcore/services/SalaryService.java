package springcore.services;

import java.sql.SQLException;

public interface SalaryService {

    void assignSalaries() throws SQLException;

    void paySalary() throws SQLException;

    void assignBonuses() throws SQLException;

    void increaseSalariesDueToInflation() throws SQLException;
}
