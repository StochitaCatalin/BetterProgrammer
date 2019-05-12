package com.stochitacatalin.betterprogrammer;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class TopicItem implements Serializable {
    public int _ID;
    String name;
    public int chapters,completed;
    Class acitivity;
    String icon;
    public TopicItem(int _ID,String name,int chapters,int completed,Class activity,String icon){
        this._ID = _ID;
        this.name = name;
        this.chapters = chapters;
        this.completed = completed;
        this.acitivity = activity;
        this.icon = icon;
    }
}
