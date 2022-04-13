package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private static void createMonHoc() {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Tạo môn học");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel maMHLabel = new JLabel("Mã môn học");
        jFrame.add(maMHLabel);
        maMHLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField maMHTxtField = new JTextField();
        jFrame.add(maMHTxtField);

        JLabel tenMHLabel = new JLabel("Tên môn học");
        jFrame.add(tenMHLabel);
        tenMHLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField tenMHTxtField = new JTextField();
        jFrame.add(tenMHTxtField);

        JPanel jPanel = new JPanel();
        JButton confirmBtn = new JButton("Xác nhận");
        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement stm = connection.prepareStatement("INSERT INTO mon_hoc VALUES (?,?)");
                    stm.setString(1, maMHTxtField.getText());
                    stm.setString(2, tenMHTxtField.getText());
                    try {
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(jFrame, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(jFrame, "Failure!", "Error", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                } catch (SQLException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        });
        JButton cancelBtn = new JButton("Huỷ bỏ");
        cancelBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }

        });
        jPanel.add(confirmBtn);
        jPanel.add(cancelBtn);
        jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jFrame.add(jPanel);

        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        connection = getDBConnection();
        createMonHoc();
    }
}
