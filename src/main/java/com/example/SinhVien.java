package com.example;

import java.sql.*;
import javax.swing.*;
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
            for (int j = 0; j < 15; j++) {
                rs.next();
                int coMat = rs.getInt(1);
                JCheckBox jCheckBox = new JCheckBox("Tuần " + (j + 1));
                jCheckBox.setSelected(coMat == 1 ? true : false);
                checkBoxList.addCheckbox(jCheckBox);
            }
            checkBoxList.setVisibleRowCount(1);
            checkBoxList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            jFrame.add(checkBoxList);

            JPanel buttonsPanel = new JPanel();
            JButton confirmBtn = new JButton("Xác nhận");
            confirmBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ListModel checkBoxListModel = checkBoxList.getModel();

                    PreparedStatement stm;
                    try {
                        stm = connection.prepareStatement(
                                "UPDATE diem_danh_sv SET co_mat = ? WHERE ma_mh = ? AND mssv = ? AND tuan = ?");

                        for (int j = 0; j < 15; j++) {
                            JCheckBox jCheckBox = (JCheckBox) checkBoxListModel.getElementAt(j);

                            stm.setInt(1, jCheckBox.isSelected() == true ? 1 : 0);
                            stm.setString(2, maMH);
                            stm.setInt(3, mssv);
                            stm.setInt(4, (j + 1));

                            stm.executeUpdate();
                        }
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(jFrame, "Failure!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(jFrame, "Success!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
            JButton cancelBtn = new JButton("Huỷ bỏ");
            cancelBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jFrame.dispose();
                }

            });
            buttonsPanel.add(confirmBtn);
            buttonsPanel.add(cancelBtn);
            buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jFrame.add(buttonsPanel);

            jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.pack();
            jFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
