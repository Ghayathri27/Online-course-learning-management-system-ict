package coursemnt;


import java.sql.Connection;

public class Testconnection {

    public static void main(String[] args) {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Connection successful!");

            con.close();

        } catch (Exception e) {

            System.out.println("Connection failed!");
            System.out.println(e.getMessage());

        }
    }
}