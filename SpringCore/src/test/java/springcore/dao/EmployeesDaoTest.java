package springcore.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springcore.employee.Employee;
import springcore.mappers.Mapper;
import springcore.services.connectionservices.ConnectTemporary;
import springcore.statuses.EmployeeStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    @Captor
    ArgumentCaptor<ResultSet> resultSetArgumentCaptor;
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
    }

    @Test
    public void getEmployeesByStatus() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        employeesDao.getEmployeesByStatus(status);

        verify(preparedStatement).setString(eq(1), eq(status.name()));
        verify(preparedStatement, times(1)).execute();

        when(mapper.map(resultSet)).thenReturn(employee1);
        when(mapper.map(resultSet)).thenReturn(employee2);
        assertEquals(mapper.map(resultSet), employee1);
        assertEquals(mapper.map(resultSet), employee2);

        verify(mapper, atLeastOnce()).map(resultSet);
        verify(mapper, times(2)).map(resultSet);
    }

    @Test
    public void updateEmployees() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesDao.updateEmployees(employees);
        for (Employee employee : employees) {
            verify(mapper, times(2)).update(employee, preparedStatement);

        }
        verify(connectTemporary).commit();

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();

        verify(preparedStatement).executeBatch();
    }
}