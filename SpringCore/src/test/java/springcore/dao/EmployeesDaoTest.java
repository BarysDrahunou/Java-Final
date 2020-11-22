package springcore.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import springcore.employee.Employee;
import springcore.mappers.Mapper;
import springcore.services.*;
import springcore.statuses.EmployeeStatus;

import java.sql.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EmployeesDaoTest {

    @Mock
    Mapper<ResultSet, List<Employee>,
            List<Employee>, PreparedStatement> mapper;
    @Mock
    ConnectTemporary connectTemporary;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    @Captor
    ArgumentCaptor<List<Employee>> listArgumentCaptor;
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

        verify(mapper).add(listArgumentCaptor.capture(), eq(preparedStatement));
        verify(connectTemporary).commit();

        assertEquals(employees, listArgumentCaptor.getValue());
    }

    @Test
    public void getEmployeesByStatus() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        employeesDao.getEmployeesByStatus(status);

        verify(preparedStatement).setString(eq(1), eq(status.name()));
        verify(preparedStatement).execute();
        verify(mapper).map(resultSetArgumentCaptor.capture());

        assertEquals(resultSet, resultSetArgumentCaptor.getValue());
    }

    @Test
    public void updateEmployees() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        employeesDao.updateEmployees(employees);

        verify(mapper).update(employees,preparedStatement);
        verify(connectTemporary).commit();
    }
}