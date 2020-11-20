package springcore.services;

import org.junit.*;
import org.mockito.*;
import org.springframework.util.ReflectionUtils;
import springcore.company.Company;
import springcore.employee.Employee;
import springcore.dao.EmployeesImplDb;
import springcore.employee.EmployeeCreator;
import springcore.statuses.EmployeeStatus;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    EmployeesImplDb employeesImplDb;
    @Mock
    Company company;
    @Mock
    Employee employee1;
    @Mock
    Employee employee2;
    @Mock
    Employee employee3;
    @Mock
    EmployeeCreator employeeCreator;
    @Mock
    List<Employee> employees;
    EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        employeeService = new EmployeeServiceImplementation();
        employeeService.setCompany(company);
        employeeService.setEmployeesImplDb(employeesImplDb);
        Field employeesToFire = EmployeeServiceImplementation.class.getDeclaredField("employeesToFire");
        employeesToFire.setAccessible(true);
        ReflectionUtils.setField(employeesToFire, employeeService, 100500);
        Field employeesToHire = EmployeeServiceImplementation.class.getDeclaredField("employeesToHire");
        employeesToHire.setAccessible(true);
        ReflectionUtils.setField(employeesToHire, employeeService, 100500);
    }

    @Test
    public void hireEmployees() throws SQLException {
        when(company.getVacanciesCount()).thenReturn(3, 2, 1, 0);
        when(employeeCreator.createEmployeeAndGet()).thenReturn(employee1, employee2, employee3);
        ArgumentCaptor<EmployeeStatus> argumentCaptor = ArgumentCaptor.forClass(EmployeeStatus.class);
        employeeService.hireEmployees(employeeCreator);
        verify(employeeCreator,times(3)).createEmployeeAndGet();
        verify(employee1).setStatus(argumentCaptor.capture());
        verify(employee2).setStatus(argumentCaptor.capture());
        verify(employee3).setStatus(argumentCaptor.capture());
        assertTrue(argumentCaptor.getAllValues().stream()
                .allMatch(employeeStatus -> employeeStatus.equals(EmployeeStatus.NEW)));
        verify(company, times(3)).closeVacancy();
        verify(employeesImplDb).addEmployees(anyList());
    }


    @Test
    public void fireEmployees() throws SQLException {
        when(employeesImplDb.getEmployeesByStatus(any(EmployeeStatus.class)))
                .thenReturn(employees);
        when(employees.size()).thenReturn(3, 3, 2, 2, 1, 1, 0);
        when(employees.remove(anyInt())).thenReturn(employee1, employee2, employee3);
        employeeService.fireEmployees();
        verify(employees, times(7)).size();
        verify(employees, times(3)).remove(anyInt());
        verify(employeesImplDb).updateEmployeesStatusById(eq(EmployeeStatus.FIRED), anyList());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void increaseExperience() throws SQLException {
        when(employeesImplDb.getEmployeesByStatus(eq(EmployeeStatus.WORKS))).thenReturn(employees);
        ArgumentCaptor<List<Employee>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        employeeService.increaseExperience();
        verify(employeesImplDb).getEmployeesByStatus(EmployeeStatus.WORKS);
        verify(employeesImplDb).increaseExp(argumentCaptor.capture());
        assertEquals(employees,argumentCaptor.getValue());
    }
}