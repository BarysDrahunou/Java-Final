package springcore.mappers;

import org.springframework.stereotype.Service;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Employee mapper to work with a database.
 */
@Service
public class EmployeeMapper implements Mapper<ResultSet, List<Employee>,
        List<Employee>, PreparedStatement> {

    /**
     * Map employees from database to list
     *
     * @param resultSet source object from database
     * @return list of mapped employees
     */
    @Override
    public List<Employee> map(ResultSet resultSet) {
        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add employees to database
     *
     * @param employees         list of employees to add
     * @param preparedStatement target object to put employees into database
     */
    @Override
    public void add(List<Employee> employees, PreparedStatement preparedStatement) {
        try {

            for (Employee employee : employees) {
                preparedStatement.setString(1, employee.getName());
                preparedStatement.setString(2, employee.getSurname());
                preparedStatement.setString(3, employee.getStatus().name());
                preparedStatement.setBigDecimal(4, employee.getPersonalBonuses());

                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update employees into database
     *
     * @param employees         list of employees to update
     * @param preparedStatement target object to put employees into database
     */
    @Override
    public void update(List<Employee> employees, PreparedStatement preparedStatement) {
        try {

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
