package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.employee.Employee;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;
import springcore.utilityconnection.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

@Repository
public class EmployeesImplDb implements EmployeesDao {

    private final ConnectTemporary connectTemporary;

    @Autowired
    public EmployeesImplDb(ConnectTemporary connectTemporary){
        this.connectTemporary = connectTemporary;
    }

    @Override
    public void addEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(ADD_EMPLOYEES_QUERY);
        for (Employee employee : employees) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getStatus().name());
            preparedStatement.setBigDecimal(4, employee.getPersonalBonuses());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }

    @Override
    public List<Employee> getEmployeesByStatus(EmployeeStatus status) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(GET_EMPLOYEES_BY_STATUS_QUERY);
        preparedStatement.setString(1, status.name());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
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
    public void updateEmployees(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement =
                connectTemporary.getPreparedStatement(UPDATE_EMPLOYEES_QUERY);
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
        connectTemporary.commit();
    }

    @Override
    public void updateEmployeesStatusByStatus(EmployeeStatus setStatus, EmployeeStatus findStatus)
            throws SQLException {
        PreparedStatement preparedStatement =
                connectTemporary.getPreparedStatement(UPDATE_EMPLOYEES_STATUS_BY_STATUS_QUERY);
        preparedStatement.setString(1, setStatus.name());
        preparedStatement.setString(2, findStatus.name());
        preparedStatement.execute();
        connectTemporary.commit();
    }

    @Override
    public void updateEmployeesStatusById(EmployeeStatus status, List<Employee> employees)
            throws SQLException {
        PreparedStatement preparedStatement =
                connectTemporary.getPreparedStatement(UPDATE_EMPLOYEES_STATUS_BY_ID_QUERY);
        for (Employee employee : employees) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, employee.getId());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }

    @Override
    public void increaseExp(List<Employee> employees) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(INCREASE_EXPERIENCE_QUERY);
        for (Employee employee : employees) {
            preparedStatement.setInt(1, employee.getTimeWorked() + 1);
            preparedStatement.setInt(2, employee.getId());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }
}
