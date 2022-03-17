package com.team34.model.chapter;

import com.team34.model.UIDManager;
import com.team34.model.event.EventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ChapterManager {

    private HashMap<Long, Chapter> chapters;
    private ArrayList<LinkedList<Long>> chapterOrderLists;
    private boolean hasChanged;

    public ChapterManager() {
        hasChanged = false;
        chapters = new HashMap<>();
        chapterOrderLists = new ArrayList<>();
        chapterOrderLists.add(new LinkedList<>());

    }

    public long newChapter(String name, String description, String color) {
        long uid = UIDManager.nextUID();
        addChapter(uid, name, description, color);

        if (chapterOrderLists.size() < 1)
            chapterOrderLists.add(new LinkedList<>());

        for (LinkedList<Long> e : chapterOrderLists)
            e.add(uid);

        return uid;
    }

    public boolean editChapter(long uid, String name, String description) {
        if (chapters.containsKey(uid)) {
            chapters.replace(uid, new Chapter(name, description, "#F28220"));
            hasChanged = true;
            return true;
        }
        return false;
    }

    public void removeChapter(long uid) {
        chapters.remove(uid);
        UIDManager.removeUID(uid);

        for (LinkedList<Long> e : chapterOrderLists)
            e.remove(uid);

        hasChanged = true;
    }

    public void addChapter(long uid, String name, String description, String color) {
        chapters.put(uid, new Chapter(name, description, color));
        hasChanged = true;
    }

    public Object[] getChapterData(long uid) {
        Object[] data = new Object[2];
        Chapter chapter = chapters.get(uid);
        data[0] = chapter.getName();
        data[1] = chapter.getDescription();

        return data;
    }

    public Object[][] getChapters() {
        if (chapters.size() < 1)
            return null;

        Long[] uidOrder = chapters.keySet().toArray(new Long[chapters.size()]);
        Object[][] eventArray = new Object[uidOrder.length][4];

        for (int i = 0; i < uidOrder.length; i++) {
            long uid = uidOrder[i];
            Chapter chapterRef = chapters.get(uid);
            eventArray[i][0] = uid;
            eventArray[i][1] = chapterRef.getName();
            eventArray[i][2] = chapterRef.getDescription();
            eventArray[i][3] = chapterRef.getColor();
        }
        System.out.println(Arrays.deepToString(eventArray) + "Chapters");
        return eventArray;
    }

    public Long[] getChapterOrder(int chapterOrderList) {
        if (chapterOrderLists == null)
            return null;

        if (chapterOrderList >= chapterOrderLists.size() || chapterOrderList < 0)
            return null;


        System.out.println(chapterOrderLists+ " ChapterorderLists");
        //System.out.println(Arrays.toString(chapterOrderLists.get(chapterOrderList).toArray()) + "hej2");

        return chapterOrderLists.get(chapterOrderList).toArray(
                new Long[chapterOrderLists.get(chapterOrderList).size()]

        );
    }

    public int getChapterIndex(int chapterOrderList, long uid) {
        Long[] chapters = getChapterOrder(chapterOrderList);

        for (int i = 0; i < chapters.length; i++) {
            if (chapters[i].equals(uid))
                return i;
        }
        return -1;
    }

    public void addOrderList(LinkedList<Long> orderList) {
        chapterOrderLists.add(orderList);
    }

    /**
     * Removes all events and event order lists, and sets {@link #} to false.
     */
    public void clear() {
        chapters.clear();
        chapterOrderLists.clear();
        hasChanged = false;
    }

    /**
     * Returns whether the data inside this class has changed
     *
     * @return the value of {@link #}
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     * Sets {@link #} to false.
     */
    public void resetChanges() {
        hasChanged = false;
    }

    public Chapter getChapter(long uid) {
        return this.chapters.get(uid);
    }

    public Chapter getChapter(String chapterName) {
        for (Chapter value : chapters.values()) {
            if (value.getName().equalsIgnoreCase(chapterName))
                return value;
        }
        return null;
    }


}
