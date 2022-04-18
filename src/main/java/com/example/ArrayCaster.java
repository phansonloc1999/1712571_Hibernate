package com.example;

public abstract class ArrayCaster {
    public static String[] objectToStringArray(Object[] objects) {
        String[] strings = new String[objects.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = (String) objects[i];
        }
        return strings;
    }
}
