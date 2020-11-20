package springcore.services;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.company.Company;
import springcore.currency.Usd;
import springcore.employee.Employee;
import springcore.dao.*;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PositionServiceTest {

    PositionService positionService;
    @Mock
    Company company;
    @Mock
    PositionsImplDb positionsImplDb;
    @Mock
    EmployeesImplDb employeesImplDb;
    @Mock
    List<String> jobs;
    @Mock
    Employee employee1;
    @Mock
    Employee employee2;
    @Mock
    Position position1;
    @Mock
    Position position2;
    String path;
    @Mock
    List<Position> positions;
    List<Employee> employees;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        path = "src/main/resources/testJobs.txt";
        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
        positionService = new PositionService(company, positionsImplDb, employeesImplDb, path);
        Field jobsField = PositionService.class.getDeclaredField("jobs");
        jobsField.setAccessible(true);
        ReflectionUtils.setField(jobsField, positionService, jobs);
        Field positionsToOpen = PositionService.class.getDeclaredField("positionsToOpen");
        positionsToOpen.setAccessible(true);
        ReflectionUtils.setField(positionsToOpen, positionService, 10);
        Field positionsToClose = PositionService.class.getDeclaredField("positionsToClose");
        positionsToClose.setAccessible(true);
        ReflectionUtils.setField(positionsToClose, positionService, 100500);
        Field employeesToChangeWork = PositionService.class.getDeclaredField("employeesToChangeWork");
        employeesToChangeWork.setAccessible(true);
        ReflectionUtils.setField(employeesToChangeWork, positionService, 100500);
    }

    @Test
    public void addPositions() throws SQLException {
        when(positionsImplDb.getAllPositions()).thenReturn(positions);
        when(jobs.get(anyInt())).thenReturn("Rocker", "Biker", "Actor");
        when(jobs.size()).thenReturn(3);
        when(positions.contains(any(Position.class)))
                .thenReturn(true, true, false);
        when(positions.indexOf(any(Position.class))).thenReturn(1, 2);
        when(positions.get(anyInt()))
                .thenReturn(new Position("Rocker"),
                        new Position("Actor"));
        when(positions.indexOf(any(Position.class))).thenReturn(1, 2);
        positionService.addPositions();
        verify(company, atMost(10)).openVacancy();
        verify(positions, atMost(10)).remove(any(Position.class));
        verify(positions, atMost(10)).add(any(Position.class));
        verify(positionsImplDb).updatePositions(anyList());
        verify(positionsImplDb).addPositions(anyList());
    }

    @Test
    public void assignPositions() throws SQLException {
        when(employeesImplDb.getEmployeesByStatus(any())).thenReturn(employees);
        when(positionsImplDb.getPositions(any(), any())).thenReturn(positions);
        when(positions.size()).thenReturn(2);
        when(positions.get(anyInt())).thenReturn(position1, position2);
        when(positions.remove(any(Position.class))).thenReturn(true);
        when(position1.getVacancies()).thenReturn(1, 0);
        when(position2.getVacancies()).thenReturn(5, 4);
        positionService.assignPositions();
        verify(employee1).setPosition(any(Position.class));
        verify(employee1).setStatus(any(EmployeeStatus.class));
        verify(employee2).setPosition(any(Position.class));
        verify(employee2).setStatus(any(EmployeeStatus.class));
        verify(employeesImplDb).updateEmployees(anyList());
        verify(positionsImplDb).updatePositions(anyList());
        verify(positions, times(2)).size();
        verify(positions, times(1)).remove(any(Position.class));
        verify(positions, times(2)).get(anyInt());
    }

    @Test
    public void clearPositions() throws SQLException {
        when(employeesImplDb.getEmployeesByStatus(any(EmployeeStatus.class))).thenReturn(employees);
        when(employee1.getPosition()).thenReturn(position1);
        when(employee2.getPosition()).thenReturn(position2);
        when(position1.getPositionName()).thenReturn("Actor");
        when(position2.getPositionName()).thenReturn("Ment");
        when(positionsImplDb.getPositions(anyString(), anyString())).thenReturn(positions);
        when(positions.get(0)).thenReturn(position1, position2);
        when(position1.getVacancies()).thenReturn(1);
        when(position2.getVacancies()).thenReturn(4);
        positionService.clearPositions();
        verify(employeesImplDb).updateEmployeesStatusByStatus(any(EmployeeStatus.class)
                , any(EmployeeStatus.class));
        verify(company, times(2)).openVacancy();
        verify(positionsImplDb).updatePositions(anyList());
    }

    @Test
    public void closePositions() throws SQLException {
        @SuppressWarnings("unchecked")
        List<Position> positions = mock(List.class);
        when(positionsImplDb.getPositions(anyString(), anyInt())).thenReturn(positions);
        when(positions.isEmpty()).thenReturn(false, false, true);
        when(positions.size()).thenReturn(2, 1);
        when(positions.get(anyInt())).thenReturn(position1, position2);
        when(positions.remove(any(Position.class))).thenReturn(true);
        when(position1.getVacancies()).thenReturn(1, 0);
        when(position2.getVacancies()).thenReturn(5, 4);
        positionService.closePositions();
        verify(positions, times(1)).remove(any(Position.class));
        verify(company, times(2)).closeVacancy();
        verify(positionsImplDb).updatePositions(anyList());
    }

    @Test
    public void changePosition() throws SQLException {
        employees.addAll(new ArrayList<>(employees));
        when(positionsImplDb.getAllPositions()).thenReturn(positions);
        when(employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS)).thenReturn(employees);
        when(positions.stream()).thenReturn(Stream.of(position1,position2));
        when(employee1.getPosition()).thenReturn(position1);
        when(employee2.getPosition()).thenReturn(position2);
        when(positions.get(anyInt())).thenReturn(position1,position2);
        Usd salary1 = new Usd(100);
        Usd salary2 = new Usd(500);
        when(position1.getSalary()).thenReturn(salary1);
        when(position2.getSalary()).thenReturn(salary2);
        int firstPositionVacancies=5;
        int secondPositionVacancies=10;
        when(position1.getVacancies()).thenReturn(firstPositionVacancies);
        when(position2.getVacancies()).thenReturn(secondPositionVacancies);
        ArgumentCaptor<Usd> argumentCaptor=ArgumentCaptor.forClass(Usd.class);
        when(positions.get(anyInt())).thenReturn(position2,position1,position1,position2);
        positionService.changePosition();
        verify(position1,times(2)).setSalary(argumentCaptor.capture());
        verify(position2,times(2)).setSalary(argumentCaptor.capture());
        assertEquals(salary1,argumentCaptor.getAllValues().get(1));
        assertEquals(salary2,argumentCaptor.getAllValues().get(0));
        verify(employeesImplDb).updateEmployees(anyList());
    }
}