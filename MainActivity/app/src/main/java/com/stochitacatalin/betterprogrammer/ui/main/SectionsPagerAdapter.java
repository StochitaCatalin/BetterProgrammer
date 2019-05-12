package com.stochitacatalin.betterprogrammer.ui.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.stochitacatalin.betterprogrammer.ChapterItem;
import com.stochitacatalin.betterprogrammer.Database.ChapterContract;
import com.stochitacatalin.betterprogrammer.Database.DatabaseHelper;
import com.stochitacatalin.betterprogrammer.Database.SectionComponentContract;
import com.stochitacatalin.betterprogrammer.Database.SectionContract;
import com.stochitacatalin.betterprogrammer.Database.TopicContract;
import com.stochitacatalin.betterprogrammer.TopicItem;

import java.io.Serializable;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public class TabInfo implements Serializable {
        int id;
        int chapter;
        String name;
        public boolean completed;
        public String type;

        TabInfo(int id, int chapter,String name,String type,boolean completed) {
            this.id = id;
            this.chapter = chapter;
            this.name = name;
            this.type = type;
            this.completed = completed;
        }
    }

    private TabInfo[] TAB_INFO;
    private final Context mContext;
    private ChapterItem chapterItem;
    public SectionsPagerAdapter(Context context, FragmentManager fm, ChapterItem chapterItem) {
        super(fm);
        mContext = context;
        this.chapterItem = chapterItem;
        SQLiteDatabase databaseHelper = new DatabaseHelper(mContext).getReadableDatabase();
        TAB_INFO = retriveTabInfo(databaseHelper);
    }

    public TabInfo[] getTabs(){
        return TAB_INFO;
    }

    TabInfo[] retriveTabInfo(SQLiteDatabase db){
        TabInfo[] infos = new TabInfo[chapterItem.sections];

        String[] projection = {
                BaseColumns._ID,
                SectionContract.SectionEntry.COLUMN_NAME_CHAPTER,
                SectionContract.SectionEntry.COLUMN_NAME_NAME,
                SectionContract.SectionEntry.COLUMN_NAME_TYPE,
                SectionContract.SectionEntry.COLUMN_NAME_COMPLETED,
        };

// Filter results WHERE "title" = 'My Title'
          String selection = SectionContract.SectionEntry.COLUMN_NAME_CHAPTER + " = ?";
            String [] selectionArgs = {String.valueOf(chapterItem._ID)};

// How you want the results sorted in the resulting Cursor
           String sortOrder = SectionContract.SectionEntry.COLUMN_NAME_ORDER + " ASC";

        Cursor cursor = db.query(
                SectionContract.SectionEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        int i = 0;
        while(cursor.moveToNext()) {
            infos[i] = new TabInfo(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4)==1);
            i++;
        }

        cursor.close();
        return infos;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      //  return mContext.getResources().getString(TAB_TITLES[0]);
        return String.valueOf(TAB_INFO[position].name);
    }

    @Override
    public int getCount() {
        if(TAB_INFO == null)
            return 0;
        else
            return TAB_INFO.length;
    }

    public void complete(int position){
        TAB_INFO[position].completed = true;

        SQLiteDatabase db = new DatabaseHelper(mContext).getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SectionContract.SectionEntry.COLUMN_NAME_COMPLETED, true);

// Which row to update, based on the title
        String selection = SectionContract.SectionEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(TAB_INFO[position].id)};

        int count = db.update(
                SectionContract.SectionEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        chapterItem.completed = ChapterContract.ChapterEntry.updateSections(db,chapterItem._ID);
        if(chapterItem.completed == chapterItem.sections){
            TopicContract.TopicEntry.updateChapters(db,chapterItem.topic);
        }
       // if(count == 1)
           //topicItem.completed = TAB_INFO.length;
    }

}