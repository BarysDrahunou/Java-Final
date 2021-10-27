package springcore.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springcore.currency.Usd;
import springcore.position.Position;

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

public class PositionMapperTest {

    @Mock
    ResultSet resultSet;
    @Mock
    PreparedStatement preparedStatement;
    Mapper<ResultSet, Position,
            Position, PreparedStatement> positionMapper;
    Position position1;
    Position position2;
    List<Position> positions;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        positionMapper = new PositionMapper();

        position1 = new Position("Actor");
        position2 = new Position("Ment");

        position1.setVacancies(5);
        position1.setActiveWorkers(3);
        position1.setSalary(new Usd(100));

        position2.setVacancies(10);
        position2.setActiveWorkers(8);
        position2.setSalary(new Usd(666));

        positions = Arrays.asList(position1, position2);
    }

    @Test
    public void map() throws SQLException {

        when(resultSet.getString(anyString())).thenReturn("Actor", "Ment");
        when(resultSet.getInt(anyString())).thenReturn(5, 3, 100, 10, 8, 666);

        List<Position> positions = new ArrayList<>();

        positions.add(positionMapper.map(resultSet));
        positions.add(positionMapper.map(resultSet));

        verify(resultSet, times(2)).getString(anyString());
        verify(resultSet, times(6)).getInt(anyString());

        Position position1InList = positions.get(0);
        Position position2InList = positions.get(1);

        assertEquals(position1, position1InList);
        assertEquals(position1.getVacancies(), position1InList.getVacancies());
        assertEquals(position1.getActiveWorkers(), position1InList.getActiveWorkers());
        assertEquals(position1.getSalary(), position1InList.getSalary());

        assertEquals(position2, position2InList);
        assertEquals(position2.getVacancies(), position2InList.getVacancies());
        assertEquals(position2.getActiveWorkers(), position2InList.getActiveWorkers());
        assertEquals(position2.getSalary(), position2InList.getSalary());
    }

    @Test
    public void add() throws SQLException {
        for (Position position : positions) {
            positionMapper.add(position, preparedStatement);
        }

        verify(preparedStatement).setString(eq(1), eq(position1.getPositionName()));
        verify(preparedStatement).setString(eq(1), eq(position2.getPositionName()));

        verify(preparedStatement).setInt(eq(2), eq(position1.getVacancies()));
        verify(preparedStatement).setInt(eq(3), eq(position1.getActiveWorkers()));
        verify(preparedStatement).setInt(eq(4), eq(position1.getSalary().getValue()));

        verify(preparedStatement).setInt(eq(2), eq(position2.getVacancies()));
        verify(preparedStatement).setInt(eq(3), eq(position2.getActiveWorkers()));
        verify(preparedStatement).setInt(eq(4), eq(position2.getSalary().getValue()));
    }

    @Test
    public void update() throws SQLException {
        for (Position position : positions) {
            positionMapper.update(position, preparedStatement);
        }

        verify(preparedStatement).setString(eq(4), eq(position1.getPositionName()));
        verify(preparedStatement).setInt(eq(1), eq(position1.getVacancies()));
        verify(preparedStatement).setInt(eq(2), eq(position1.getActiveWorkers()));
        verify(preparedStatement).setInt(eq(3), eq(position1.getSalary().getValue()));

        verify(preparedStatement).setString(eq(4), eq(position2.getPositionName()));
        verify(preparedStatement).setInt(eq(1), eq(position2.getVacancies()));
        verify(preparedStatement).setInt(eq(2), eq(position2.getActiveWorkers()));
        verify(preparedStatement).setInt(eq(3), eq(position2.getSalary().getValue()));
    }
}