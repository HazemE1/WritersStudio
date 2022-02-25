package com.team34.model.character;

import com.team34.model.event.EventListObject;

/**
 * @author Morgan Karlsson
 * @updated Frida Jacobsson 2022-02-24
 */

public class Character {

    private String name = "";
    private String description = "";
    private int age = 0;
    private double chartPositionX = 0.0;
    private double chartPositionY = 0.0;
    private EventListObject event;

    public Character(String name, String description, int age, EventListObject event, double posX, double posY) {
        this.name = name;
        this.description = description;
        this.event = event;
        this.age = age;
        chartPositionX = posX;
        chartPositionY = posY;
    }

    public EventListObject getEvent() {
        return event;
    }

    public void setEvent(EventListObject event) {
        this.event = event;
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

    public double getChartPositionX() {
        return chartPositionX;
    }

    public double getChartPositionY() {
        return chartPositionY;
    }

    public int getAge() {
        return age;
    }
}
