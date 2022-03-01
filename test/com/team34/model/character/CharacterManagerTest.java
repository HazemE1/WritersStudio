package com.team34.model.character;

import com.team34.model.event.EventListObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CharacterManagerTest {
    private CharacterManager cm;

    @BeforeEach
    public void setup(){
        cm = new CharacterManager();
    }

    @Test
    void newCharacterWithValidAge() {
        long charID = cm.newCharacter("Eric", " ", 25, new EventListObject("hejhej", 1L), 100, 100);
        String[] data = cm.getCharacterData(charID);
        int age = Integer.parseInt(data[3]);
        assertEquals(25, age);
    }
    @Test
    void newCharacterWithInValidAge() {
        long charID = cm.newCharacter("Eric", " ", -25, new EventListObject("hejhej", 1L), 100, 100);
        assertEquals(-1L, charID);
    }
}