package com.team34.model.chapter;

import com.team34.controller.ColorGenerator;

public class ChapterListObject {

    private String title;
    private long uid;
    private String color;

    public ChapterListObject(String title, long uid, String color) {
        this.title = title;
        this.uid = uid;
        this.color = color;

    }

    public String getTitle() {
        return title;
    }

    public long getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getColor() {
        return color;
    }
}
