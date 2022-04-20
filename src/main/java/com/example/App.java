package com.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        GiaoVu giaoVu = new GiaoVu();
        giaoVu.setConnection(DatabaseUtils.getDBConnection("giaovu", "giaovu"));
        giaoVu.createMonHoc();

        // SinhVien sinhVien = new SinhVien(DatabaseUtils.getDBConnection("sinhvien", "sinhvien"), 1712572);
        // sinhVien.showDiemDanh("CSC002");
    }
}
