package springcore.utilityconnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectTemporary implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
    public static final String LOGIN = "root";
    public static final String PASSWORD = "root";

    private final Connection connection;
    private static ConnectTemporary instance;

    private ConnectTemporary() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Problem with database driver", e);
        }
        Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        connection.setAutoCommit(false);
        this.connection = connection;
    }

    public static ConnectTemporary getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectTemporary();
        }
        return instance;
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        if (connection != null) return connection.prepareStatement(sql);
        return null;
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
