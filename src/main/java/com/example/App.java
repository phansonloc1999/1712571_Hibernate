package com.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

import Utils.*;

/**
 * Hello world!
 *
 */
public class App {
    public final static String SV_DB_USERNAME = "sinhvien", SV_DB_PASSWORD = "sinhvien";
    public final static String GV_DB_USERNAME = "giaovu", GV_DB_PASSWORD = "giaovu";

    public static void main(String[] args) {
        final JFrame jFrame = new JFrame();
        JLabel usernameJLabel = new JLabel("Username");
        jFrame.add(usernameJLabel);
        final JTextField usernameTxtField = new JTextField();
        usernameTxtField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(usernameTxtField);
        JLabel passwordLabel = new JLabel("Password");
        jFrame.add(passwordLabel);
        final JPasswordField passwordField = new JPasswordField();
        passwordField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(passwordField);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel buttonsPanel = new JPanel();
        JButton confirmBtn = new JButton("Đăng nhập");
        confirmBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTxtField.getText();
                if (username.equals("giaovu")) {
                    String password = String.valueOf(passwordField.getPassword());
                    if (password.equals("giaovu")) {
                        GiaoVu gv = new GiaoVu();
                        gv.setConnection(DatabaseUtils.getDBConnection(GV_DB_USERNAME, GV_DB_PASSWORD));
                        gv.showMainMenu();
                        jFrame.dispose();
                    }
                } else {
                    try {
                        SinhVien sv = new SinhVien();
                        sv.setConnection(DatabaseUtils.getDBConnection(SV_DB_USERNAME, SV_DB_PASSWORD));
                        PreparedStatement statement = sv.getConnection().prepareStatement(
                                "SELECT password FROM sinh_vien WHERE mssv = " + usernameTxtField.getText(),
                                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = statement.executeQuery();
                        if (!rs.next())
                            JOptionPane.showMessageDialog(jFrame, "Username doesnt exist!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        rs.beforeFirst();
                        while (rs.next()) {
                            String hashedPassword = rs.getString(1);
                            if (hashedPassword
                                    .equals(EncryptionUtils
                                            .sha1FromString(String.valueOf(passwordField.getPassword())))) {
                                sv.setMssv(Integer.parseInt(usernameTxtField.getText()));
                                statement = sv.getConnection().prepareStatement(
                                        "SELECT forceChangePass FROM sinh_vien WHERE mssv = " + username);
                                ResultSet rs1 = statement.executeQuery();
                                rs1.next();
                                if (rs1.getInt(1) == 1) {
                                    sv.changePassFirstLogin();
                                } else
                                    sv.showMainMenu();
                                jFrame.dispose();
                                return;
                            }
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(jFrame, "Wrong username, password!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(confirmBtn);
        JButton cancelBtn = new JButton("Thoát");
        cancelBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }

        });
        buttonsPanel.add(cancelBtn);
        buttonsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        buttonsPanel.setLayout(new FlowLayout());
        jFrame.add(buttonsPanel);

        jFrame.setTitle("Sinh viên đăng nhập");
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
