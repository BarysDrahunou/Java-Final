package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.employee.Employee;
import springcore.mappers.EmployeeMapper;
import springcore.statuses.EmployeeStatus;
import springcore.services.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Employees impl db.
 * Allows working with database and add, get and update
 * employees from this base
 */
@Repository
public class EmployeesImplDb implements EmployeesDao<List<Employee>, List<Employee>> {

    private final ConnectTemporary connectTemporary;
    private final EmployeeMapper employeeMapper;

    /**
     * Instantiates a new Employees impl db.
     *
     * @param connectTemporary an instance of the class which provides a connection
     *                         to database
     * @param employeeMapper   the employee mapper to perform operations with employees
     */
    @Autowired
    public EmployeesImplDb(ConnectTemporary connectTemporary,
                           EmployeeMapper employeeMapper) {
        this.connectTemporary = connectTemporary;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Add employees.
     *
     * @param employees the list of employees to insertion
     * @throws SQLException if there are problems with connection
     *                      so employees cannot be added
     */
    @Override
    public void addEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(ADD_EMPLOYEES_QUERY);

        employeeMapper.add(employees, preparedStatement);

        connectTemporary.commit();
    }

    /**
     * Gets employees by status.
     *
     * @param status the status by which employees will be retrieved
     * @return the list of employees
     * @throws SQLException if there are problems with connection
     *                      so employees cannot be got
     */
    @Override
    public List<Employee> getEmployeesByStatus(EmployeeStatus status)
            throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(GET_EMPLOYEES_BY_STATUS_QUERY);

        preparedStatement.setString(1, status.name());
        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();

        return employeeMapper.map(resultSet);
    }

    /**
     * Update employees.
     *
     * @param employees the list of employees which should be updated
     * @throws SQLException if there are problems with connection
     *                      so employees cannot be updated
     */
    @Override
    public void updateEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatementForUpdate();

        employeeMapper.update(employees, preparedStatement);

        connectTemporary.commit();
    }

    private PreparedStatement getPreparedStatementForUpdate() throws SQLException {
        return connectTemporary.getPreparedStatement(UPDATE_EMPLOYEES_QUERY);
    }

    /**
     * Gets connect temporary.
     *
     * @return the connect temporary
     */
    public ConnectTemporary getConnectTemporary() {
        return connectTemporary;
    }
}
