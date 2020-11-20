package springcore.utilityconnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;

import static springcore.constants.LogMessages.*;

@Component
public class ConnectTemporary implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
    public static final String LOGIN = "root";
    public static final String PASSWORD = "root";

    private final Connection connection;

    public ConnectTemporary() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error(DRIVER_ERROR_MESSAGE, e);
        }
        Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);

        connection.setAutoCommit(false);

        this.connection = connection;
    }

    public void truncateTables(String... tableNames) throws SQLException {
        Statement statement = getStatement();

        for (String tableName : tableNames) {
            statement.addBatch(tableName);
        }

        statement.executeBatch();

        commit();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection != null ? connection.prepareStatement(sql) : null;
    }

    public Statement getStatement() throws SQLException {
        return connection != null ? connection.createStatement() : null;
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
