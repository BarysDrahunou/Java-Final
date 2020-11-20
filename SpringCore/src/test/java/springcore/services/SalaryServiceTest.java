package springcore.services;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.salary.Salary;
import springcore.statuses.EmployeeStatus;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SalaryServiceTest {

    @Mock
    PositionsImplDb positionsImplDb;
    @Mock
    EmployeesImplDb employeesImplDb;
    @Mock
    Position position1;
    @Mock
    Position position2;
    @Mock
    Employee employee1;
    @Mock
    Employee employee2;
    List<Position> positions;
    List<Employee> employees;
    SalaryService salaryService;

    @Before
    public void setUp() throws NoSuchFieldException{
        MockitoAnnotations.initMocks(this);
        salaryService = new SalaryServiceImplementation(positionsImplDb, employeesImplDb);
        positions = new ArrayList<>(Arrays.asList(position1, position2));
        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
        Field salaryValueMax = SalaryServiceImplementation.class.getDeclaredField("salaryValueMax");
        salaryValueMax.setAccessible(true);
        ReflectionUtils.setField(salaryValueMax, salaryService, 100500);
        Field percentageOfIndexing = SalaryServiceImplementation.class.getDeclaredField("percentageOfIndexing");
        percentageOfIndexing.setAccessible(true);
        ReflectionUtils.setField(percentageOfIndexing, salaryService, 10);
    }

    @Test
    public void assignSalaries() throws SQLException {
        when(positionsImplDb.getPositions(anyString(), any())).thenReturn(positions);
        salaryService.assignSalaries();
        verify(positionsImplDb).assignSalaries(anyList());
    }

    @Test
    @SuppressWarnings("all")
    public void paySalary() throws SQLException {
        when(employeesImplDb.getEmployeesByStatus(any(EmployeeStatus.class))).thenReturn(employees);
        when(employee1.getPosition()).thenReturn(position1);
        when(position1.getPositionName()).thenReturn("Ment");
        when(positionsImplDb.getPositionSalary("Ment")).thenReturn(new Usd(500));
        when(employee2.getPosition()).thenReturn(position2);
        when(position2.getPositionName()).thenReturn("Actor");
        when(positionsImplDb.getPositionSalary("Actor")).thenReturn(new Usd(100));
        when(employee1.getTimeWorked()).thenReturn(2);
        when(employee2.getTimeWorked()).thenReturn(10);
        when(employee1.getPersonalBonuses()).thenReturn(BigDecimal.ONE);
        when(employee2.getPersonalBonuses()).thenReturn(BigDecimal.ZERO);
        salaryService.paySalary();
        verify(positionsImplDb, times(2)).getPositionSalary(anyString());
        verify(employee1).getTimeWorked();
        verify(employee1).getPersonalBonuses();
        verify(employee2).getTimeWorked();
        verify(employee2).getPersonalBonuses();
    }

    @Test
    public void assignBonuses() throws SQLException {
        List<Employee> employeesList = new ArrayList<>(Arrays.asList(employee1,
                employee1, employee1, employee2, employee2));
        when(employeesImplDb.getEmployeesByStatus(any(EmployeeStatus.class))).thenReturn(employeesList);
        ArgumentCaptor<BigDecimal> argumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        salaryService.assignBonuses();
        verify(employee1).setPersonalBonuses(argumentCaptor.capture());
        verify(employee2).setPersonalBonuses(argumentCaptor.capture());
        List<BigDecimal> values=argumentCaptor.getAllValues();
        assertTrue(BigDecimal.TEN.compareTo(values.get(0))>0);
        assertTrue(BigDecimal.TEN.negate().compareTo(values.get(1))<0);
        verify(employeesImplDb).updateEmployees(employeesList);
    }

    @Test
    @SuppressWarnings("all")
    public void increaseSalariesDueToInflation() throws SQLException {
        when(positionsImplDb.getAllPositions()).thenReturn(positions);
        Usd salary1 = new Usd(100);
        Usd salary2 = new Usd(300);
        when(position1.getSalary()).thenReturn(salary1);
        when(position2.getSalary()).thenReturn(salary2);
        ArgumentCaptor<Usd> argumentCaptor = ArgumentCaptor.forClass(Usd.class);
        salaryService.increaseSalariesDueToInflation();
        verify(positionsImplDb).updatePositions(positions);
        verify(position1).getSalary();
        verify(position1).setSalary(argumentCaptor.capture());
        verify(position2).getSalary();
        verify(position2).setSalary(argumentCaptor.capture());
        assertTrue(argumentCaptor.getAllValues().get(0).getValue()
                < Salary.changeSalaryFromInflation(17, salary1).getValue());
        assertTrue(argumentCaptor.getAllValues().get(1).getValue()
                < Salary.changeSalaryFromInflation(17, salary2).getValue());
    }
}