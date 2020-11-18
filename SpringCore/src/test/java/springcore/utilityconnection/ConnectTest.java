package springcore.utilityconnection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ConnectTest {

    @Mock
    Connection connection;
    Connect connect;

    @Before
    public void init() throws NoSuchFieldException, SQLException, ClassNotFoundException {
        MockitoAnnotations.initMocks(this);
            connect = new Connect("root", "root",
                    "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306?serverTimezone=UTC");
            Field field = Connect.class.getDeclaredField("connection");
            field.setAccessible(true);
            ReflectionUtils.setField(field, connect, connection);
    }

    @Test
    public void getConnection() {
        assertEquals(connection, connect.getConnection());
    }
}