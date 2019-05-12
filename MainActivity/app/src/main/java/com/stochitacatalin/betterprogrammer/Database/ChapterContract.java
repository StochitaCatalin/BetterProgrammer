package com.stochitacatalin.betterprogrammer.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

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
        public static final String COLUMN_NAME_SECTIONS = "sections";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_ORDER = "`order`";

        public static int updateSections(SQLiteDatabase db, int _ID) {

            String[] projection = {
                    BaseColumns._ID,
            };

            String selection = SectionContract.SectionEntry.COLUMN_NAME_CHAPTER + " = ? AND " + SectionContract.SectionEntry.COLUMN_NAME_COMPLETED + " = ?";
            String[] selectionArgs = {String.valueOf(_ID), String.valueOf(1)};

            Cursor cursor = db.query(
                    SectionContract.SectionEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            int completed = cursor.getCount();

            cursor.close();

            updateCompleted(db,_ID,completed);

            return completed;
        }

        public static void updateCompleted(SQLiteDatabase db,int _ID,int completed){

            ContentValues values = new ContentValues();
            values.put(ChapterEntry.COLUMN_NAME_COMPLETED, completed);

            String selection = ChapterEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(_ID)};

            int count = db.update(
                    ChapterEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }
}