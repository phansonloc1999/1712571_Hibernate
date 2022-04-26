package com.example;

import javax.swing.*;

import Utils.CheckBoxList;
import Utils.EncryptionUtils;
import Utils.SinhVienDAO;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GiaoVu {
    private Connection connection = null;
    private java.util.List<SinhVien> sinhVienList = null;

    public GiaoVu() {
        connection = null;
        sinhVienList = SinhVienDAO.getSinhVienList();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void addNewSinhVien() {
        final JFrame jFrame = new JFrame();

        JLabel mssvJLabel = new JLabel("Nhập mã số sinh viên");
        jFrame.add(mssvJLabel);
        final JTextField mssvTxtField = new JTextField();
        mssvTxtField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(mssvTxtField);
        JLabel tenSvLabel = new JLabel("Tên sinh viên");
        jFrame.add(tenSvLabel);
        final JTextField tenSvTxtField = new JTextField();
        tenSvTxtField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        jFrame.add(tenSvTxtField);

        JPanel buttonsPanel = new JPanel();
        JButton confirmBtn = new JButton("Xác nhận");
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO sinh_vien (mssv, ten_sv, username, password, forceChangePass) VALUES (?,?,?,?,1)");
                    int mssv = Integer.parseInt(mssvTxtField.getText());
                    statement.setInt(1, mssv);
                    statement.setString(2, tenSvTxtField.getText());
                    statement.setString(3, mssvTxtField.getText());
                    statement.setString(4, EncryptionUtils.sha1FromString(mssvTxtField.getText()));
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(jFrame, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(jFrame, "Failure!", "Error", JOptionPane.ERROR_MESSAGE);
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
        buttonsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        buttonsPanel.setLayout(new FlowLayout());

        jFrame.setTitle("Thêm sinh viên mới");
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.add(buttonsPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void createMonHoc() {
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

                    if (maMHTxtField.getText().equals("")) {
                        JOptionPane.showMessageDialog(jFrame, "Mã môn học không hợp lệ", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    stm.setString(1, maMHTxtField.getText());
                    stm.setString(2, tenMHTxtField.getText());
                    try {
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(jFrame, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        jFrame.dispose();
                        createThoiKhoaBieu(maMHTxtField.getText());
                    } catch (SQLException e1) {
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
    }

    public void createThoiKhoaBieu(final String maMH) {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Tạo thời khoá biểu");

        JLabel ngayBdLabel = new JLabel("Ngày bắt đầu (YYYY-MM-DD)");
        jFrame.add(ngayBdLabel);
        ngayBdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField ngayBdTxtField = new JTextField();
        jFrame.add(ngayBdTxtField);

        JLabel ngayKtLabel = new JLabel("Ngày kết thúc (YYYY-MM-DD)");
        jFrame.add(ngayKtLabel);
        ngayBdLabel.setAlignmentX(Component.TOP_ALIGNMENT);
        final JTextField ngayKtTxtField = new JTextField();
        jFrame.add(ngayKtTxtField);

        JLabel thuLabel = new JLabel("Thứ trong tuần (2 (Thứ 2) -> 8 (Chủ nhật))");
        jFrame.add(thuLabel);
        thuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField thuTxtField = new JTextField();
        jFrame.add(thuTxtField);

        JLabel gioBdLabel = new JLabel("Giờ bắt đầu (HH:MM:SS)");
        jFrame.add(gioBdLabel);
        gioBdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JTextField gioBdTxtField = new JTextField();
        jFrame.add(gioBdTxtField);

        JLabel gioKtLabel = new JLabel("Giờ kết thúc (HH:MM:SS)");
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
                    if (!lastMoreThanFifteenWeeks(ngayBdTxtField.getText(), ngayKtTxtField.getText())) {
                        JOptionPane.showMessageDialog(jFrame, "Course must last 15 weeks!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PreparedStatement stm = connection
                            .prepareStatement(
                                    "INSERT INTO thoi_khoa_bieu (ngay_bd, ngay_kt, thu, gio_bd, gio_kt, ten_phong_hoc, ma_mh) VALUES (?,?,?,?,?,?,?)");
                    stm.setString(1, ngayBdTxtField.getText());
                    stm.setString(2, ngayKtTxtField.getText());
                    stm.setString(3, thuTxtField.getText());
                    stm.setString(4, gioBdTxtField.getText());
                    stm.setString(5, gioKtTxtField.getText());
                    stm.setString(6, phongHocTxtField.getText());
                    stm.setString(7, maMH);
                    try {
                        stm.executeUpdate();
                        JOptionPane.showMessageDialog(jFrame, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        addSVToMonHoc(maMH);
                        jFrame.dispose();
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

    public void addSVToMonHoc(final String maMH) {
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
                for (int i = 0; i < sinhVienList.size(); i++) {
                    String mssv = String.valueOf(sinhVienList.get(i).getMssv());
                    checkBoxList.addCheckbox(new JCheckBox(mssv));
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
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
        selectMethodJFrame.add(checkMssvBtn);

        JButton nhapMssvBtn = new JButton("Nhập danh sách từng dòng mssv");
        nhapMssvBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
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
                            jFrame.dispose();
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
                jFrame.setTitle("Nhập MSSV");
                jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
        selectMethodJFrame.add(nhapMssvBtn);

        JButton openCSVBtn = new JButton("Dùng template CSV");
        openCSVBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String templateCsvStr = getTemplateCsvContentAsString();
                    final JFrame jFrame = new JFrame();
                    JLabel templateDsMssvLabel = new JLabel("Template danh sách mã số sinh viên");
                    templateDsMssvLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    jFrame.add(templateDsMssvLabel);

                    final JTextArea mssvTextArea = new JTextArea();
                    mssvTextArea.setText(templateCsvStr);
                    mssvTextArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    jFrame.add(mssvTextArea);

                    JPanel jPanel = new JPanel();
                    JButton confirmBtn = new JButton("Xác nhận");
                    confirmBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Scanner scanner = new Scanner(mssvTextArea.getText());
                                ArrayList<String> mssvFromCSV = new ArrayList<String>();
                                while (scanner.hasNextLine()) {
                                    String line = scanner.nextLine();
                                    String[] mssvInLine = line.split(",");
                                    for (String mssv : mssvInLine) {
                                        if (!mssv.equals("")) {
                                            mssvFromCSV.add(mssv);
                                        }
                                    }
                                }

                                PreparedStatement statement = connection
                                        .prepareStatement("INSERT INTO danh_sach_sv_mh VALUES (?,?)");
                                for (String mssv : mssvFromCSV) {
                                    statement.setString(1, maMH);
                                    statement.setString(2, mssv);
                                    statement.executeUpdate();
                                }

                                JOptionPane.showMessageDialog(selectMethodJFrame, "Success!", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException e1) {
                                JOptionPane.showMessageDialog(selectMethodJFrame, "Failed!", "Failed",
                                        JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                        }
                    });
                    jPanel.add(confirmBtn);
                    JButton cancelBtn = new JButton("Huỷ bỏ");
                    cancelBtn.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jFrame.dispose();
                        }

                    });
                    jPanel.add(cancelBtn);
                    jPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    jPanel.setLayout(new FlowLayout());
                    jFrame.add(jPanel);

                    jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
                    jFrame.setTitle("Template CSV");
                    jFrame.pack();
                    jFrame.setVisible(true);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        selectMethodJFrame.add(openCSVBtn);

        selectMethodJFrame.setTitle("Thêm sinh viên vào môn học");
        selectMethodJFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectMethodJFrame.pack();
        selectMethodJFrame.setVisible(true);
    }

    public void showDiemDanh(final String maMH) {
        final JFrame jFrame = new JFrame();
        jFrame.setTitle("Điểm danh môn học " + maMH);

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

            jFrame.pack();
            jFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showMainMenu() {
        final JFrame jFrame = new JFrame();

        JButton addSinhVienBtn = new JButton("Thêm sinh viên");
        addSinhVienBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewSinhVien();
            }
        });
        jFrame.add(addSinhVienBtn);

        JButton themMonHocBtn = new JButton("Thêm môn học");
        themMonHocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMonHoc();
            }
        });
        jFrame.add(themMonHocBtn);
        JButton xemDiemDanhBtn = new JButton("Xem điểm danh");
        xemDiemDanhBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM mon_hoc");
                    int numOfMonHoc = 0;
                    while (rs.next()) {
                        numOfMonHoc = rs.getInt(1);
                    }
                    String[] maMonHocArr = new String[numOfMonHoc];
                    rs = statement.executeQuery("SELECT ma_mh FROM mon_hoc");
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

                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });
        jFrame.add(xemDiemDanhBtn);

        jFrame.setTitle("Menu chính");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new FlowLayout());
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public String getTemplateCsvContentAsString() throws IOException {
        InputStream inStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("template.csv");
        String text = new BufferedReader(
                new InputStreamReader(inStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }

    public boolean lastMoreThanFifteenWeeks(String ngayBd, String ngayKt) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date ngayBdDate = dateFormat.parse(ngayBd);
            java.util.Date ngayKtDate = dateFormat.parse(ngayKt);
            long diffInMillies = ngayKtDate.getTime() - ngayBdDate.getTime();
            int diffInWeeks = (int) (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 7);
            if (diffInWeeks < 15)
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
}
