package springcore.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.*;

import static springcore.constants.LogMessages.*;

/**
 * Class for creating an instance of temporary connection.
 */
@Component
public class ConnectTemporary implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    public final String driver;
    public final String url;
    public final String login;
    public final String password;

    private Connection connection;

    /**
     * Instantiates a new Connect temporary.
     */
    @Autowired
    public ConnectTemporary(@Value("${DRIVER}") String driver, @Value("${URL}") String url,
                            @Value("${LOGIN}") String login, @Value("${PASSWORD}") String password) {
        this.driver = driver;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    /**
     * Truncate tables.
     *
     * @param tableNames the table names which will be truncated
     * @throws SQLException if there are problems with connection to database
     */
    public void truncateTables(String... tableNames) throws SQLException {
        if (this.connection == null) {
            openConnection();
        }
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
        if (this.connection == null) {
            openConnection();
        }
        return connection.prepareStatement(sql);
    }

    /**
     * Gets statement.
     *
     * @return the statement
     * @throws SQLException if there are problems with connection to database
     */
    public Statement getStatement() throws SQLException {
        if (this.connection == null) {
            openConnection();
        }
        return connection.createStatement();
    }

    /**
     * Commit.
     *
     * @throws SQLException if there are problems with connection to database
     */
    public void commit() throws SQLException {
        if (this.connection == null) {
            openConnection();
        }
        connection.commit();
    }

    private void openConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error(DRIVER_ERROR_MESSAGE, e);
        }
        Connection connection = DriverManager.getConnection(url, login, password);

        connection.setAutoCommit(false);

        this.connection = connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
