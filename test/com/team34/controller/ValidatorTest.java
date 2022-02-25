package com.team34.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test void letterShouldReturnMinusOne() {
        var validator = new Validator();
        assertEquals(-1, validator.returnStringAsInt("hello"));
    }

    @Test void numberShouldBeReturnedAsDigit() {
        var validator = new Validator();
        assertEquals(5, validator.returnStringAsInt("5"));
    }

    @Test void nullShouldReturnMinusOne() {
        var validator = new Validator();
        assertEquals(-1, validator.returnStringAsInt(null));
    }

    @Test void emptyStringShouldReturnMinusOne() {
        var validator = new Validator();
        assertEquals(-1, validator.returnStringAsInt(""));
    }

    ////

    @Test
    void testMinusOneShouldReturnFalse() {
        var validator = new Validator();
        assertFalse(validator.validateValidAge(-1));
    }

    @Test
    void testZeroShouldReturnTrue() {
        var validator = new Validator();
        assertTrue(validator.validateValidAge(0));
    }

    @Test
    void testOneShouldReturnTrue() {
        var validator = new Validator();
        assertTrue(validator.validateValidAge(1));
    }
}