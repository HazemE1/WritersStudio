package com.team34.view.character;

import com.team34.model.event.EventListObject;

/**
 * The CharacterListObject class is used to create objects containing character names and UIDs. The objects are used
 * in the {@link com.team34.view.character.CharacterList} class for displaying the characters in the list view, and for
 * easier access to their respective UIDs when a character list item is selected.
 */
public class CharacterListObject {
    private String name;
    private long uid;
    private EventListObject eventListObject;

    public CharacterListObject(String name,long uid) {
        this.name = name;
        this.uid = uid;

    }

    public EventListObject getEventListObject() {
        return eventListObject;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    /**
     * Overridden toString function so the {@link com.team34.view.character.CharacterList} is able to display
     * the character names in the list view.
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
