package springcore.dao;

import org.junit.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import springcore.employee.Employee;
import springcore.mappers.Mapper;
import springcore.services.connectionservices.ConnectTemporary;
import springcore.statuses.EmployeeStatus;

import java.sql.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class EmployeesDaoTest {

    @Mock
    Mapper<ResultSet, Employee,
            Employee, PreparedStatement> mapper;
    @Mock
    ConnectTemporary connectTemporary;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    @Captor
    ArgumentCaptor<Employee> argumentCaptor;
    List<Employee> employees;
    EmployeesDao employeesDao;
    Employee employee1;
    Employee employee2;
    EmployeeStatus status;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        employee1 = new Employee("Vitali", "Petrovich");
        employee2 = new Employee("Vadim", "Petrovich");

        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
        employeesDao = new EmployeesImplDb(connectTemporary, mapper);
        status = EmployeeStatus.WORKS;
    }

    @Test
    public void addEmployees() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesDao.addEmployees(employees);

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement, times(1)).executeBatch();
        verify(connectTemporary, times(1)).commit();

        verify(mapper, times(2)).add(argumentCaptor.capture(), eq(preparedStatement));

        List<Employee> capturedEmployees = argumentCaptor.getAllValues();

        assertEquals(employee1.getName(), capturedEmployees.get(0).getName());
        assertEquals(employee1.getSurname(), capturedEmployees.get(0).getSurname());
        assertEquals(employee1.getStatus(), capturedEmployees.get(0).getStatus());

        assertEquals(employee2.getName(), capturedEmployees.get(1).getName());
        assertEquals(employee2.getSurname(), capturedEmployees.get(1).getSurname());
        assertEquals(employee2.getStatus(), capturedEmployees.get(1).getStatus());

        assertEquals(2, capturedEmployees.size());
    }

    @Test
    public void getEmployeesByStatus() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, true, false);
        when(mapper.map(resultSet)).thenReturn(employee1, employee1, employee2);

        List<Employee> receivedEmployees = employeesDao.getEmployeesByStatus(status);

        verify(preparedStatement).setString(eq(1), eq(status.name()));
        verify(preparedStatement).execute();

        verify(mapper, times(3)).map(resultSet);

        assertEquals(3,receivedEmployees.size());

        assertEquals(employee1.getName(), receivedEmployees.get(0).getName());
        assertEquals(employee1.getSurname(), receivedEmployees.get(0).getSurname());
        assertEquals(employee1.getStatus(), receivedEmployees.get(0).getStatus());

        assertEquals(employee1.getName(), receivedEmployees.get(1).getName());
        assertEquals(employee1.getSurname(), receivedEmployees.get(1).getSurname());
        assertEquals(employee1.getStatus(), receivedEmployees.get(1).getStatus());

        assertEquals(employee2.getName(), receivedEmployees.get(2).getName());
        assertEquals(employee2.getSurname(), receivedEmployees.get(2).getSurname());
        assertEquals(employee2.getStatus(), receivedEmployees.get(2).getStatus());
    }

    @Test
    public void updateEmployees() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesDao.updateEmployees(employees);

        verify(connectTemporary).commit();

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();

        verify(preparedStatement).executeBatch();

        verify(mapper, times(2)).update(argumentCaptor.capture(), eq(preparedStatement));

        List<Employee> capturedEmployees = argumentCaptor.getAllValues();

        assertEquals(employee1.getName(), capturedEmployees.get(0).getName());
        assertEquals(employee1.getSurname(), capturedEmployees.get(0).getSurname());
        assertEquals(employee1.getStatus(), capturedEmployees.get(0).getStatus());

        assertEquals(employee2.getName(), capturedEmployees.get(1).getName());
        assertEquals(employee2.getSurname(), capturedEmployees.get(1).getSurname());
        assertEquals(employee2.getStatus(), capturedEmployees.get(1).getStatus());

        assertEquals(2, capturedEmployees.size());
    }
}