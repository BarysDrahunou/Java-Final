package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.mappers.Mapper;
import springcore.position.Position;
import springcore.services.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

/**
 * The type Positions impl db.
 * Allows working with database and add, get and update
 * positions from this base
 */
@Repository
public class PositionsImplDb implements PositionsDao {

    private final ConnectTemporary connectTemporary;
    private final Mapper<ResultSet, List<Position>,
            List<Position>, PreparedStatement> mapper;

    /**
     * Instantiates a new Positions impl db.
     *
     * @param connectTemporary an instance of the class which provides a connection
     *                         to database
     * @param mapper           the position mapper to perform operations with positions
     */
    @Autowired
    public PositionsImplDb(ConnectTemporary connectTemporary, Mapper<ResultSet, List<Position>,
            List<Position>, PreparedStatement> mapper) {
        this.connectTemporary = connectTemporary;
        this.mapper = mapper;
    }

    /**
     * Add positions.
     *
     * @param positions the list of positions to insertion
     */
    @Override
    public void addPositions(List<Position> positions) {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(ADD_POSITIONS_QUERY);

            mapper.add(positions, preparedStatement);

            connectTemporary.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all positions.
     *
     * @return the list of all positions from database
     */
    @Override
    public List<Position> getAllPositions() {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(GET_ALL_POSITIONS_QUERY);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            return mapper.map(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets positions by status.
     *
     * @param argument the argument by which positions will be retrieved
     * @param value    the value by which positions will be retrieved
     * @return the list of positions
     */
    @Override
    public List<Position> getPositions(String argument, Object value) {
        try {
            String query = String.format(GET_EXACT_POSITIONS_QUERY, argument);
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(query);

            preparedStatement.setObject(1, value);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            return mapper.map(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update positions.
     *
     * @param positions the list of positions which should be updated
     */
    @Override
    public void updatePositions(List<Position> positions) {
        try {
            PreparedStatement preparedStatement = connectTemporary
                    .getPreparedStatement(UPDATE_POSITIONS_QUERY);

            mapper.update(positions, preparedStatement);

            connectTemporary.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectTemporary getConnectTemporary() {
        return connectTemporary;
    }

    public Mapper<ResultSet, List<Position>, List<Position>, PreparedStatement> getMapper() {
        return mapper;
    }
}
