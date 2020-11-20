import org.apache.logging.log4j.*;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springcore.config.SpringConfig;
import springcore.services.*;
import springcore.utilityconnection.ConnectTemporary;

import java.sql.*;

import static springcore.constants.VariablesConstants.*;

public class CompanyEmulator {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int years;

    public CompanyEmulator(int years) {
        this.years = years;
    }

    public void emulate() {

        try (AnnotationConfigApplicationContext context
                     = new AnnotationConfigApplicationContext(SpringConfig.class);
            ConnectTemporary connectTemporary = context.getBean(ConnectTemporary.class)) {
            truncateTables(connectTemporary.getConnection());
            EmployeeService employeeService = context.getBean(EmployeeService.class);
            PositionService positionService = context.getBean(PositionService.class);
            SalaryService salaryService = context.getBean(SalaryService.class);
            for (int year = INITIAL_YEAR_VALUE; year <= years; year++) {
                for (int month = INITIAL_MONTH_VALUE; month <= LAST_MONTH_VALUE; month++) {
                    positionService.addPositions();
                    salaryService.assignSalaries();
                    employeeService.hireEmployees(context);
                    positionService.assignPositions();
                    employeeService.increaseExperience();
                    salaryService.assignBonuses();
                    salaryService.paySalary();
                    employeeService.fireEmployees();
                    positionService.clearPositions();
                    positionService.closePositions();
                    positionService.changePosition();
                }
                salaryService.increaseSalariesDueToInflation();
            }
        } catch (BeanCreationException | BeanDefinitionStoreException | SQLException e) {
            LOGGER.error(e);
        }
    }

    private void truncateTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.addBatch(EMPLOYEES_TABLE_MESSAGE);
        statement.addBatch(POSITIONS_TABLE_MESSAGE);
        statement.executeBatch();
        connection.commit();
    }
}
