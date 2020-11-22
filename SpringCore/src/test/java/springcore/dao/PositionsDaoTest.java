//package springcore.dao;
//
//import org.junit.*;
//import org.mockito.*;
//import springcore.mappers.Mapper;
//import springcore.position.Position;
//import springcore.services.connectionservices.ConnectTemporary;
//
//import java.sql.*;
//import java.util.*;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static springcore.constants.SQLQueries.GET_EXACT_POSITIONS_QUERY;
//
//public class PositionsDaoTest {
//
//    @Mock
//    Mapper<ResultSet, List<Position>,
//            List<Position>, PreparedStatement> mapper;
//    @Mock
//    ConnectTemporary connectTemporary;
//    @Mock
//    ResultSet resultSet;
//    @Mock
//    PreparedStatement preparedStatement;
//    @Captor
//    ArgumentCaptor<List<Position>> listArgumentCaptor;
//    @Captor
//    ArgumentCaptor<ResultSet> resultSetArgumentCaptor;
//    @Captor
//    ArgumentCaptor<String> stringArgumentCaptor;
//    List<Position> positions;
//    PositionsDao positionsDao;
//    Position position1;
//    Position position2;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//        position1 = new Position("Actor");
//        position2 = new Position("Ment");
//
//        positions = new ArrayList<>(Arrays.asList(position1, position2));
//        positionsDao = new PositionsImplDb(connectTemporary, mapper);
//    }
//
//    @Test
//    public void addPositions() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//
//        positionsDao.addPositions(positions);
//
//        verify(mapper).add(listArgumentCaptor.capture(), eq(preparedStatement));
//        verify(connectTemporary).commit();
//
//        assertEquals(positions, listArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void getAllPositions() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.getResultSet()).thenReturn(resultSet);
//
//        positionsDao.getAllPositions();
//
//        verify(preparedStatement).execute();
//        verify(preparedStatement).getResultSet();
//        verify(mapper).map(resultSetArgumentCaptor.capture());
//
//        assertEquals(resultSet, resultSetArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void getPositions() throws SQLException {
//        String argument = "Some string";
//        String value = "Some value";
//
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.getResultSet()).thenReturn(resultSet);
//
//        positionsDao.getPositions(argument, value);
//
//        verify(connectTemporary).getPreparedStatement(stringArgumentCaptor.capture());
//        verify(preparedStatement).setObject(eq(1), stringArgumentCaptor.capture());
//        verify(preparedStatement).execute();
//        verify(mapper).map(resultSetArgumentCaptor.capture());
//
//        assertEquals(resultSet, resultSetArgumentCaptor.getValue());
//
//        List<String> values = stringArgumentCaptor.getAllValues();
//
//        assertEquals(String.format(GET_EXACT_POSITIONS_QUERY, argument), values.get(0));
//        assertEquals(value, values.get(1));
//    }
//
//    @Test
//    public void updatePositions() throws SQLException {
//        when(connectTemporary.getPreparedStatement(anyString())).thenReturn(preparedStatement);
//
//        positionsDao.updatePositions(positions);
//
//        verify(mapper).update(listArgumentCaptor.capture(), eq(preparedStatement));
//        verify(connectTemporary).commit();
//
//        assertEquals(positions, listArgumentCaptor.getValue());
//    }
//}