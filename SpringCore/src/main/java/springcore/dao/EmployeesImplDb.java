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

@Repository
public class EmployeesImplDb implements EmployeesDao {

    private final ConnectTemporary connectTemporary;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeesImplDb(ConnectTemporary connectTemporary,
                           EmployeeMapper employeeMapper) {
        this.connectTemporary = connectTemporary;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public void addEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(ADD_EMPLOYEES_QUERY);

        employeeMapper.add(employees, preparedStatement);

        connectTemporary.commit();
    }

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

    @Override
    public void updateEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatementForUpdate();

        employeeMapper.update(employees, preparedStatement);

        connectTemporary.commit();
    }

    private PreparedStatement getPreparedStatementForUpdate() throws SQLException {
        return connectTemporary.getPreparedStatement(UPDATE_EMPLOYEES_QUERY);
    }

    public ConnectTemporary getConnectTemporary() {
        return connectTemporary;
    }
}
