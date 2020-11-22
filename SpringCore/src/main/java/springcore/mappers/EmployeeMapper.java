package springcore.mappers;

import org.springframework.stereotype.Service;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.sql.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Employee mapper to work with a database.
 */
@Service
public class EmployeeMapper implements Mapper<ResultSet, Employee,
        Employee, PreparedStatement> {

    /**
     * Map employees from database to list
     *
     * @param resultSet source object from database
     * @return mapped employee
     */
    @Override
    public Employee map(ResultSet resultSet) {
        try {
            Employee employee = new Employee(resultSet.getString(NAME),
                    resultSet.getString(SURNAME));

            employee.setId(resultSet.getInt(ID));
            employee.setStatus(EmployeeStatus.valueOf(resultSet.getString(STATUS)));
            employee.setPosition(new Position(resultSet.getString(POSITION)));
            employee.setPersonalBonuses(resultSet.getBigDecimal(PERSONAL_BONUSES));
            employee.setTimeWorked(resultSet.getInt(TIME_WORKED));

            return employee;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add employees to database
     *
     * @param employee          employee to add
     * @param preparedStatement target object to put employee into database
     */
    @Override
    public void add(Employee employee, PreparedStatement preparedStatement) {
        try {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getStatus().name());
            preparedStatement.setBigDecimal(4, employee.getPersonalBonuses());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update employees into database
     *
     * @param employee          employee to update
     * @param preparedStatement target object to put employee into database
     */
    @Override
    public void update(Employee employee, PreparedStatement preparedStatement) {
        try {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getStatus().name());
            preparedStatement.setString(4, employee.getPosition().getPositionName());
            preparedStatement.setBigDecimal(5, employee.getPersonalBonuses());
            preparedStatement.setInt(6, employee.getTimeWorked());
            preparedStatement.setInt(7, employee.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
