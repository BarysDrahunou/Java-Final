package springcore.services;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import springcore.annotations.InjectRandomInt;
import springcore.company.Company;
import springcore.employee.Employee;
import springcore.dao.EmployeesImplDb;
import springcore.employee.EmployeeCreator;
import springcore.statuses.EmployeeStatus;

import java.sql.SQLException;
import java.util.*;

import static springcore.constants.LogMessages.*;

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

    public void fireEmployees() throws SQLException {
        List<Employee> employees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS);
        int amountEmployeesToFire = new Random().nextInt(employeesToFire + 1);
        List<Employee> employeesToFireList = new ArrayList<>();

        for (int i = 0; i < amountEmployeesToFire && employees.size() > 0; i++) {
            Employee employee = employees.remove(new Random().nextInt(employees.size()));

            employeesToFireList.add(employee);
            LOGGER.info(String.format(FIRED_EMPLOYEE_MESSAGE, employee));
        }

        employeesImplDb.updateEmployeesStatusById(EmployeeStatus.FIRED, employeesToFireList);
    }

    public void increaseExperience() throws SQLException {
        List<Employee> employees = employeesImplDb
                .getEmployeesByStatus(EmployeeStatus.WORKS);

        employeesImplDb.increaseExp(employees);
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Autowired
    public void setEmployeesImplDb(EmployeesImplDb employeesImplDb) {
        this.employeesImplDb = employeesImplDb;
    }
}
