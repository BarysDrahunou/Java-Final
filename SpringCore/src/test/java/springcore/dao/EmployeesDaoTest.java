package springcore.dao;

import org.mockito.*;
import springcore.employee.Employee;
import springcore.mappers.Mapper;
import springcore.services.connectionservices.ConnectTemporary;
import springcore.statuses.EmployeeStatus;

import java.sql.*;
import java.util.*;

public class EmployeesDaoTest {

    @Mock
    Mapper<ResultSet, List<Employee>,
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


//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//        employee1 = new Employee("Vitali", "Petrovich");
//        employee2 = new Employee("Vadim", "Petrovich");
//
//        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
//        employeesDao = new EmployeesImplDb(connectTemporary, mapper);
//        status = EmployeeStatus.WORKS;
//    }
//
//    @Test
//    public void addEmployees() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//
//        employeesDao.addEmployees(employees);
//
//        verify(mapper).add(argumentCaptor.capture(), eq(preparedStatement));
//        verify(connectTemporary).commit();
//
//        assertEquals(employee, argumentCaptor.getValue());
//    }
//
//    @Test
//    public void getEmployeesByStatus() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.getResultSet()).thenReturn(resultSet);
//
//        employeesDao.getEmployeesByStatus(status);
//
//        verify(preparedStatement).setString(eq(1), eq(status.name()));
//        verify(preparedStatement).execute();
//        verify(mapper).map(resultSetArgumentCaptor.capture());
//
//        assertEquals(resultSet, resultSetArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void updateEmployees() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//
//        employeesDao.updateEmployees(employees);
//
//        verify(mapper).update(employee,preparedStatement);
//        verify(connectTemporary).commit();
//    }
}