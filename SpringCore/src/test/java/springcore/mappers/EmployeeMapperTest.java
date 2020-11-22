package springcore.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springcore.employee.Employee;
import springcore.position.Position;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static springcore.statuses.EmployeeStatus.NEW;
import static springcore.statuses.EmployeeStatus.WORKS;

public class EmployeeMapperTest {

    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    Mapper<ResultSet, List<Employee>,
            List<Employee>, PreparedStatement> employeeMapper;
    Employee employee1;
    Employee employee2;
    Position position1;
    Position position2;
    List<Employee> employees;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        position1 = new Position("Ment");
        position2 = new Position("Actor");

        employeeMapper = new EmployeeMapper();

        employee1 = new Employee("Oleg", "Denisov");
        employee2 = new Employee("Denis", "Olegov");

        employee1.setId(100500);
        employee1.setStatus(NEW);
        employee1.setPosition(position1);
        employee1.setPersonalBonuses(BigDecimal.ZERO);
        employee1.setTimeWorked(10);

        employee2.setId(500100);
        employee2.setStatus(WORKS);
        employee2.setPosition(position2);
        employee2.setPersonalBonuses(BigDecimal.TEN);
        employee2.setTimeWorked(4);

        employees = Arrays.asList(employee1, employee2);
    }

    @Test
    public void map() throws SQLException {
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString(anyString())).
                thenReturn("Oleg", "Denisov",
                        NEW.name(), position1.getPositionName())
                .thenReturn("Denis", "Olegov",
                        WORKS.name(), position2.getPositionName());
        when(resultSet.getInt(anyString())).thenReturn(100500, 10)
                .thenReturn(500100, 4);
        when(resultSet.getBigDecimal(anyString())).thenReturn(BigDecimal.ZERO)
                .thenReturn(BigDecimal.TEN);


        List<Employee> employees = employeeMapper.map(resultSet);

        Employee employee1InList = employees.get(0);
        Employee employee2InList = employees.get(1);

        assertEquals(2, employees.size());

        assertEquals(employee1.getName(), employee1InList.getName());
        assertEquals(employee1.getSurname(), employee1InList.getSurname());
        assertEquals(employee1.getId(), employee1InList.getId());
        assertEquals(employee1.getStatus(), employee1InList.getStatus());
        assertEquals(employee1.getPersonalBonuses(), employee1InList.getPersonalBonuses());
        assertEquals(employee1.getTimeWorked(), employee1InList.getTimeWorked());

        assertEquals(employee2.getName(), employee2InList.getName());
        assertEquals(employee2.getSurname(), employee2InList.getSurname());
        assertEquals(employee2.getId(), employee2InList.getId());
        assertEquals(employee2.getStatus(), employee2InList.getStatus());
        assertEquals(employee2.getPersonalBonuses(), employee2InList.getPersonalBonuses());
        assertEquals(employee2.getTimeWorked(), employee2InList.getTimeWorked());
    }

    @Test
    public void add() throws SQLException {
        employeeMapper.add(employees, preparedStatement);

        verify(preparedStatement).setString(eq(1), eq(employee1.getName()));
        verify(preparedStatement).setString(eq(2), eq(employee1.getSurname()));
        verify(preparedStatement).setString(eq(3), eq(employee1.getStatus().name()));
        verify(preparedStatement).setBigDecimal(eq(4), eq(employee1.getPersonalBonuses()));

        verify(preparedStatement).setString(eq(1), eq(employee2.getName()));
        verify(preparedStatement).setString(eq(2), eq(employee2.getSurname()));
        verify(preparedStatement).setString(eq(3), eq(employee2.getStatus().name()));
        verify(preparedStatement).setBigDecimal(eq(4), eq(employee2.getPersonalBonuses()));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();

        verify(preparedStatement).executeBatch();
    }

    @Test
    public void update() throws SQLException {
        employeeMapper.update(employees, preparedStatement);

        verify(preparedStatement).setString(eq(1), eq(employee1.getName()));
        verify(preparedStatement).setString(eq(2), eq(employee1.getSurname()));
        verify(preparedStatement).setString(eq(3), eq(employee1.getStatus().name()));
        verify(preparedStatement).setString(eq(4), eq(employee1.getPosition().getPositionName()));
        verify(preparedStatement).setBigDecimal(eq(5), eq(employee1.getPersonalBonuses()));
        verify(preparedStatement).setInt(eq(6), eq(employee1.getTimeWorked()));
        verify(preparedStatement).setInt(eq(7), eq(employee1.getId()));

        verify(preparedStatement).setString(eq(1), eq(employee2.getName()));
        verify(preparedStatement).setString(eq(2), eq(employee2.getSurname()));
        verify(preparedStatement).setString(eq(3), eq(employee2.getStatus().name()));
        verify(preparedStatement).setString(eq(4), eq(employee2.getPosition().getPositionName()));
        verify(preparedStatement).setBigDecimal(eq(5), eq(employee2.getPersonalBonuses()));
        verify(preparedStatement).setInt(eq(6), eq(employee2.getTimeWorked()));
        verify(preparedStatement).setInt(eq(7), eq(employee2.getId()));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();

        verify(preparedStatement).executeBatch();
    }
}