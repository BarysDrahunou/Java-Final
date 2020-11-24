package springcore.services.connectionservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.*;

import static springcore.constants.SQLExceptionMessages.*;

/**
 * Class for creating an instance of temporary connection.
 */
@Component
public class ConnectTemporaryImpl implements ConnectTemporary {

    private static final Logger LOGGER = LogManager.getLogger();
    private final String driver;
    private final String url;
    private final String login;
    private final String password;

    private Connection connection;

    /**
     * Instantiates a new Connect temporary.
     */
    @Autowired
    public ConnectTemporaryImpl(@Value("${DRIVER}") String driver, @Value("${URL}") String url,
                                @Value("${LOGIN}") String login, @Value("${PASSWORD}") String password) {
        this.driver = driver;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    /**
     * Open connection.
     */
    @Override
    public void openConnection() {
        try {
            Class.forName(driver);

            Connection connection = DriverManager.getConnection(url, login, password);

            connection.setAutoCommit(false);

            this.connection = connection;
        } catch (ClassNotFoundException e) {
            LOGGER.error(CLASS_NOT_FOUND_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        } catch (SQLException e) {
            LOGGER.error(OPEN_CONNECTION_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }

    /**
     * Truncate tables.
     *
     * @param tableNames the table names which will be truncated
     */
    @Override
    public void truncateTables(String... tableNames) {
        try {
            Statement statement = getStatement();

            for (String tableName : tableNames) {
                statement.addBatch(tableName);
            }

            statement.executeBatch();

            commit();
        } catch (SQLException e) {
            LOGGER.error(TRUNCATE_TABLES_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }

    /**
     * Gets prepared statement.
     *
     * @param sql query to database
     * @return the prepared statement
     */
    @Override
    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            LOGGER.error(PREPARED_STATEMENT_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }

    /**
     * Gets statement.
     *
     * @return the statement
     */
    @Override
    public Statement getStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            LOGGER.error(STATEMENT_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }

    /**
     * Commit.
     */
    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(COMMIT_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(CLOSE_DATABASE_EXCEPTION_MESSAGE, e);

            throw new RuntimeException(e);
        }
    }
}
