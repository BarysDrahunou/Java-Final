package springcore.mappers;

import org.springframework.stereotype.Service;
import springcore.currency.Usd;
import springcore.position.Position;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Position mapper to work with a database.
 */
@Service
public class PositionMapper implements Mapper<ResultSet, List<Position>,
        List<Position>, PreparedStatement> {

    /**
     * Map positions from database to list
     *
     * @param resultSet source object from database
     * @return list of mapped positions
     */
    @Override
    public List<Position> map(ResultSet resultSet) {
        try {
            List<Position> positions = new ArrayList<>();

            while (resultSet.next()) {
                Position position = new Position(resultSet.getString(POSITION));

                position.setVacancies(resultSet.getInt(VACANCIES));
                position.setActiveWorkers(resultSet.getInt(ACTIVE_WORKERS));
                position.setSalary(new Usd(resultSet.getInt(SALARY)));

                positions.add(position);
            }

            return positions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add positions to database
     *
     * @param positions         list of positions to add
     * @param preparedStatement target object to put positions into database
     */
    @Override
    public void add(List<Position> positions, PreparedStatement preparedStatement) {
        try {

            for (Position position : positions) {
                preparedStatement.setString(1, position.getPositionName());
                preparedStatement.setInt(2, position.getVacancies());
                preparedStatement.setInt(3, position.getActiveWorkers());
                preparedStatement.setInt(4, position.getSalary().getValue());

                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update positions into database
     *
     * @param positions         list of positions to update
     * @param preparedStatement target object to put positions into database
     */
    @Override
    public void update(List<Position> positions, PreparedStatement preparedStatement) {
        try {

            for (Position position : positions) {
                preparedStatement.setInt(1, position.getVacancies());
                preparedStatement.setInt(2, position.getActiveWorkers());
                preparedStatement.setInt(3, position.getSalary().getValue());
                preparedStatement.setString(4, position.getPositionName());

                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
