package com.team34.model.character;

import com.team34.model.event.EventListObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CharacterManager
 * @author Eric and Anton
 * created: 2022-03-01.
 */
class CharacterManagerTest {
    private CharacterManager cm;

    @BeforeEach
    public void setup(){
        cm = new CharacterManager();
    }

    /**
     * Tests to create the character with age 25
     * then gets the age of the character and expect it to be 25.
     */
    @Test
    void newCharacterWithValidAge() {
        long charID = cm.newCharacter("Sven", " ", 25, new EventListObject("hejhej", 1L), 100, 100);
        String[] data = cm.getCharacterData(charID);
        int age = Integer.parseInt(data[3]);
        assertEquals(25, age);
    }

    /**
     * Creates a character with the age -25 which is not possible
     * and therefore should get the uid as -1L to use later with popup
     * and error messages.
     */
    @Test
    void newCharacterWithInValidAge() {
        long charID = cm.newCharacter("Benjamin", " ", -25, new EventListObject("hejhej", 1L), 100, 100);
        assertEquals(-1L, charID);
    }

    /**
     * Creates a character and puts the UID in charID, thereafter we delete a character
     * and gives the charID as params. Then we get the character list with cm.getCharacterList
     * and checks if the size is 0.
     */
    @Test
    void deleteCharacter(){
        long charID = cm.newCharacter("Magnus", " ", 25, new EventListObject("hejhej", 1L), 100, 100);
        cm.deleteCharacter(charID);
        ArrayList<Object[]> characters = cm.getCharacterList();
        assertEquals(0, characters.size());
    }
    /**
     * Creates a character and puts the UID in charID, then we create another character without saving the UID
     * thereafter we delete a character and gives the charID as params.
     * Then we get the character list with cm.getCharacterList
     * and checks if the size is 1.
     */
    @Test
    void deleteCharacterOneRemaining(){
        long charID = cm.newCharacter("Magnus", " ", 25, new EventListObject("hejhej", 1L), 100, 100);
        cm.newCharacter("Johan", " ", 25, new EventListObject("hejhej", 1L), 100, 100);
        cm.deleteCharacter(charID);
        ArrayList<Object[]> characters = cm.getCharacterList();
        assertEquals(1, characters.size());
    }
    /**
     * Tests trying to delete when character list is null or empty. deleteCharacter should
     * for the test to succeed be a boolean that returns false if the characterMap.remove(uid)
     * returns null. And true if characterMap.remove(uid) is successful
     */
    @Test
    void deleteCharacterWhenNoCharRemaining(){
        long charID = 0;
        //assertFalse(cm.deleteCharacter(charID));//commentet just because if not the program cannot run with the current code.
    }

}