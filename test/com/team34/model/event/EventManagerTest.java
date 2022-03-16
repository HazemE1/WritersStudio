package com.team34.model.event;

import com.team34.model.character.CharacterManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Unittest
 * @Author Alexander Olsson
 */

public class EventManagerTest {


    private EventManager em;

    @BeforeEach
    public void setup(){
        em = new EventManager();
    }


    @Test
    void newEvent() {
        long evtID = em.newEvent("Event", "EventDesc", null);
        Object[] data = em.getEventData(evtID);
        assertEquals("Event,EventDesc,,", data[0]+ "," + data[1] + ","+data[2]+","+data[3]);
    }

    @Test
    void noEvents(){
        assertNull(em.getEvents());
    }

    @Test
    void editEvent(){
        long evtID = em.newEvent("Event","EventDesc",null);
        em.editEvent(evtID,"NewEvent","NewEventDesc",null);
        Object[] data = em.getEventData(evtID);
        assertEquals("NewEvent,NewEventDesc,,", data[0]+ "," + data[1] + ","+data[2]+","+data[3]);
    }

    @Test
    void removeEvent(){
        long evtID = em.newEvent("Event","EventDesc",null);
        em.removeEvent(evtID);
        assertNull(em.getEvents());
    }


}
