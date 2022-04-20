package com.example;

import Utils.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        GiaoVu giaoVu = new GiaoVu();
        giaoVu.setConnection(DatabaseUtils.getDBConnection("giaovu", "giaovu"));
        giaoVu.showMainMenu();
    }
}
