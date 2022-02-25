package com.team34.controller;

/*
@ Author Frida Jacobsson
 */

public class Validator {

    public static int returnStringAsInt(String text) {
        try{
            int age = Integer.parseInt(text);
            return age;
        }catch(NumberFormatException numberFormatException){
            return -1;
        }
    }

    public static boolean validateValidAge(int age) {
        if(age < 0) return false;
        return true;
    }
}
