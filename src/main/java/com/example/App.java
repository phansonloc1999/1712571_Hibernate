package com.example;

import javax.swing.JFrame;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App {
    private static Connection connection = null;

    private static Connection getDBConnection() {
        Connection conn = null;
        String connectionUrl = "jdbc:mysql://192.168.1.7:3306/DiemDanh";
        String user = "diemdanhuser";
        String password = "24061999";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl, user, password);
            if (conn != null) {
                System.out.println("Connected to DB!");
            } else
                System.out.println("Failed to connect to DB!");
        } catch (ClassNotFoundException | SQLException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // JFrame jframe = new JFrame();
        // jframe.pack();
        // jframe.setVisible(true);

        connection = getDBConnection();
    }
}
