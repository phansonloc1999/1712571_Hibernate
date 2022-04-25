package com.example;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import Utils.CheckBoxList;
import Utils.EncryptionUtils;

import java.awt.*;
import java.awt.event.*;

public class SinhVien implements java.io.Serializable {
    public Connection connection = null;

    private int mssv;

    public int getMssv() {
        return mssv;
    }

    public void setMssv(int mssv) {
        this.mssv = mssv;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public SinhVien() {
        connection = null;
        mssv = 0;
    }

    public SinhVien(Connection connection, int mssv) {
        this.connection = connection;
        this.mssv = mssv;
    }

    public void showDiemDanh(final String maMH) {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Điểm danh môn học " + maMH);

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT co_mat FROM diem_danh_sv WHERE mssv = " + mssv + " AND ma_mh = '" + maMH + "'");

            final CheckBoxList checkBoxList = new CheckBoxList();
            checkBoxList.setDisableCheckBoxes(true);
            for (int j = 0; j < 15; j++) {
                rs.next();
                int coMat = rs.getInt(1);
                JCheckBox jCheckBox = new JCheckBox("Tuần " + (j + 1));
                jCheckBox.setEnabled(false);
                jCheckBox.setSelected(coMat == 1 ? true : false);
                checkBoxList.addCheckbox(jCheckBox);
            }
            checkBoxList.setVisibleRowCount(1);
            checkBoxList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            jFrame.add(checkBoxList);

            JPanel buttonsPanel = new JPanel();
            JButton exitBtn = new JButton("Thoát");
            exitBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jFrame.dispose();
                }

            });
            buttonsPanel.add(exitBtn);
            buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jFrame.add(buttonsPanel);

            jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
            jFrame.pack();
            jFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean compareDateToWeekDay(int weekDay, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Special case, Sunday in database is 8 while in calendar is 1
        if (weekDay == 8
                && calendar.get(Calendar.DAY_OF_WEEK) == 1)
            return true;
        // Other cases
        return weekDay == calendar.get(Calendar.DAY_OF_WEEK) ? true : false;
    }

    public void showMainMenu() {
        JFrame jFrame = new JFrame();

        JButton diemDanhBtn = new JButton("Điểm danh");
        diemDanhBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame jFrame = new JFrame();
                JLabel ngayLabel = new JLabel("Ngày (YYYY-MM-DD)");
                jFrame.add(ngayLabel);
                final JTextField ngayTxtField = new JTextField();
                jFrame.add(ngayTxtField);
                JLabel gioLabel = new JLabel("Giờ (HH:MM:SS)");
                jFrame.add(gioLabel);
                final JTextField gioTxtField = new JTextField();
                jFrame.add(gioTxtField);

                JPanel buttonsPanel = new JPanel();
                JButton confirmBtn = new JButton("Xác nhận");
                confirmBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Statement statement;
                        try {
                            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            ResultSet rs = statement
                                    .executeQuery("SELECT ma_mh FROM danh_sach_sv_mh WHERE mssv = " + mssv);
                            rs.last();
                            int numOfMaMh = rs.getRow();
                            rs.beforeFirst();
                            // Build a string containing all ma mon hoc. Ex: " '1712571', '1712572',
                            // '1712573' "
                            String allMaMh = "";
                            for (int i = 0; i < numOfMaMh; i++) {
                                rs.next();
                                allMaMh += "'" + rs.getString(1) + "'";
                                // Append ',' after every ma mon hoc, except the last one
                                if (i < numOfMaMh - 1)
                                    allMaMh += ",";
                            }

                            rs = statement
                                    .executeQuery(
                                            "SELECT ngay_bd, ngay_kt, thu, gio_bd, gio_kt, ma_mh FROM thoi_khoa_bieu WHERE ma_mh IN ("
                                                    + allMaMh + ")");

                            while (rs.next()) {

                                java.util.Date diemDanhDate = null;
                                java.util.Date diemDanhTime = null;
                                try {
                                    diemDanhDate = new SimpleDateFormat("yyyy-MM-dd").parse(ngayTxtField.getText());
                                    diemDanhTime = new SimpleDateFormat("hh:mm:ss").parse(gioTxtField.getText());
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                java.sql.Date ngayBD = rs.getDate(1);
                                java.sql.Date ngayKT = rs.getDate(2);
                                int weekDay = rs.getInt(3);
                                java.sql.Time gioBd = rs.getTime(4);
                                java.sql.Time gioKt = rs.getTime(5);
                                if (compareDateToWeekDay(weekDay, diemDanhDate) && diemDanhDate.before(ngayKT)
                                        && diemDanhDate.after(ngayBD) && diemDanhTime.after(gioBd)
                                        && diemDanhTime.before(gioKt)) {
                                    PreparedStatement statement1 = connection
                                            .prepareStatement(
                                                    "UPDATE diem_danh_sv SET co_mat = 1 WHERE ma_mh = ? AND mssv = ? AND tuan = ?");
                                    String maMh = rs.getString(6);
                                    statement1.setString(1, maMh);
                                    statement1.setInt(2, mssv);
                                    long diffInMillies = Math.abs(diemDanhDate.getTime() - ngayBD.getTime());
                                    int diffInWeeks = (int) (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
                                            / 7);
                                    statement1.setInt(3, diffInWeeks + 1);
                                    statement1.executeUpdate();

                                    JOptionPane.showMessageDialog(jFrame, "Success!",
                                            "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                });
                buttonsPanel.add(confirmBtn);
                JButton cancelBtn = new JButton("Huỷ bỏ");
                cancelBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jFrame.dispose();
                    }
                });
                buttonsPanel.add(cancelBtn);
                buttonsPanel.setLayout(new FlowLayout());
                buttonsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                jFrame.add(buttonsPanel);

                jFrame.setTitle("Chọn thời điểm điểm danh");
                jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

                jFrame.pack();
                jFrame.setVisible(true);
            }

        });
        jFrame.add(diemDanhBtn);

        JButton xemDiemDanhBtn = new JButton("Xem điểm danh");
        xemDiemDanhBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame jFrame = new JFrame();

                Statement statement;
                try {
                    statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM danh_sach_sv_mh WHERE mssv = " + mssv);
                    int numOfMonHoc = 0;
                    while (rs.next()) {
                        numOfMonHoc = rs.getInt(1);
                    }
                    String[] maMonHocArr = new String[numOfMonHoc];
                    rs = statement.executeQuery("SELECT ma_mh FROM danh_sach_sv_mh WHERE mssv = " + mssv);
                    int i = 0;
                    while (rs.next()) {
                        maMonHocArr[i] = rs.getString(1);
                        i++;
                    }

                    String maMH = (String) JOptionPane.showInputDialog(jFrame,
                            "Chọn mã môn học muốn xem điểm danh",
                            "Chọn mã môn học",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            maMonHocArr,
                            maMonHocArr[0]);

                    // If a string was returned, say so.
                    if ((maMH != null) && (maMH.length() > 0)) {
                        showDiemDanh(maMH);
                    }
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }
        });
        jFrame.add(xemDiemDanhBtn);

        JButton changePassBtn = new JButton("Đổi mật khẩu");
        changePassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame jFrame = new JFrame();
                JLabel newPassLabel = new JLabel("Nhập mật khẩu mới");
                jFrame.add(newPassLabel);
                final JPasswordField newPassField = new JPasswordField();
                jFrame.add(newPassField);
                newPassField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                JLabel newPassRepeatLabel = new JLabel("Nhập lại mật khẩu mới");
                jFrame.add(newPassRepeatLabel);
                final JPasswordField newPassRepeatField = new JPasswordField();
                newPassRepeatField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                jFrame.add(newPassRepeatField);

                JPanel buttonsPanel = new JPanel();
                JButton confirmBtn = new JButton("Xác nhận");
                confirmBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Statement statement = connection.createStatement();
                            String password = String.valueOf(newPassField.getPassword());
                            if (password.equals("")) {
                                JOptionPane.showMessageDialog(jFrame, "Password cant be empty!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            String repeatPassword = String.valueOf(newPassRepeatField.getPassword());
                            if (!password.equals(repeatPassword)) {
                                JOptionPane.showMessageDialog(jFrame, "Password and repeat password don't match!",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            String hashedPass = EncryptionUtils.sha1FromString(password);
                            statement
                                    .executeUpdate(
                                            "UPDATE sinh_vien SET password = '" + hashedPass + "' WHERE mssv = "
                                                    + mssv);
                            JOptionPane.showMessageDialog(jFrame, "Successfully changed password!", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            showMainMenu();
                            jFrame.dispose();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                buttonsPanel.add(confirmBtn);
                JButton cancelBtn = new JButton("Huỷ bỏ!");
                cancelBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jFrame.dispose();
                    }
                });
                buttonsPanel.add(cancelBtn);
                buttonsPanel.setLayout(new FlowLayout());
                buttonsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                jFrame.add(buttonsPanel);

                jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
                jFrame.setTitle("Đổi mật khẩu");
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
        jFrame.add(changePassBtn);

        jFrame.setTitle("Menu chính");
        jFrame.setLayout(new FlowLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void changePassFirstLogin() {
        final JFrame jFrame = new JFrame();
        JLabel newPassLabel = new JLabel("Nhập mật khẩu mới");
        jFrame.add(newPassLabel);
        final JPasswordField newPassField = new JPasswordField();
        jFrame.add(newPassField);
        newPassField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        JLabel newPassRepeatLabel = new JLabel("Nhập lại mật khẩu mới");
        jFrame.add(newPassRepeatLabel);
        final JPasswordField newPassRepeatField = new JPasswordField();
        newPassRepeatField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(newPassRepeatField);

        JPanel buttonsPanel = new JPanel();
        JButton confirmBtn = new JButton("Xác nhận");
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    String password = String.valueOf(newPassField.getPassword());
                    if (password.equals("")) {
                        JOptionPane.showMessageDialog(jFrame, "Password cant be empty!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String repeatPassword = String.valueOf(newPassRepeatField.getPassword());
                    if (!password.equals(repeatPassword)) {
                        JOptionPane.showMessageDialog(jFrame, "Password and repeat password don't match!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String hashedPass = EncryptionUtils.sha1FromString(password);
                    statement
                            .executeUpdate(
                                    "UPDATE sinh_vien SET password = '" + hashedPass + "' WHERE mssv = " + mssv);
                    JOptionPane.showMessageDialog(jFrame, "Successfully changed password!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    statement.executeUpdate("UPDATE sinh_vien SET forceChangePass = 0 WHERE mssv = " + mssv);
                    showMainMenu();
                    jFrame.dispose();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonsPanel.add(confirmBtn);
        JButton cancelBtn = new JButton("Huỷ bỏ!");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        buttonsPanel.add(cancelBtn);
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(buttonsPanel);

        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setTitle("Đổi mật khẩu");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
