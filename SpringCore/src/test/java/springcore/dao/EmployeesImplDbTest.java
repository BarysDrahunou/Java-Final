package springcore.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;
import springcore.utilityconnection.ConnectTemporary;

import java.lang.reflect.Field;
import java.math.*;
import java.sql.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class EmployeesImplDbTest {

    @Mock
    ConnectTemporary connectTemporary;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    List<Employee> employees;
    EmployeesImplDb employeesImplDb;
    Employee employee1;
    Employee employee2;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        employee1 = new Employee("Vitali", "Petrovich");

        employee1.setStatus(EmployeeStatus.FIRED);
        employee1.setPersonalBonuses(new BigDecimal(BigInteger.ONE));
        employee1.setPosition(new Position("Ment"));
        employee1.setTimeWorked(1);
        employee1.setId(1);

        employee2 = new Employee("Vadim", "Petrovich");

        employee2.setStatus(EmployeeStatus.NEW);
        employee2.setPersonalBonuses(new BigDecimal(BigInteger.TEN));
        employee2.setPosition(new Position("Doctor"));
        employee2.setTimeWorked(2);
        employee2.setId(2);

        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
        employeesImplDb = new EmployeesImplDb(connectTemporary);

        Field field = EmployeesImplDb.class.getDeclaredField("connectTemporary");
        field.setAccessible(true);
        ReflectionUtils.setField(field, employeesImplDb, connectTemporary);
    }

    @Test
    public void addEmployees() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesImplDb.addEmployees(employees);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(6))
                .setString(anyInt(), stringArgumentCaptor.capture());
        List<String> stringArguments = stringArgumentCaptor.getAllValues();

        assertEquals(employee1.getName(), stringArguments.get(0));
        assertEquals(employee1.getSurname(), stringArguments.get(1));
        assertEquals(employee1.getStatus().name(), stringArguments.get(2));
        assertEquals(employee2.getName(), stringArguments.get(3));
        assertEquals(employee2.getSurname(), stringArguments.get(4));
        assertEquals(employee2.getStatus().name(), stringArguments.get(5));

        ArgumentCaptor<BigDecimal> bigDecimalArgumentCaptor = ArgumentCaptor
                .forClass(BigDecimal.class);
        verify(preparedStatement, times(2))
                .setBigDecimal(anyInt(), bigDecimalArgumentCaptor.capture());
        List<BigDecimal> bigDecimalArguments = bigDecimalArgumentCaptor.getAllValues();

        assertEquals(employee1.getPersonalBonuses(), bigDecimalArguments.get(0));
        assertEquals(employee2.getPersonalBonuses(), bigDecimalArguments.get(1));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @Test
    public void getEmployeesByStatus() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getString("NAME")).thenReturn("Vitali");
        when(resultSet.getString("SURNAME")).thenReturn("Petrovich");
        when(resultSet.getInt("ID")).thenReturn(11);
        when(resultSet.getString("STATUS")).thenReturn("WORKS");
        when(resultSet.getString("POSITION")).thenReturn("Actor");
        when(resultSet.getBigDecimal("PERSONAL_BONUSES")).thenReturn(BigDecimal.ONE);
        when(resultSet.getInt("TIME_WORKED")).thenReturn(11);

        List<Employee> employees = employeesImplDb.getEmployeesByStatus(EmployeeStatus.WORKS);
        Employee employee = employees.get(0);

        assertEquals(1, employees.size());
        assertEquals("Vitali", employee.getName());
        assertEquals("Petrovich", employee.getSurname());
        assertEquals(11, employee.getId());
        assertEquals(EmployeeStatus.WORKS, employee.getStatus());
        assertEquals(new Position("Actor"), employee.getPosition());
        assertEquals(BigDecimal.valueOf(1), employee.getPersonalBonuses());
        assertEquals(11, employee.getTimeWorked());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement).setString(anyInt(), argumentCaptor.capture());
        String status = argumentCaptor.getValue();
        assertSame(EmployeeStatus.valueOf(status), EmployeeStatus.WORKS);
    }

    @Test
    public void updateEmployees() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesImplDb.updateEmployees(employees);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(8))
                .setString(anyInt(), stringArgumentCaptor.capture());
        List<String> stringArguments = stringArgumentCaptor.getAllValues();

        assertEquals(employee1.getName(), stringArguments.get(0));
        assertEquals(employee1.getSurname(), stringArguments.get(1));
        assertEquals(employee1.getStatus().name(), stringArguments.get(2));
        assertEquals(employee1.getPosition().getPositionName(), stringArguments.get(3));
        assertEquals(employee2.getName(), stringArguments.get(4));
        assertEquals(employee2.getSurname(), stringArguments.get(5));
        assertEquals(employee2.getStatus().name(), stringArguments.get(6));
        assertEquals(employee2.getPosition().getPositionName(), stringArguments.get(7));

        ArgumentCaptor<BigDecimal> bigDecimalArgumentCaptor = ArgumentCaptor
                .forClass(BigDecimal.class);
        verify(preparedStatement, times(2))
                .setBigDecimal(anyInt(), bigDecimalArgumentCaptor.capture());

        List<BigDecimal> bigDecimalArguments = bigDecimalArgumentCaptor.getAllValues();

        assertEquals(employee1.getPersonalBonuses(), bigDecimalArguments.get(0));
        assertEquals(employee2.getPersonalBonuses(), bigDecimalArguments.get(1));

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(preparedStatement, times(4)).setInt(anyInt(),
                integerArgumentCaptor.capture());

        List<Integer> integerArguments = integerArgumentCaptor.getAllValues();

        assertEquals(employee1.getTimeWorked(), (long) integerArguments.get(0));
        assertEquals(employee1.getId(), (long) integerArguments.get(1));
        assertEquals(employee2.getTimeWorked(), (long) integerArguments.get(2));
        assertEquals(employee2.getId(), (long) integerArguments.get(3));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @Test
    public void updateEmployeesStatusByStatus() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesImplDb.updateEmployeesStatusByStatus(EmployeeStatus.FIRED, EmployeeStatus.WENT_OUT);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(2)).setString(anyInt(),
                argumentCaptor.capture());

        List<String> arguments = argumentCaptor.getAllValues();

        assertEquals(EmployeeStatus.FIRED, EmployeeStatus.valueOf(arguments.get(0)));
        assertEquals(EmployeeStatus.WENT_OUT, EmployeeStatus.valueOf(arguments.get(1)));

        verify(preparedStatement).execute();
        verify(connectTemporary).commit();
    }

    @Test
    public void updateEmployeesStatusById() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesImplDb.updateEmployeesStatusById(EmployeeStatus.WORKS, employees);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(2)).setString(anyInt(),
                stringArgumentCaptor.capture());

        List<String> stringArguments = stringArgumentCaptor.getAllValues();

        assertEquals(EmployeeStatus.WORKS.name(), stringArguments.get(0));
        assertEquals(EmployeeStatus.WORKS.name(), stringArguments.get(1));

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(preparedStatement, times(2)).setInt(anyInt(),
                integerArgumentCaptor.capture());

        List<Integer> integerArguments = integerArgumentCaptor.getAllValues();

        assertEquals(employee1.getId(), (long) integerArguments.get(0));
        assertEquals(employee2.getId(), (long) integerArguments.get(1));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @Test
    public void increaseExp() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesImplDb.increaseExp(employees);

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(preparedStatement, times(4)).setInt(anyInt(),
                integerArgumentCaptor.capture());

        List<Integer> integerArguments = integerArgumentCaptor.getAllValues();

        assertEquals(employee1.getTimeWorked() + 1, (long) integerArguments.get(0));
        assertEquals(employee1.getId(), (long) integerArguments.get(1));
        assertEquals(employee2.getTimeWorked() + 1, (long) integerArguments.get(2));
        assertEquals(employee2.getId(), (long) integerArguments.get(3));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @After
    public void destroy() throws SQLException {
        connectTemporary.close();
    }
}