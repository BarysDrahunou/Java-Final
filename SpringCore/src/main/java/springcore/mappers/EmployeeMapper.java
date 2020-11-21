package springcore.mappers;

import org.springframework.stereotype.Service;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

@Service
public class EmployeeMapper implements Mapper<ResultSet, List<Employee>,
        List<Employee>, PreparedStatement> {

    @Override
    public List<Employee> map(ResultSet resultSet) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        while (resultSet.next()) {
            Employee employee = new Employee(resultSet.getString(NAME),
                    resultSet.getString(SURNAME));

            employee.setId(resultSet.getInt(ID));
            employee.setStatus(EmployeeStatus.valueOf(resultSet.getString(STATUS)));
            employee.setPosition(new Position(resultSet.getString(POSITION)));
            employee.setPersonalBonuses(resultSet.getBigDecimal(PERSONAL_BONUSES));
            employee.setTimeWorked(resultSet.getInt(TIME_WORKED));

            employees.add(employee);
        }

        return employees;
    }

    @Override
    public void add(List<Employee> employees, PreparedStatement preparedStatement)
            throws SQLException {

        for (Employee employee : employees) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getStatus().name());
            preparedStatement.setBigDecimal(4, employee.getPersonalBonuses());

            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }

        preparedStatement.executeBatch();
    }

    @Override
    public void update(List<Employee> employees, PreparedStatement preparedStatement)
            throws SQLException {

        for (Employee employee : employees) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getStatus().name());
            preparedStatement.setString(4, employee.getPosition().getPositionName());
            preparedStatement.setBigDecimal(5, employee.getPersonalBonuses());
            preparedStatement.setInt(6, employee.getTimeWorked());
            preparedStatement.setInt(7, employee.getId());

            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }

        preparedStatement.executeBatch();
    }
}
