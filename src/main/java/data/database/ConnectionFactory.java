package data.database;

import data.database.exceptions.DatabaseConnectionException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private ConnectionFactory() {
    }

    private static final String PROPERTIES_PATH = "D:\\java\\polyclinic\\src\\main\\resources\\database.properties";

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new DatabaseConnectionException();
        }
        return properties;
    }

    public static Connection getConnection() {
        Properties properties = loadProperties();
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        try {
            Class.forName(properties.getProperty("driver"));
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectionException();
        }
    }
}
