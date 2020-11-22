package springcore.services.companyservices;

import org.junit.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.company.Company;
import springcore.currency.Usd;
import springcore.dao.*;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.services.PositionCreator;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static springcore.statuses.EmployeeStatus.*;

public class PositionServiceTest {

    PositionServiceImplementation positionService;
    List<Employee> employees;
    @Mock
    PositionCreator positionCreator;
    @Mock
    Company company;
    @Mock
    Employee employee;
    @Mock
    EmployeesDao employeesDao;
    @Mock
    Position position;
    @Mock
    Position position1;
    @Mock
    Position position2;
    @Mock
    Position position3;
    @Mock
    Position position4;
    @Mock
    PositionsDao positionsDao;
    @Mock
    List<Position> positions;
    @Captor
    ArgumentCaptor<Integer> argumentCaptor;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        employees = Arrays.asList(employee, employee, employee);
        positionService = new PositionServiceImplementation(positionsDao, employeesDao);

        positionService.setCompany(company);

        Field positionsToClose = PositionServiceImplementation
                .class.getDeclaredField("positionsToClose");
        positionsToClose.setAccessible(true);
        ReflectionUtils.setField(positionsToClose, positionService, 50000);

        Field positionsToOpen = PositionServiceImplementation
                .class.getDeclaredField("positionsToOpen");
        positionsToOpen.setAccessible(true);
        ReflectionUtils.setField(positionsToOpen, positionService, 10);

        Field employeesToChangeWork = PositionServiceImplementation
                .class.getDeclaredField("employeesToChangeWork");
        employeesToChangeWork.setAccessible(true);
        ReflectionUtils.setField(employeesToChangeWork, positionService, 50000);
    }

    @Test
    public void addPositions() {
        when(positionsDao.getAllPositions()).thenReturn(positions);
        when(positionCreator.createPositionAndGet()).thenReturn(position);
        when(positions.contains(position)).thenReturn(true, true, false);
        when(positions.indexOf(position)).thenReturn(1000000);
        when(positions.remove(anyInt())).thenReturn(position);
        when(position.getVacancies()).thenReturn(0);

        positionService.addPositions(positionCreator);

        verify(position, atMost(2)).setVacancies(argumentCaptor.capture());
        verify(positions, atMost(10)).add(position);
        verify(company, atMost(10)).closeVacancy();
        verify(positionsDao).updatePositions(anyList());
        verify(positionsDao).addPositions(anyList());

        assertEquals(1, (long) argumentCaptor.getValue());
    }

    @Test
    public void assignPositions() {
        when(employeesDao.getEmployeesByStatus(eq(NEW))).thenReturn(employees);
        when(positionsDao.getPositions(anyString(), any())).thenReturn(positions);

        when(positions.size()).thenReturn(100000);

        when(positions.get(anyInt())).thenReturn(position);
        when(position.getActiveWorkers()).thenReturn(5);
        when(position.getVacancies()).thenReturn(1, 0, 10);


        positionService.assignPositions();

        verify(position).setVacancies(eq(0));
        verify(position, times(2)).setVacancies(9);
        verify(position, times(3)).setActiveWorkers(6);

        verify(positions).remove(eq(position));

        verify(employeesDao).updateEmployees(eq(employees));
        verify(positionsDao).updatePositions(anyList());
    }

    @Test
    public void clearPositions() {
        when(employeesDao.getEmployeesByStatus(FIRED)).thenReturn(employees);
        when(employee.getPosition()).thenReturn(new Position("Ment"),
                new Position("Kent"), new Position("Cement"));

        when(positionsDao.getPositions(anyString(), any())).thenReturn(positions);

        when(positions.get(0)).thenReturn(position);
        when(position.getVacancies()).thenReturn(10);
        when(position.getActiveWorkers()).thenReturn(5);

        positionService.clearPositions();

        verify(position, times(3)).setVacancies(11);
        verify(position, times(3)).setActiveWorkers(4);

        verify(company, times(3)).openVacancy();
        verify(positionsDao).updatePositions(anyList());
    }

    @Test
    public void closePositions() {
        when(positionsDao.getPositions(anyString(), any())).thenReturn(positions);
        when(positions.isEmpty()).thenReturn(false, false, false, true);
        when(positions.size()).thenReturn(100500);
        when(positions.get(anyInt())).thenReturn(position);
        when(position.getVacancies()).thenReturn(1, 0, 5);

        positionService.closePositions();

        verify(position).setVacancies(0);
        verify(position, times(2)).setVacancies(4);
        verify(positions).remove(position);
        verify(company, times(3)).closeVacancy();
        verify(positionsDao).updatePositions(anyList());
    }

    @Test
    public void changePosition() {
        List<Position> positions = new ArrayList<>();

        positions.add(position1);
        positions.add(position2);
        positions.add(position3);
        positions.add(position4);

        Usd salary1 = new Usd(100);
        Usd salary2 = new Usd(200);
        Usd salary3 = new Usd(300);

        when(positionsDao.getAllPositions()).thenReturn(positions);
        when(employeesDao.getEmployeesByStatus(WORKS)).thenReturn(employees);

        when(employee.getPosition()).thenReturn(position1, position2, position3);

        when(position1.getSalary()).thenReturn(salary1);
        when(position2.getSalary()).thenReturn(salary2);
        when(position3.getSalary()).thenReturn(salary3);
        when(position1.getVacancies()).thenReturn(5);
        when(position2.getVacancies()).thenReturn(50);
        when(position3.getVacancies()).thenReturn(500);

        positionService.changePosition();

        verify(position1, atMost(employees.size())).setActiveWorkers(anyInt());
        verify(position2, atMost(employees.size())).setActiveWorkers(anyInt());
        verify(position3, atMost(employees.size())).setActiveWorkers(anyInt());
        verify(position1, atMost(employees.size())).setVacancies(anyInt());
        verify(position2, atMost(employees.size())).setVacancies(anyInt());
        verify(position3, atMost(employees.size())).setVacancies(anyInt());

        verify(position1).setSalary(any());
        verify(position2).setSalary(any());
        verify(position3).setSalary(any());
        verify(employeesDao).updateEmployees(employees);
        verify(positionsDao).updatePositions(anyList());
    }
}