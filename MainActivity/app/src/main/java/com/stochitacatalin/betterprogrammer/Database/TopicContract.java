package com.stochitacatalin.betterprogrammer.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class TopicContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TopicContract() {}

    /* Inner class that defines the table contents */
    public static class TopicEntry implements BaseColumns {
        public static final String TABLE_NAME = "topics";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CHAPTERS = "chapters";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_ORDER = "`order`";
        public static int updateChapters(SQLiteDatabase db, int _ID) {

            String[] projection = {
                    BaseColumns._ID,
                    ChapterContract.ChapterEntry.COLUMN_NAME_SECTIONS,
                    ChapterContract.ChapterEntry.COLUMN_NAME_COMPLETED
            };

            String selection = ChapterContract.ChapterEntry.COLUMN_NAME_TOPIC + " = ?";
            String[] selectionArgs = {String.valueOf(_ID)};

            Cursor cursor = db.query(
                    ChapterContract.ChapterEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            int completed = 0;

            while(cursor.moveToNext()){
                if(cursor.getInt(1) == cursor.getInt(2)) {
                    completed++;
                }
            }

            cursor.close();

            updateCompleted(db,_ID,completed);

            return completed;
        }

        public static void updateCompleted(SQLiteDatabase db,int _ID,int completed){

            ContentValues values = new ContentValues();
            values.put(TopicEntry.COLUMN_NAME_COMPLETED, completed);

            String selection = TopicEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(_ID)};

            int count = db.update(
                    TopicEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

    }

}