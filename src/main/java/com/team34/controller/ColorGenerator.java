package com.team34.controller;

import java.util.Random;

public class ColorGenerator {

    static int n = 0;

    public static String getNewColor() {

        switch ((n++)%6) {
            case 1:
                return "#9EC0E6";
            case 2:
                return "#9EE6BD";
            case 3:
                return "#CBB6D9";
            case 4:
                return "#DE866D";
            case 5:
                return "#6D9DA6";
            case 6:
                return "#2179BF";
            default:
                return "#F282A7";
        }
    }
}
