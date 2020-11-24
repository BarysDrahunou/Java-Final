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

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     */
    @Override
    void close();
}
