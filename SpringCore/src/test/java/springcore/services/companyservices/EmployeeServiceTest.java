package springcore.services.companyservices;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.company.Company;
import springcore.dao.EmployeesDao;
import springcore.employee.Employee;
import springcore.services.employeecreator.EmployeeCreator;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static springcore.statuses.EmployeeStatus.*;

public class EmployeeServiceTest {

    EmployeeServiceImplementation employeeService;
    @Mock
    EmployeeCreator employeeCreator;
    @Mock
    Company company;
    @Mock
    Employee employee;
    @Mock
    EmployeesDao employeesDao;
    @Mock
    List<Employee> employees;
    @Captor
    ArgumentCaptor<List<Employee>> argumentCaptor;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        employeeService = new EmployeeServiceImplementation();

        employeeService.setCompany(company);
        employeeService.setEmployeesDao(employeesDao);

        Field employeesToFire = EmployeeServiceImplementation
                .class.getDeclaredField("employeesToFire");
        employeesToFire.setAccessible(true);
        ReflectionUtils.setField(employeesToFire, employeeService, 50000);

        Field employeesToHire = EmployeeServiceImplementation
                .class.getDeclaredField("employeesToHire");
        employeesToHire.setAccessible(true);
        ReflectionUtils.setField(employeesToHire, employeeService, 50000);
    }

    @Test
    public void hireEmployees() {
        when(company.getVacanciesCount()).thenReturn(3, 2, 1, 0);
        when(employeeCreator.createEmployeeAndGet()).thenReturn(employee);

        employeeService.hireEmployees(employeeCreator);

        verify(employee, times(3)).setStatus(eq(NEW));
        verify(company, times(3)).closeVacancy();
        verify(employeesDao).addEmployees(argumentCaptor.capture());

        assertTrue(argumentCaptor.getValue()
                .stream()
                .allMatch(employee1 -> employee1.equals(employee)));
    }


    @Test
    public void fireEmployees() {
        when(employeesDao.getEmployeesByStatus(any())).thenReturn(employees);
        when(employees.remove(anyInt())).thenReturn(employee);
        when(employees.size()).thenReturn(1, 1, 1, 1, 1, 1, 1, 1, 0);

        employeeService.fireEmployees();

        verify(employees, times(4)).remove(anyInt());
        verify(employee, times(4)).setStatus(eq(FIRED));
        verify(employeesDao).updateEmployees(anyList());
    }

    @Test
    public void releaseEmployees() {
        List<Employee> temporaryEmployees = Arrays.asList(employee, employee, employee);

        when(employeesDao.getEmployeesByStatus(eq(FIRED))).thenReturn(temporaryEmployees);

        employeeService.releaseEmployees();

        verify(employee, times(3)).setStatus(eq(WENT_OUT));
        verify(employeesDao).updateEmployees(argumentCaptor.capture());

        assertEquals(temporaryEmployees, argumentCaptor.getValue());
    }

    @Test
    public void increaseExperience() {
        List<Employee> temporaryEmployees = Arrays.asList(employee, employee, employee);

        when(employeesDao.getEmployeesByStatus(eq(WORKS))).thenReturn(temporaryEmployees);
        when(employee.getTimeWorked()).thenReturn(1, 2, 3);

        employeeService.increaseExperience();

        verify(employee).setTimeWorked(2);
        verify(employee).setTimeWorked(3);
        verify(employee).setTimeWorked(4);

        verify(employeesDao).updateEmployees(argumentCaptor.capture());

        assertEquals(temporaryEmployees, argumentCaptor.getValue());
    }
}