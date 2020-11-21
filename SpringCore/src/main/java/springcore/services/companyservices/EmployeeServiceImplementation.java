package springcore.services.companyservices;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import springcore.annotations.InjectRandomInt;
import springcore.company.Company;
import springcore.employee.Employee;
import springcore.dao.EmployeesImplDb;
import springcore.services.EmployeeCreator;
import springcore.statuses.EmployeeStatus;

import java.sql.SQLException;
import java.util.*;

import static springcore.constants.LogMessages.*;
import static springcore.statuses.EmployeeStatus.*;

/**
 * The type Employee service implementation.
 */
@Service
@Scope("prototype")
public class EmployeeServiceImplementation implements EmployeeService {

    private static final Logger LOGGER = LogManager.getLogger();
    private Company company;
    private EmployeesImplDb employeesImplDb;
    @InjectRandomInt(max = 5)
    private int employeesToFire;
    @InjectRandomInt(max = 10)
    private int employeesToHire;

    /**
     * Creates new employees via employeeCreator and hired them into a company
     *
     * @param employeeCreator the instance of EmployeeCreator.class to hire new employees
     * @throws SQLException if there are problems with database
     */
    public void hireEmployees(EmployeeCreator employeeCreator) throws SQLException {

        int amountEmployeesToHire = new Random().nextInt(employeesToHire + 1);
        List<Employee> employeesToHireList = new ArrayList<>();

        for (int i = 0; i < amountEmployeesToHire && company.getVacanciesCount() > 0; i++) {
            Employee employee = employeeCreator.createEmployeeAndGet();

            employee.setStatus(EmployeeStatus.NEW);

            employeesToHireList.add(employee);
            company.closeVacancy();

            LOGGER.info(String.format(HIRED_EMPLOYEE_MESSAGE, employee));
        }

        employeesImplDb.addEmployees(employeesToHireList);
    }

    /**
     * Fire employees from a company
     *
     * @throws SQLException if there are problems with database
     */
    public void fireEmployees() throws SQLException {
        List<Employee> employees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS);
        int amountEmployeesToFire = new Random().nextInt(employeesToFire + 1);
        List<Employee> employeesToFireList = new ArrayList<>();

        for (int i = 0; i < amountEmployeesToFire && employees.size() > 0; i++) {
            Employee employee = employees.remove(new Random().nextInt(employees.size()));

            employee.setStatus(FIRED);
            employeesToFireList.add(employee);

            LOGGER.info(String.format(FIRED_EMPLOYEE_MESSAGE, employee));
        }

        employeesImplDb.updateEmployees(employeesToFireList);
    }

    /**
     * Release employees from a company after they have received their salaries.
     *
     * @throws SQLException if there are problems with database
     */
    public void releaseEmployees() throws SQLException {
        List<Employee> employees = employeesImplDb
                .getEmployeesByStatus(FIRED);

        employees.forEach(employee -> employee.setStatus(WENT_OUT));

        employeesImplDb.updateEmployees(employees);
    }

    /**
     * Increase experience of current company's employees
     *
     * @throws SQLException if there are problems with database
     */
    public void increaseExperience() throws SQLException {
        List<Employee> employees = employeesImplDb
                .getEmployeesByStatus(EmployeeStatus.WORKS);

        employees.forEach(employee -> employee
                .setTimeWorked(employee.getTimeWorked() + 1));

        employeesImplDb.updateEmployees(employees);
    }

    /**
     * Sets company to this service
     *
     * @param company company for which current service will operate
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Sets employees impl db.
     *
     * @param employeesImplDb the employees impl db
     */
    @Autowired
    public void setEmployeesImplDb(EmployeesImplDb employeesImplDb) {
        this.employeesImplDb = employeesImplDb;
    }

    /**
     * Gets employees impl db.
     *
     * @return the employees impl db
     */
    public EmployeesImplDb getEmployeesImplDb() {
        return employeesImplDb;
    }
}
