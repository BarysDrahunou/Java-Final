package springcore.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springcore.mappers.Mapper;
import springcore.position.Position;
import springcore.services.connectionservices.ConnectTemporary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static springcore.constants.SQLQueries.GET_EXACT_POSITIONS_QUERY;

public class PositionsDaoTest {

    @Mock
    Mapper<ResultSet, Position,
            Position, PreparedStatement> mapper;
    @Mock
    ConnectTemporary connectTemporary;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    @Captor
    ArgumentCaptor<Position> positionArgumentCaptor;
    @Captor
    ArgumentCaptor<ResultSet> resultSetArgumentCaptor;
    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;
    List<Position> positions;
    PositionsDao positionsDao;
    Position position1;
    Position position2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        position1 = new Position("Actor");
        position2 = new Position("Ment");

        positions = new ArrayList<>(Arrays.asList(position1, position2));
        positionsDao = new PositionsImplDb(connectTemporary, mapper);
    }

    @Test
    public void addPositions() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        positionsDao.addPositions(positions);

        verify(mapper, times(2)).add(positionArgumentCaptor.capture(), eq(preparedStatement));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement, times(1)).executeBatch();
        verify(connectTemporary, times(1)).commit();
    }

    @Test
    public void getAllPositions() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        positionsDao.getAllPositions();
        verify(preparedStatement).execute();
        verify(preparedStatement).getResultSet();

        while (resultSet.next()) {
            when(mapper.map(resultSet)).thenReturn(position1);
            assertEquals(mapper.map(resultSet), position1);

            when(mapper.map(resultSet)).thenReturn(position2);
            assertEquals(mapper.map(resultSet), position2);

            verify(mapper, times(2)).map(resultSet);
            verify(mapper).map(resultSetArgumentCaptor.capture());

            verify(mapper).map(resultSetArgumentCaptor.capture());
            assertEquals(resultSet, resultSetArgumentCaptor.getValue());
        }
    }

    @Test
    public void getPositions() throws SQLException {
        String argument = "Some string";
        String value = "Some value";

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        positionsDao.getPositions(argument, value);

        verify(connectTemporary).getPreparedStatement(stringArgumentCaptor.capture());
        verify(preparedStatement).setObject(eq(1), stringArgumentCaptor.capture());
        verify(preparedStatement).execute();
        while (resultSet.next()) {
            verify(mapper).map(resultSetArgumentCaptor.capture());

            assertEquals(resultSet, resultSetArgumentCaptor.getValue());

            List<String> values = stringArgumentCaptor.getAllValues();

            assertEquals(String.format(GET_EXACT_POSITIONS_QUERY, argument), values.get(0));
            assertEquals(value, values.get(1));
        }
    }

    @Test
    public void updatePositions() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        positionsDao.updatePositions(positions);

        verify(mapper, times(2)).update(positionArgumentCaptor.capture(), eq(preparedStatement));
        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }
}