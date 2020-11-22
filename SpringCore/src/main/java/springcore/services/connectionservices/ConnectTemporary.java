package springcore.services.connectionservices;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * The interface Connect temporary.
 */
public interface ConnectTemporary extends AutoCloseable {

    /**
     * Open connection.
     */
    void openConnection();

    /**
     * Truncate tables.
     *
     * @param tableNames names of tables to truncate
     */
    void truncateTables(String... tableNames);

    /**
     * Gets prepared statement.
     *
     * @param sql the sql
     * @return the prepared statement
     */
    PreparedStatement getPreparedStatement(String sql);

    /**
     * Gets statement.
     *
     * @return the statement
     */
    Statement getStatement();

    /**
     * Commit.
     */
    void commit();

    @Override
    void close();
}
