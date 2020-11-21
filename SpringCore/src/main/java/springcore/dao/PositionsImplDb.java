package springcore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springcore.mappers.PositionMapper;
import springcore.position.Position;
import springcore.utilityconnection.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static springcore.constants.SQLQueries.*;

@Repository
public class PositionsImplDb implements PositionsDao {

    private final ConnectTemporary connectTemporary;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionsImplDb(ConnectTemporary connectTemporary, PositionMapper positionMapper) {
        this.connectTemporary = connectTemporary;
        this.positionMapper = positionMapper;
    }

    @Override
    public void addPositions(List<Position> positions) throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(ADD_POSITIONS_QUERY);

        positionMapper.add(positions, preparedStatement);

        connectTemporary.commit();
    }

    @Override
    public List<Position> getAllPositions() throws SQLException {
        PreparedStatement preparedStatement = connectTemporary
                .getPreparedStatement(GET_ALL_POSITIONS_QUERY);

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();

        return positionMapper.map(resultSet);
    }

    @Override
    public List<Position> getPositions(String argument, Object value) throws SQLException {
        String query = String.format(GET_EXACT_POSITIONS_QUERY, argument);
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(query);

        preparedStatement.setObject(1, value);

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();

        return positionMapper.map(resultSet);
    }

    @Override
    public void updatePositions(List<Position> positions)
            throws SQLException {
        PreparedStatement preparedStatement = connectTemporary.getPreparedStatement(UPDATE_POSITIONS_QUERY);

        positionMapper.update(positions, preparedStatement);

        connectTemporary.commit();
    }
}
