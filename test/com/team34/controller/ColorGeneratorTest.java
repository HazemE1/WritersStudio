package com.team34.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColorGeneratorTest {

    @AfterEach
    public void reset() {
        ColorGenerator.n = 0;
    }

    @Test
    public void getDefaultColor() {
        String color = ColorGenerator.getNewColor();
        assertEquals("#F282A7", color);
    }

    @Test
    public void getFirstColor() {
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#9EC0E6", color);
    }

    @Test
    public void getSecondColor() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#9EE6BD", color);
    }

    @Test
    public void getThirdColor() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#CBB6D9", color);
    }

    @Test
    public void getFourthColor() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#DE866D", color);
    }

    @Test
    public void getFifthColor() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#6D9DA6", color);
    }

    @Test
    public void getSixthColor() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#2179BF", color);
    }

    @Test
    public void getCheckModulo() {
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();
        ColorGenerator.getNewColor();

        String color = ColorGenerator.getNewColor();
        assertEquals("#9EC0E6", color);
    }
}