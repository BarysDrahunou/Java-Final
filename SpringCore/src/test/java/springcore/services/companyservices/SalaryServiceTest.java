package springcore.services.companyservices;

import org.junit.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.currency.Usd;
import springcore.dao.*;
import springcore.employee.Employee;
import springcore.position.Position;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static springcore.statuses.EmployeeStatus.*;

public class SalaryServiceTest {

    SalaryService<List<Position>, List<Employee>> salaryService;
    List<Position> positions;
    List<Employee> employees;
    @Mock
    Employee employee;
    @Mock
    EmployeesDao<List<Employee>> employeesDao;
    @Mock
    Position position;
    @Mock
    PositionsDao<List<Position>> positionsDao;
    @Captor
    ArgumentCaptor<Usd> argumentCaptor;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        positions = Arrays.asList(position, position, position);
        employees = Arrays.asList(employee, employee, employee);

        salaryService = new SalaryServiceImplementation(positionsDao, employeesDao);

        Field salaryValueMax = SalaryServiceImplementation
                .class.getDeclaredField("salaryValueMax");
        salaryValueMax.setAccessible(true);
        ReflectionUtils.setField(salaryValueMax, salaryService, 5000);
        Field percentageOfIndexing = SalaryServiceImplementation
                .class.getDeclaredField("percentageOfIndexing");
        percentageOfIndexing.setAccessible(true);
        ReflectionUtils.setField(percentageOfIndexing, salaryService, 10);
    }

    @Test
    public void assignSalaries() {
        when(positionsDao.getPositions(anyString(), any())).thenReturn(positions);

        salaryService.assignSalaries();

        verify(position, times(3)).setSalary(any());
        verify(positionsDao).updatePositions(positions);
    }

    @Test
    @SuppressWarnings("All")
    public void paySalary() {
        when(employeesDao.getEmployeesByStatus(WORKS)).thenReturn(employees);
        when(positionsDao.getAllPositions()).thenReturn(positions);

        when(position.getSalary()).thenReturn(new Usd(100));
        when(employee.getPosition()).thenReturn(position);
        when(employee.getPersonalBonuses()).thenReturn(BigDecimal.ZERO);

        salaryService.paySalary();

        verify(position, times(3)).getSalary();
        verify(employee, times(3)).getPersonalBonuses();
        verify(employee, times(3)).getTimeWorked();
    }

    @Test
    public void assignBonuses() {
        List<Employee> bigEmployeesList = Stream
                .of(employees, employees, employees, employees)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        when(employeesDao.getEmployeesByStatus(WORKS)).thenReturn(bigEmployeesList);

        salaryService.assignBonuses();

        verify(employee, times(5)).setPersonalBonuses(any());
        verify(employeesDao).updateEmployees(bigEmployeesList);
    }

    @Test
    public void increaseSalariesDueToInflation() {
        Usd salary = new Usd(100);

        when(positionsDao.getAllPositions()).thenReturn(positions);
        when(position.getSalary()).thenReturn(salary);

        salaryService.increaseSalariesDueToInflation();

        verify(position, times(3))
                .setSalary(argumentCaptor.capture());
        verify(positionsDao).updatePositions(positions);

        assertTrue(argumentCaptor.getValue().getValue() < salary.getValue() * 1.1);
    }
}