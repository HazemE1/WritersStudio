package com.team34.model.chapter;

import com.team34.controller.ColorGenerator;

public class Chapter {

    private String name = "";
    private String description = "";
    private String color = "";

    public Chapter(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getColor(){
        return color;
    }
}
