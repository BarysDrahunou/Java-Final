package springcore.mappers;

import org.springframework.stereotype.Service;
import springcore.currency.Usd;
import springcore.position.Position;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

@Service
public class PositionMapper implements Mapper<ResultSet, List<Position>,
        List<Position>, PreparedStatement> {

    @Override
    public List<Position> map(ResultSet resultSet) throws SQLException {
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
    public void add(List<Position> positions, PreparedStatement preparedStatement)
            throws SQLException {

        for (Position position : positions) {
            preparedStatement.setString(1, position.getPositionName());
            preparedStatement.setInt(2, position.getVacancies());

            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }

        preparedStatement.executeBatch();
    }

    @Override
    public void update(List<Position> positions, PreparedStatement preparedStatement)
            throws SQLException {

        for (Position position : positions) {
            preparedStatement.setInt(1, position.getVacancies());
            preparedStatement.setInt(2, position.getActiveWorkers());
            preparedStatement.setInt(3, position.getSalary().getValue());
            preparedStatement.setString(4, position.getPositionName());

            preparedStatement.addBatch();
            preparedStatement.clearParameters();
        }

        preparedStatement.executeBatch();
    }
}
