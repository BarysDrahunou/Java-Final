package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.currency.Usd;
import springcore.position.Position;
import springcore.utilityconnection.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;
import static springcore.constants.VariablesConstants.*;

@Repository
public class PositionsImplDb implements PositionsDao {

    private final ConnectTemporary connectTemporary;

    @Autowired
    public PositionsImplDb(ConnectTemporary connectTemporary){
        this.connectTemporary = connectTemporary;
    }

    @Override
    public void addPositions(List<Position> positions) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(ADD_POSITIONS_QUERY);
        for (Position position : positions) {
            preparedStatement.setString(1, position.getPositionName());
            preparedStatement.setInt(2, position.getVacancies());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }

    @Override
    public List<Position> getAllPositions() throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(GET_ALL_POSITIONS_QUERY);
        return getPositionsList(preparedStatement);
    }

    @Override
    public List<Position> getPositions(String argument, Object value) throws SQLException {
        String query = String.format(GET_EXACT_POSITIONS_QUERY, argument);
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(query);
        preparedStatement.setObject(1, value);
        return getPositionsList(preparedStatement);
    }

    private List<Position> getPositionsList(PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        List<Position> positions = new ArrayList<>();
        while (resultSet.next()) {
            Position position = new Position(resultSet.getString(POSITION));
            position.setVacancies(resultSet.getInt(VACANCIES));
            position.setActiveWorkers(resultSet.getInt(ACTIVE_WORKERS));
            position.setSalary(new Usd(resultSet.getInt(SALARY)));
            positions.add(position);
        }
        return positions;
    }

    @Override
    public Usd getPositionSalary(String position) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(GET_POSITION_SALARY_QUERY);
        preparedStatement.setString(1, position);
        preparedStatement.execute();
        Usd usd = new Usd(DECIMAL_BASE);
        ResultSet resultSet = preparedStatement.getResultSet();
        if (resultSet.next()) {
            usd = new Usd(resultSet.getInt(SALARY));
        }
        return usd;
    }

    @Override
    public void updatePositions(List<Position> positions)
            throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(UPDATE_POSITIONS_QUERY);
        for (Position position : positions) {
            preparedStatement.setInt(1, position.getVacancies());
            preparedStatement.setInt(2, position.getActiveWorkers());
            preparedStatement.setInt(3, position.getSalary().getValue());
            preparedStatement.setString(4, position.getPositionName());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }

    @Override
    public void assignSalaries(List<Position> positions) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(ASSIGN_SALARIES_QUERY);
        for (Position position : positions) {
            preparedStatement.setInt(1, position.getSalary().getValue());
            preparedStatement.setString(2, position.getPositionName());
            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }
        preparedStatement.executeBatch();
        connectTemporary.commit();
    }
}
