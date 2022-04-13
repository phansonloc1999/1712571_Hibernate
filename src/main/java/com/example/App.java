package com.example;

import javax.swing.JFrame;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App {
    private static Connection getDBConnection() {
        Connection connection = null;
        String connectionUrl = "jdbc:mysql://192.168.1.7:3306/DiemDanh";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(connectionUrl, "", "");
            if (connection != null) {
                System.out.println("Connected to DB!");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        // JFrame jframe = new JFrame();
        // jframe.pack();
        // jframe.setVisible(true);

        getDBConnection();
    }
}
