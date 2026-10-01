package coursemnt;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // System Environment Variables (Railway-la auto-fetch aagum)
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String dbName = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            // Railway-la env variable illana default hardcoded values use pannum
            if (host == null) {
                // Testing locally or direct Railway Public Domain
                String url = "jdbc:mysql://YOUR_RAILWAY_MYSQL_PUBLIC_HOST:PORT/YOUR_DB_NAME?useSSL=false&allowPublicKeyRetrieval=true";
                con = DriverManager.getConnection(url, "root", "YOUR_PASSWORD");
            } else {
                // Inside Railway Network
                String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true";
                con = DriverManager.getConnection(url, user, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
