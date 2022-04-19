package com.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        GiaoVu giaoVu = new GiaoVu();
        giaoVu.connection = DatabaseUtils.getDBConnection("giaovu", "giaovu");
        // giaoVu.createMonHoc();
        giaoVu.showDiemDanh("CSC002");
    }
}
