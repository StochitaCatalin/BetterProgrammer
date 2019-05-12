package com.stochitacatalin.betterprogrammer;

import java.io.Serializable;

public class ChapterItem implements Serializable {
    public int _ID;
    public int topic;
    String name;
    public int sections;
    public int completed;

    ChapterItem(int _ID,int topic,String name,int sections,int completed){
        this._ID = _ID;
        this.topic = topic;
        this.name = name;
        this.sections = sections;
        this.completed = completed;
    }

}
