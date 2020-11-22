package springcore.services.connectionservices;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Class for creating an instance of temporary connection.
 */
@Component
public class ConnectTemporaryImpl implements ConnectTemporary {

    public String driver;
    public String url;
    public String login;
    public String password;

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
        } catch (ClassNotFoundException | SQLException e) {
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
            throw new RuntimeException(e);
        }
    }
}
