package com.team34.model.event;

import com.team34.controller.ColorGenerator;
import com.team34.model.chapter.ChapterListObject;

/**
 * Class for event objects that contains the event information.
 *
 * @author Jim Andersson
 */

public class Event {

    private String name = "";
    private String description = "";
    private ChapterListObject chapterListObject;
    private String color = "";

    /**
     * Instantiates event object with name and description.
     *
     * @param name        Event name
     * @param description Event description
     */
    public Event(String name, String description, ChapterListObject chapterListObject) {
        this.name = name;
        this.description = description;
        this.chapterListObject = chapterListObject;
        this.color = ColorGenerator.getNewColor();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChapterListObject getChapterListObject() {
        return chapterListObject;
    }

    public Object getColor() {
        return color;
    }
}
