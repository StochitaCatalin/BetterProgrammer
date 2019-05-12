package com.stochitacatalin.betterprogrammer;


import java.io.Serializable;

public class TopicItem implements Serializable {
    public int _ID;
    String name;
    public int chapters,completed;
    String icon;
    public TopicItem(int _ID,String name,String icon){
        this._ID = _ID;
        this.name = name;
        this.icon = icon;
    }

    public void setChapters(int chapters){
        this.chapters = chapters;
    }

    public void setCompleted(int completed){
        this.completed = completed;
    }
}
