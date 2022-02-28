package com.team34.controller;

/**
 * Handle functionality for checking different values.
 * Has a test class in test dictionary with automated tests.
 *
 * @author Frida Jacobsson
 */
public class Validator {

    /**
     * Method that takes an age in String and try to parse it to an integer.
     *
     * @param text
     * @return an age in integer or -1 if text is not possible to parse text to int
     */
    public static int returnStringAsInt(String text) {
        try {
            int age = Integer.parseInt(text);
            return age;
        } catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    /**
     * @param age
     * @return false if age is negative, true if age is >=0
     */
    public static boolean validateValidAge(int age) {
        if (age < 0) return false;
        return true;
    }
}
