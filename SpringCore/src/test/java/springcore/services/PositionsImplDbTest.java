package springcore.services;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;
import springcore.currency.Usd;
import springcore.dao.PositionsImplDb;
import springcore.mappers.PositionMapper;
import springcore.position.Position;
import springcore.services.ConnectTemporary;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class PositionsImplDbTest {

    @Mock
    PositionMapper positionMapper;
    @Mock
    ConnectTemporary connectTemporary;
    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    List<Position> positions;
    PositionsImplDb positionsImplDb;
    Position position1;
    Position position2;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        position1 = new Position("Actor");
        position1.setVacancies(5);
        position1.setActiveWorkers(0);
        position1.setSalary(new Usd(500));
        position2 = new Position("Accountant");
        position2.setVacancies(0);
        position2.setActiveWorkers(3);
        position2.setSalary(new Usd(250));
        positions = new ArrayList<>(Arrays.asList(position1, position2));
        positionsImplDb = new PositionsImplDb(connectTemporary,positionMapper);
        Field field = PositionsImplDb.class.getDeclaredField("connectTemporary");
        field.setAccessible(true);
        ReflectionUtils.setField(field, positionsImplDb, connectTemporary);
    }

    @Test
    public void addPositions() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        positionsImplDb.addPositions(positions);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(2)).setString(anyInt(),
                stringArgumentCaptor.capture());
        List<String> stringArguments = stringArgumentCaptor.getAllValues();
        assertEquals(position1.getPositionName(), stringArguments.get(0));
        assertEquals(position2.getPositionName(), stringArguments.get(1));
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(preparedStatement, times(2)).setInt(anyInt(),
                integerArgumentCaptor.capture());
        List<Integer> integerArguments = integerArgumentCaptor.getAllValues();
        assertEquals(position1.getVacancies(), (long) integerArguments.get(0));
        assertEquals(position2.getVacancies(), (long) integerArguments.get(1));
        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @Test
    public void getAllPositions() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("POSITION")).thenReturn("Actor");
        when(resultSet.getInt(anyString())).thenReturn(11).thenReturn(12).thenReturn(13);
        List<Position> positions = positionsImplDb.getAllPositions();
        verify(preparedStatement).execute();
        assertEquals(1, positions.size());
        Position position = positions.get(0);
        assertEquals("Actor", position.getPositionName());
        assertEquals(11, position.getVacancies());
        assertEquals(12, position.getActiveWorkers());
        assertEquals(13, position.getSalary().getValue());
    }

    @Test
    public void getPositions() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("POSITION")).thenReturn("Actor").thenReturn("Ment");
        when(resultSet.getInt(anyString())).thenReturn(11).thenReturn(12).thenReturn(13)
                .thenReturn(20).thenReturn(21).thenReturn(22);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Object value = new Object();
        List<Position> positions = positionsImplDb.getPositions(anyString(), value);
        verify(preparedStatement).setObject(anyInt(), argumentCaptor.capture());
        assertEquals(value, argumentCaptor.getValue());
        verify(preparedStatement).execute();
        verify(resultSet, times(6)).getInt(anyString());
        verify(resultSet, times(2)).getString(anyString());
        assertEquals(2, positions.size());
        Position position1 = positions.get(0);
        assertEquals("Actor", position1.getPositionName());
        assertEquals(11, position1.getVacancies());
        assertEquals(12, position1.getActiveWorkers());
        assertEquals(13, position1.getSalary().getValue());
        Position position2 = positions.get(1);
        assertEquals("Ment", position2.getPositionName());
        assertEquals(20, position2.getVacancies());
        assertEquals(21, position2.getActiveWorkers());
        assertEquals(22, position2.getSalary().getValue());
    }

//    @Test
//    public void getPositionSalary() throws SQLException {
//
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.getResultSet()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true);
//        when(resultSet.getInt(anyString())).thenReturn(100500);
//        Position position = new Position("President");
//        Usd salary = positionsImplDb.getPositionSalary(position.getPositionName());
//        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
//        verify(preparedStatement).setString(anyInt(), argumentCaptor.capture());
//        verify(preparedStatement).execute();
//        assertEquals(position.getPositionName(), argumentCaptor.getValue());
//        assertEquals(new Usd(100500), salary);
//    }

    @Test
    public void updatePositions() throws SQLException {

        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
        positionsImplDb.updatePositions(positions);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(preparedStatement, times(2)).setString(anyInt(),
                stringArgumentCaptor.capture());
        List<String> stringArguments = stringArgumentCaptor.getAllValues();
        assertEquals(position1.getPositionName(), stringArguments.get(0));
        assertEquals(position2.getPositionName(), stringArguments.get(1));
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(preparedStatement, times(6)).setInt(anyInt(),
                integerArgumentCaptor.capture());
        List<Integer> integerArguments = integerArgumentCaptor.getAllValues();
        assertEquals(position1.getVacancies(), (long) integerArguments.get(0));
        assertEquals(position1.getActiveWorkers(), (long) integerArguments.get(1));
        assertEquals(position1.getSalary().getValue(), (long) integerArguments.get(2));
        assertEquals(position2.getVacancies(), (long) integerArguments.get(3));
        assertEquals(position2.getActiveWorkers(), (long) integerArguments.get(4));
        assertEquals(position2.getSalary().getValue(), (long) integerArguments.get(5));
        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).clearParameters();
        verify(preparedStatement).executeBatch();
        verify(connectTemporary).commit();
    }

    @After
    public void destroy() throws SQLException {
        connectTemporary.close();
    }
}