package springcore.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;

import static springcore.constants.LogMessages.*;

/**
 * Class for creating an instance of temporary connection.
 */
@Service
public class ConnectTemporary implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
    public static final String LOGIN = "root";
    public static final String PASSWORD = "root";

    private final Connection connection;

    /**
     * Instantiates a new Connect temporary.
     *
     * @throws SQLException the sql exception if class for driver is not found
     */
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

    /**
     * Truncate tables.
     *
     * @param tableNames the table names which will be truncated
     * @throws SQLException if there are problems with connection to database
     */
    public void truncateTables(String... tableNames) throws SQLException {
        Statement statement = getStatement();

        for (String tableName : tableNames) {
            statement.addBatch(tableName);
        }

        statement.executeBatch();

        commit();
    }

    /**
     * Gets prepared statement.
     *
     * @param sql query to database
     * @return the prepared statement
     * @throws SQLException if there are problems with connection to database
     */
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection != null ? connection.prepareStatement(sql) : null;
    }

    /**
     * Gets statement.
     *
     * @return the statement
     * @throws SQLException if there are problems with connection to database
     */
    public Statement getStatement() throws SQLException {
        return connection != null ? connection.createStatement() : null;
    }

    /**
     * Commit.
     *
     * @throws SQLException if there are problems with connection to database
     */
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
