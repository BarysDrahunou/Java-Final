package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.employee.Employee;
import springcore.mappers.Mapper;
import springcore.statuses.EmployeeStatus;
import springcore.services.connectionservices.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Employees impl db.
 * Allows working with database and add, get and update
 * employees from this base
 */
@Repository
public class EmployeesImplDb implements EmployeesDao {

    private final ConnectTemporary connectTemporary;
    private final Mapper<ResultSet, Employee,
            Employee, PreparedStatement> mapper;

    /**
     * Instantiates a new Employees impl db.
     *
     * @param connectTemporary an instance of the class which provides a connection
     *                         to database
     * @param mapper           the employee mapper to perform operations with employees
     */
    @Autowired
    public EmployeesImplDb(ConnectTemporary connectTemporary,
                           Mapper<ResultSet, Employee,
                                   Employee, PreparedStatement> mapper) {
        this.connectTemporary = connectTemporary;
        this.mapper = mapper;
    }

    /**
     * Add employees.
     *
     * @param employees the list of employees to insertion
     */
    @Override
    public void addEmployees(List<Employee> employees) {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(ADD_EMPLOYEES_QUERY);

            for (Employee employee : employees) {
                mapper.add(employee, preparedStatement);

                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();
            connectTemporary.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets employees by status.
     *
     * @param status the status by which employees will be retrieved
     * @return the list of employees
     */
    @Override
    public List<Employee> getEmployeesByStatus(EmployeeStatus status) {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(GET_EMPLOYEES_BY_STATUS_QUERY);

            preparedStatement.setString(1, status.name());
            preparedStatement.execute();

            List<Employee> employees = new ArrayList<>();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                employees.add(mapper.map(resultSet));
            }

            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update employees.
     *
     * @param employees the list of employees which should be updated
     */
    @Override
    public void updateEmployees(List<Employee> employees) {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(UPDATE_EMPLOYEES_QUERY);

            for (Employee employee : employees) {
                mapper.update(employee, preparedStatement);

                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();
            connectTemporary.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectTemporary getConnectTemporary() {
        return connectTemporary;
    }

    public Mapper<ResultSet, Employee, Employee, PreparedStatement> getMapper() {
        return mapper;
    }
}
