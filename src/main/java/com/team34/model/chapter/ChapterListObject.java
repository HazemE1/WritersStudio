package com.team34.model.chapter;

public class ChapterListObject {

    private String title;
    private long uid;

    public ChapterListObject(String title, long uid) {
        this.title = title;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }


    public long getUid() {
        return uid;
    }

}
