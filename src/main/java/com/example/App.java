package com.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        GiaoVu giaoVu = new GiaoVu();
        giaoVu.connection = DatabaseUtils.getDBConnection();
        giaoVu.createMonHoc();

        // createMonHoc();
        // createThoiKhoaBieu();
        // addSVToMonHoc("CSC001");
        // showDiemDanh("CSC002");
    }
}
