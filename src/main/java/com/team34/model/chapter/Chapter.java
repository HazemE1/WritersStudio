package com.team34.model.chapter;

import com.team34.model.event.Event;

import java.util.HashSet;
import java.util.Set;

public class Chapter {

    private String name = "";
    private String description = "";
    private String color = "";
    private Set<Event> events;

    public Chapter(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
        events = new HashSet<>();
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public String getColor(){
        return color;
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
}
