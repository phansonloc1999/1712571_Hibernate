package com.example;

import javax.swing.*;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.sql.*;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    private static Connection connection = null;

    private static final Dimension PREFERED_FRAME_SIZE = new Dimension(400, 300);

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

                        jFrame.dispose();
                        addSVToMonHoc(maMHTxtField.getText());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(jFrame, "Failure!", "Error", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                } catch (SQLException e2) {
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

        jFrame.setTitle("Tạo môn học");
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void createThoiKhoaBieu() {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Tạo thời khoá biểu");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel maMHLabel = new JLabel("Mã môn học");
        jFrame.add(maMHLabel);
        maMHLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField maMHTextField = new JTextField();
        jFrame.add(maMHTextField);

        JLabel ngayBdLabel = new JLabel("Ngày bắt đầu");
        jFrame.add(ngayBdLabel);
        ngayBdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField ngayBdTxtField = new JTextField();
        jFrame.add(ngayBdTxtField);

        JLabel ngayKtLabel = new JLabel("Ngày kết thúc");
        jFrame.add(ngayKtLabel);
        ngayBdLabel.setAlignmentX(Component.TOP_ALIGNMENT);
        final JTextField ngayKtTxtField = new JTextField();
        jFrame.add(ngayKtTxtField);

        JLabel thuLabel = new JLabel("Thứ trong tuần");
        jFrame.add(thuLabel);
        thuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField thuTxtField = new JTextField();
        jFrame.add(thuTxtField);

        JLabel gioBdLabel = new JLabel("Giờ bắt đầu");
        jFrame.add(gioBdLabel);
        gioBdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField gioBdTxtField = new JTextField();
        jFrame.add(gioBdTxtField);

        JLabel gioKtLabel = new JLabel("Giờ kết thúc");
        jFrame.add(gioKtLabel);
        gioKtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField gioKtTxtField = new JTextField();
        jFrame.add(gioKtTxtField);

        JLabel phongHocLabel = new JLabel("Phòng học");
        jFrame.add(phongHocLabel);
        phongHocLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField phongHocTxtField = new JTextField();
        jFrame.add(phongHocTxtField);

        JPanel jPanel = new JPanel();
        JButton confirmBtn = new JButton("Xác nhận");
        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement stm = connection
                            .prepareStatement(
                                    "INSERT INTO thoi_khoa_bieu (ngay_bd, ngay_kt, thu, gio_bd, gio_kt, ten_phong_hoc, ma_mh) VALUES (?,?,?,?,?,?,?)");
                    stm.setString(1, ngayBdTxtField.getText());
                    stm.setString(2, ngayKtTxtField.getText());
                    stm.setString(3, thuTxtField.getText());
                    stm.setString(4, gioBdTxtField.getText());
                    stm.setString(5, gioKtTxtField.getText());
                    stm.setString(6, phongHocTxtField.getText());
                    stm.setString(7, maMHTextField.getText());
                    try {
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(jFrame, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(jFrame, "Failure!", "Error", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                } catch (SQLException e2) {
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
    }

    public static void addSVToMonHoc(final String maMH) {
        final JFrame selectMethodJFrame = new JFrame();
        JButton checkMssvBtn = new JButton("Check chọn mssv");
        checkMssvBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectMethodJFrame.dispose();

                final JFrame jFrame = new JFrame();

                String[] maMHStrings = null;
                try {
                    Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stm.executeQuery("SELECT ma_mh FROM mon_hoc");
                    rs.last();
                    int rowCount = rs.getRow();
                    maMHStrings = new String[rowCount];
                    int i = 0;
                    rs.beforeFirst();
                    while (rs.next()) {
                        maMHStrings[i] = rs.getString(1);
                        i++;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                final CheckBoxList checkBoxList = new CheckBoxList();
                try {
                    Statement stm = connection.createStatement();
                    ResultSet rs = stm.executeQuery("SELECT * FROM sinh_vien");

                    while (rs.next()) {
                        String mssv = String.valueOf(rs.getInt(1));
                        checkBoxList.addCheckbox(new JCheckBox(mssv));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                JPanel topPanel = new JPanel();
                JLabel checkBoxMssvLabel = new JLabel("Chọn MSSV");
                topPanel.add(checkBoxMssvLabel);
                checkBoxMssvLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                topPanel.add(checkBoxList);
                checkBoxList.setAlignmentX(Component.LEFT_ALIGNMENT);
                topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
                topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                jFrame.add(topPanel);

                JPanel buttonsPanel = new JPanel();
                JButton confirmBtn = new JButton("Xác nhận");
                confirmBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {

                        try {
                            PreparedStatement statement = connection
                                    .prepareStatement("INSERT INTO danh_sach_sv_mh VALUES (?,?)");
                            ListModel currentList = checkBoxList.getModel();
                            for (int i = 0; i < currentList.getSize(); i++) {
                                JCheckBox jCheckBox = (JCheckBox) currentList.getElementAt(i);
                                if (jCheckBox.isSelected()) {
                                    statement.setString(1, maMH);
                                    statement.setString(2, jCheckBox.getText());
                                    statement.executeUpdate();
                                }
                            }
                            jFrame.dispose();
                        } catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    }
                });
                JButton cancelBtn = new JButton("Huỷ bỏ");
                cancelBtn.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        jFrame.dispose();
                    }

                });
                confirmBtn.setAlignmentX(Component.BOTTOM_ALIGNMENT);
                cancelBtn.setAlignmentX(Component.BOTTOM_ALIGNMENT);
                buttonsPanel.add(Box.createRigidArea(new Dimension(0, 70)));
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.add(confirmBtn);
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.add(cancelBtn);
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
                jFrame.add(Box.createVerticalGlue());
                jFrame.add(buttonsPanel);

                jFrame.setTitle("Thêm sinh viên vào môn học");
                jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
        selectMethodJFrame.add(checkMssvBtn);
        JButton nhapMssvBtn = new JButton("Nhập mssv");
        nhapMssvBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                selectMethodJFrame.dispose();
                final JFrame jFrame = new JFrame();

                JPanel jPanel = new JPanel();
                JLabel addMssvLabel = new JLabel("Nhập các MSSV (từng dòng 1)");
                addMssvLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                jPanel.add(addMssvLabel);
                final JTextArea addMssvArea = new JTextArea();
                addMssvArea.setAlignmentX(Component.CENTER_ALIGNMENT);
                JScrollPane addMssvScrollPane = new JScrollPane(addMssvArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                addMssvScrollPane.setPreferredSize(new Dimension(100, 100));
                jPanel.add(addMssvScrollPane);
                jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

                JPanel buttonsPanel = new JPanel();
                JButton confirmBtn = new JButton("Xác nhận");
                confirmBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        try {
                            PreparedStatement statement = connection
                                    .prepareStatement("INSERT INTO danh_sach_sv_mh VALUES (?,?)");

                            String listOfMssv = addMssvArea.getText();
                            Scanner scanner = new Scanner(listOfMssv);
                            while (scanner.hasNextLine()) {
                                statement.setString(1, maMH);
                                statement.setString(2, scanner.nextLine());
                                statement.executeUpdate();
                            }
                            scanner.close();

                            JOptionPane.showMessageDialog(jFrame, "Success!", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(jFrame, "Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }

                    }
                });
                JButton cancelBtn = new JButton("Huỷ bỏ");
                cancelBtn.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        jFrame.dispose();
                    }

                });
                confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                buttonsPanel.add(Box.createRigidArea(new Dimension(0, 70)));
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.add(confirmBtn);
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.add(cancelBtn);
                buttonsPanel.add(Box.createHorizontalGlue());
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

                jFrame.add(jPanel);
                jPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                jFrame.add(buttonsPanel);
                buttonsPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
        selectMethodJFrame.add(nhapMssvBtn);
        selectMethodJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectMethodJFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectMethodJFrame.pack();
        selectMethodJFrame.setVisible(true);
    }

    public static void main(String[] args) {
        connection = getDBConnection();
        createMonHoc();
        // createThoiKhoaBieu();
        // addSVToMonHoc("CSC001");
    }
}
