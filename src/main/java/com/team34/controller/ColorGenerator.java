package com.team34.controller;

import java.util.Random;

public class ColorGenerator {

    static Random rand = new Random();

    public static String getNewColor() {

        switch (rand.nextInt(5)-1) {
            case 1:
                return "#21AFBF";
            case 2:
                return "#A0D9D9";
            case 3:
                return "#F2D9BB";
            case 4:
                return "#F2C3A7";
            case 5:
                return "#F2E0D0";
            case 6:
                return "#2179BF";
            default:
                return "#F282A7";
        }
    }
    public static int getNewValue(){
        for(int i = 1; i < 6; i++){
            return i;
        }
        return 1;
    }
}
