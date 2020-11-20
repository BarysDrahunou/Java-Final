import org.apache.logging.log4j.*;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springcore.company.Company;
import springcore.config.SpringConfig;
import springcore.employee.EmployeeCreator;
import springcore.services.*;
import springcore.utilityconnection.ConnectTemporary;

import java.sql.*;

import static springcore.constants.SQLQueries.*;
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
            connectTemporary.truncateTables(EMPLOYEES_TABLE_TRUNCATE_SQL,
                    POSITIONS_TABLE_TRUNCATE_SQL);
            Company company = new Company();
            EmployeeService employeeService = context.getBean(EmployeeService.class);
            employeeService.setCompany(company);
            PositionService positionService = context.getBean(PositionService.class);
            positionService.setCompany(company);
            SalaryService salaryService = context.getBean(SalaryService.class);
            EmployeeCreator employeeCreator = new EmployeeCreator(NAMES_PATH, SURNAMES_PATH);
            for (int year = INITIAL_YEAR_VALUE; year <= years; year++) {
                for (int month = INITIAL_MONTH_VALUE; month <= LAST_MONTH_VALUE; month++) {
                    positionService.addPositions();
                    salaryService.assignSalaries();
                    employeeService.hireEmployees(employeeCreator);
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
}
