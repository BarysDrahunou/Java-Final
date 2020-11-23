package springcore.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import springcore.mappers.Mapper;
import springcore.position.Position;
import springcore.services.connectionservices.ConnectTemporary;

import java.sql.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static springcore.constants.SQLQueries.*;

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

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement, times(1)).executeBatch();
        verify(connectTemporary, times(1)).commit();

        verify(mapper, times(2)).add(positionArgumentCaptor.capture(), eq(preparedStatement));

        List<Position> capturedPositions = positionArgumentCaptor.getAllValues();

        assertEquals(position1, capturedPositions.get(0));
        assertEquals(position2, capturedPositions.get(1));

        assertEquals(2, capturedPositions.size());
    }

    @Test
    public void getAllPositions() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, true, false);
        when(mapper.map(resultSet)).thenReturn(position1, position1, position2);

        List<Position> receivedPositions = positionsDao.getAllPositions();

        verify(preparedStatement).getResultSet();
        verify(resultSet, times(4)).next();
        verify(mapper, times(3)).map(resultSet);
        verify(preparedStatement).execute();

        assertEquals(3, receivedPositions.size());

        assertEquals(position1, receivedPositions.get(0));
        assertEquals(position1, receivedPositions.get(1));
        assertEquals(position2, receivedPositions.get(2));
    }

    @Test
    public void getPositions() throws SQLException {
        String argument = "Some string";
        String value = "Some value";

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, true, false);
        when(mapper.map(resultSet)).thenReturn(position1, position2, position2);

        List<Position> receivedPositions = positionsDao.getPositions(argument, value);

        verify(preparedStatement).setObject(eq(1), eq(value));
        verify(preparedStatement).execute();
        verify(preparedStatement).getResultSet();
        verify(resultSet, times(4)).next();
        verify(mapper, times(3)).map(resultSet);

        verify(connectTemporary).getPreparedStatement(stringArgumentCaptor.capture());
        assertEquals(String.format(GET_EXACT_POSITIONS_QUERY, argument), stringArgumentCaptor.getValue());

        assertEquals(3, receivedPositions.size());
        assertEquals(position1, receivedPositions.get(0));
        assertEquals(position2, receivedPositions.get(1));
        assertEquals(position2, receivedPositions.get(2));
    }

    @Test
    public void updatePositions() throws SQLException {
        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);

        positionsDao.updatePositions(positions);

        verify(mapper, times(2))
                .update(positionArgumentCaptor.capture(), eq(preparedStatement));

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();

        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();

        List<Position> capturedPositions = positionArgumentCaptor.getAllValues();

        assertEquals(2, capturedPositions.size());
        assertEquals(position1, capturedPositions.get(0));
        assertEquals(position2, capturedPositions.get(1));
    }
}