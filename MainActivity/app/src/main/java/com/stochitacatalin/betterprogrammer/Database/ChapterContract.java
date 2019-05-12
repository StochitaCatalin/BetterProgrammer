package com.stochitacatalin.betterprogrammer.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.stochitacatalin.betterprogrammer.ChapterItem;

public final class ChapterContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ChapterContract() {
    }

    /* Inner class that defines the table contents */
    public static class ChapterEntry implements BaseColumns {
        public static final String TABLE_NAME = "chapters";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TOPIC = "topic";
    //    public static final String COLUMN_NAME_SECTIONS = "sections";
     //   public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_ORDER = "`order`";

        public static boolean getCompleted(SQLiteDatabase db,int _ID){
            String[] projection = {
                    BaseColumns._ID,
                    SectionContract.SectionEntry.COLUMN_NAME_COMPLETED
            };

            String selection = SectionContract.SectionEntry.COLUMN_NAME_CHAPTER + " = ?";
            String[] selectionArgs = {String.valueOf(_ID)};

            Cursor cursor = db.query(
                    SectionContract.SectionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            int sections = cursor.getCount();
            int completed = 0;

            while(cursor.moveToNext()){
                if(cursor.getInt(1) == 1){
                    completed++;
                }
            }

            cursor.close();

            return sections == completed;
        }

       public static void setSectionsCompleted(SQLiteDatabase db, ChapterItem chapterItem){
           String[] projection = {
                   BaseColumns._ID,
                   SectionContract.SectionEntry.COLUMN_NAME_COMPLETED
           };

           String selection = SectionContract.SectionEntry.COLUMN_NAME_CHAPTER + " = ?";
           String[] selectionArgs = {String.valueOf(chapterItem._ID)};

           Cursor cursor = db.query(
                   SectionContract.SectionEntry.TABLE_NAME,
                   projection,
                   selection,
                   selectionArgs,
                   null,
                   null,
                   null
           );

           chapterItem.sections = cursor.getCount();
           int completed = 0;

           while(cursor.moveToNext()){
               if(cursor.getInt(1) == 1){
                   completed++;
               }
           }

           cursor.close();

           chapterItem.completed = completed;
       }
    }
}