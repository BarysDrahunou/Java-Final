package springcore.utilityconnection;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class Connect {

    private final Connection connection;

    @Autowired
    public Connect(@Value("${sql.login}") String login, @Value("${sql.password}") String password,
                   @Value("${sql.driver}") String driver, @Value("${sql.URL}") String url)
            throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, login, password);
        connection.setAutoCommit(false);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
