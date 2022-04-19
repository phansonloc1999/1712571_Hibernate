package com.example;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SinhVien {
    public Connection connection = null;

    public void showDiemDanh(final String maMH) {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Điểm danh");

        try {
            ResultSet rs = null;
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(DISTINCT mssv) FROM diem_danh_sv WHERE ma_mh = '" + maMH + "'");
            rs.next();
            final int numOfSvInMonHoc = rs.getInt(1);

            statement = connection.createStatement();
            rs = statement
                    .executeQuery(
                            "SELECT DISTINCT mssv FROM diem_danh_sv WHERE ma_mh = '" + maMH + "'");

            final JPanel[] diemDanhSvPanels = new JPanel[numOfSvInMonHoc];
            for (int i = 0; i < numOfSvInMonHoc; i++) {
                diemDanhSvPanels[i] = new JPanel();
                int mssv = 0;
                if (!rs.next())
                    break;

                mssv = rs.getInt(1);
                JLabel mssvLabel = new JLabel(Integer.toString(mssv));
                diemDanhSvPanels[i].add(mssvLabel);

                statement = connection.createStatement();
                ResultSet rs1 = statement.executeQuery(
                        "SELECT co_mat FROM diem_danh_sv WHERE mssv = '" + mssv + "' AND ma_mh = '" + maMH + "'");

                final CheckBoxList checkBoxList = new CheckBoxList();
                for (int j = 0; j < 15; j++) {
                    rs1.next();
                    int coMat = rs1.getInt(1);
                    JCheckBox jCheckBox = new JCheckBox("Tuần " + (j + 1));
                    jCheckBox.setSelected(coMat == 1 ? true : false);
                    checkBoxList.addCheckbox(jCheckBox);
                }
                checkBoxList.setVisibleRowCount(1);
                checkBoxList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                diemDanhSvPanels[i].add(checkBoxList);
                jFrame.add(diemDanhSvPanels[i]);
            }

            JPanel buttonsPanel = new JPanel();
            JButton confirmBtn = new JButton("Xác nhận");
            confirmBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    for (int i = 0; i < numOfSvInMonHoc; i++) {
                        JLabel mssvLabel = (JLabel) diemDanhSvPanels[i].getComponent(0);
                        int mssv = Integer.parseInt(mssvLabel.getText());
                        CheckBoxList checkBoxList = (CheckBoxList) diemDanhSvPanels[i].getComponent(1);
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
