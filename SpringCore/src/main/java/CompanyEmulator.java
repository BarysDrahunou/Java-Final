import org.apache.logging.log4j.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springcore.company.Company;
import springcore.config.SpringConfig;
import springcore.services.companyservices.*;
import springcore.services.connectionservices.ConnectTemporary;
import springcore.services.employeecreator.EmployeeCreator;
import springcore.services.positioncreator.PositionCreator;

import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;

/**
 * The type Company emulator.
 */
public class CompanyEmulator {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int years;

    /**
     * Instantiates a new Company emulator.
     *
     * @param years the lifecycle of the company
     */
    public CompanyEmulator(int years) {
        this.years = years;
    }

    /**
     * Emulate company life
     */
    public void emulate() {

        try (AnnotationConfigApplicationContext context
                     = new AnnotationConfigApplicationContext(SpringConfig.class);
             ConnectTemporary connectTemporary = context.getBean(ConnectTemporary.class)) {

            connectTemporary.openConnection();
            connectTemporary.truncateTables(EMPLOYEES_TABLE_TRUNCATE_SQL,
                    POSITIONS_TABLE_TRUNCATE_SQL);

            Company company = new Company();
            EmployeeService employeeService = context.getBean(EmployeeService.class);
            PositionService positionService = context.getBean(PositionService.class);
            SalaryService salaryService = context.getBean(SalaryService.class);
            EmployeeCreator employeeCreator = context.getBean(EmployeeCreator.class);
            PositionCreator positionCreator = context.getBean(PositionCreator.class);

            employeeService.setCompany(company);
            positionService.setCompany(company);

            for (int year = INITIAL_YEAR_VALUE; year <= years; year++) {
                for (int month = INITIAL_MONTH_VALUE; month <= LAST_MONTH_VALUE; month++) {
                    positionService.addPositions(positionCreator);
                    salaryService.assignSalaries();
                    employeeService.hireEmployees(employeeCreator);
                    positionService.assignPositions();

                    employeeService.increaseExperience();
                    salaryService.assignBonuses();
                    salaryService.paySalary();
                    employeeService.fireEmployees();
                    positionService.clearPositions();
                    employeeService.releaseEmployees();
                    positionService.closePositions();
                    positionService.changePosition();
                }

                salaryService.increaseSalariesDueToInflation();
            }
        } catch (RuntimeException e) {
            LOGGER.error(e);
        }
    }
}
