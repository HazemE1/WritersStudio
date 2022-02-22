package com.team34.model.chapter;

import com.team34.model.UIDManager;
import com.team34.model.event.Event;
import com.team34.model.event.EventManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ChapterManager {

    private HashMap<Long, Chapter> chapters;
    private ArrayList<LinkedList<Long>> chapterOrderLists;
    private boolean hasChanged;

    public ChapterManager() {
        hasChanged = false;
        chapters = new HashMap<Long, Chapter>();
        chapterOrderLists = new ArrayList<>();
        chapterOrderLists.add(new LinkedList<Long>());
    }

    public long newChapter(String name, String description) {
        long uid = UIDManager.nextUID();
        addChapter(uid, name, description);

        if (chapterOrderLists.size() < 1)
            chapterOrderLists.add(new LinkedList<>());

        for (LinkedList<Long> e : chapterOrderLists)
            e.add(uid);

        return uid;
    }
    public boolean editChapter(long uid, String name, String description) {
        if (chapters.containsKey(uid)) {
            chapters.replace(uid, new Chapter(name, description));
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

    public void addChapter(long uid, String name, String description) {
        chapters.put(uid, new Chapter(name, description));
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
        Object[][] eventArray = new Object[uidOrder.length][3];


        for (int i = 0; i < uidOrder.length; i++) {
            long uid = uidOrder[i];
            Chapter chapterRef = chapters.get(uid);
            eventArray[i][0] = uid;
            eventArray[i][1] = chapterRef.getName();
            eventArray[i][2] = chapterRef.getDescription();
        }
        return eventArray;
    }

    public Long[] getChapterOrder(int chapterOrderList) {
        if (chapterOrderLists == null)
            return null;
        if (chapterOrderList >= chapterOrderLists.size() || chapterOrderList < 0)
            return null;

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
     * Removes all events and event order lists, and sets {@link EventManager#} to false.
     */
    public void clear() {
        chapters.clear();
        chapterOrderLists.clear();
        hasChanged = false;
    }

    /**
     * Returns whether the data inside this class has changed
     *
     * @return the value of {@link EventManager#}
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     * Sets {@link EventManager#} to false.
     */
    public void resetChanges() {
        hasChanged = false;
    }




}
