package com.example;

import java.sql.*;

public abstract class DatabaseUtils {
    public static Connection getDBConnection(String username, String password) {
        Connection conn = null;
        String connectionUrl = "jdbc:mysql://192.168.1.7:3306/DiemDanh";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl, username, password);
            if (conn != null) {
                System.out.println("Connected to DB!");
            } else
                System.out.println("Failed to connect to DB!");
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
        return conn;
    }
}
