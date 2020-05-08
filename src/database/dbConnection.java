package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    public static Connection garitsConnection(){
       Connection conn = null; // creates the connection variable
       String url = "jdbc:sqlite:src/database/GARITSdb.db"; //URL to where the database is stored

       try {
           conn = DriverManager.getConnection(url); //Make connection to where the database is stored
       }catch (SQLException e){

        e.printStackTrace();
       }

       return conn; // returns connection
    }
}
