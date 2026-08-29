package coursemnt;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/code_conquer?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    private static final String PASSWORD = "#Ghayathri27";

    public static Connection getConnection() throws SQLException {

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC Driver not found. Add mysql-connector-j.jar to WEB-INF/lib.",
                    e
            );
        }

        Connection con = DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );

        System.out.println("MySQL connection successful!");

        return con;
    }
}