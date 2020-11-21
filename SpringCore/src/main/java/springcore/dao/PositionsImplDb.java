package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.mappers.PositionMapper;
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
public class PositionsImplDb implements PositionsDao<List<Position>, List<Position>> {

    private final ConnectTemporary connectTemporary;
    private final PositionMapper positionMapper;

    /**
     * Instantiates a new Positions impl db.
     *
     * @param connectTemporary an instance of the class which provides a connection
     *                         to database
     * @param positionMapper   the position mapper to perform operations with positions
     */
    @Autowired
    public PositionsImplDb(ConnectTemporary connectTemporary, PositionMapper positionMapper) {
        this.connectTemporary = connectTemporary;
        this.positionMapper = positionMapper;
    }

    /**
     * Add positions.
     *
     * @param positions the list of positions to insertion
     * @throws SQLException if there are problems with connection
     *                      so positions cannot be added
     */
    @Override
    public void addPositions(List<Position> positions) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(ADD_POSITIONS_QUERY);

        positionMapper.add(positions, preparedStatement);

        connectTemporary.commit();
    }

    /**
     * Gets all positions.
     *
     * @return the list of all positions from database
     * @throws SQLException if there are problems with connection
     *                      so positions cannot be got
     */
    @Override
    public List<Position> getAllPositions() throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(GET_ALL_POSITIONS_QUERY);

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();

        return positionMapper.map(resultSet);
    }

    /**
     * Gets positions by status.
     *
     * @param argument the argument by which positions will be retrieved
     * @param value the value by which positions will be retrieved
     * @return the list of positions
     * @throws SQLException if there are problems with connection
     *                      so positions cannot be got
     */
    @Override
    public List<Position> getPositions(String argument, Object value) throws SQLException {
        String query = String.format(GET_EXACT_POSITIONS_QUERY, argument);
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(query);

        preparedStatement.setObject(1, value);

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();

        return positionMapper.map(resultSet);
    }

    /**
     * Update positions.
     *
     * @param positions the list of positions which should be updated
     * @throws SQLException if there are problems with connection
     *                      so positions cannot be updated
     */
    @Override
    public void updatePositions(List<Position> positions)
            throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(UPDATE_POSITIONS_QUERY);

        positionMapper.update(positions, preparedStatement);

        connectTemporary.commit();
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
