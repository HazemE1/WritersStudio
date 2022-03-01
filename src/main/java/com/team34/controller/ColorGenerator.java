package com.team34.controller;

import java.util.Random;

/**
 * Generate random color for events.
 * @author Frida Jacobsson
 */
public class ColorGenerator {

    static int n = 0;

    /**
     * @author Frida Jacobsson
     * @return a predefined hex color in string based on a algorithm
     */
    public static String getNewColor() {
        switch ((n++)%7) {
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

    /**
     * @author Frida Jacobsson
     * @return random hex color in string
     */
    public static String getNewRandomColor(){
        Random random = new Random();
        int randomNumber = random.nextInt(0xffffff + 1);
        String colorCode = String.format("#%06x", randomNumber);
        return colorCode;
    }
}
