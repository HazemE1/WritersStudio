package com.team34.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void numberAge() {
        Character c = new Character("Anton", null, 25, null, 100, 100);
        int age = c.getAge();
        assertEquals(25, age);
    }
}