package com.example;

import java.sql.*;

public abstract class DatabaseUtils {
    public static Connection getDBConnection() {
        Connection conn = null;
        String connectionUrl = "jdbc:mysql://192.168.1.7:3306/DiemDanh";
        String user = "giaovu";
        String password = "giaovu";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl, user, password);
            if (conn != null) {
                System.out.println("Connected to DB!");
            } else
                System.out.println("Failed to connect to DB!");
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
        return conn;
    }

    public static void addSinhVien(Connection conn, int mssv, String tenSv, String username, String password) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO sinh_vien VALUES (?,?,?,?)");
            statement.setInt(1, mssv);
            statement.setString(2, tenSv);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
