package data.database;

import data.database.exceptions.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private ConnectionFactory() {
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/polyclinic";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "QWEasd123";

    public static Connection getConnection() {
        try {

            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(URL, USERNAME, PASSWORD);


            //DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/polyclinic");

            //return dataSource.getConnection();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DatabaseConnectionException();
        }
    }
}
