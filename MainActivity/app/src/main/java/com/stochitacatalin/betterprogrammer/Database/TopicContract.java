package com.stochitacatalin.betterprogrammer.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.stochitacatalin.betterprogrammer.TopicItem;

import java.util.ArrayList;

public final class TopicContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TopicContract() {}

    /* Inner class that defines the table contents */
    public static class TopicEntry implements BaseColumns {
        public static final String TABLE_NAME = "topics";
        public static final String COLUMN_NAME_TITLE = "title";
       // public static final String COLUMN_NAME_CHAPTERS = "chapters";
      //  public static final String COLUMN_NAME_COMPLETED = "completed";
      //  public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_ORDER = "`order`";
        static ArrayList<Integer> getChaptersId(SQLiteDatabase db, TopicItem topicItem) {
            String[] projection = {
                    BaseColumns._ID,
            };

            String selection = ChapterContract.ChapterEntry.COLUMN_NAME_TOPIC + " = ?";
            String[] selectionArgs = {String.valueOf(topicItem._ID)};

            Cursor cursor = db.query(
                    ChapterContract.ChapterEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            ArrayList<Integer> ids = new ArrayList<>();

            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(0));
            }
            cursor.close();
            return ids;
        }
        public static void setChaptersCompleted(SQLiteDatabase db, TopicItem topicItem) {
            ArrayList<Integer> chapters = getChaptersId(db,topicItem);
            topicItem.setChapters(chapters.size());
            int completed = 0;
            for(int chapter:chapters) {
                if (ChapterContract.ChapterEntry.getCompleted(db, chapter)) {
                    completed++;
                }
            }

            topicItem.setCompleted(completed);
              /*  String[] projection = {
                        BaseColumns._ID,
                        ChapterContract.ChapterEntry.COLUMN_NAME_SECTIONS,
                        ChapterContract.ChapterEntry.COLUMN_NAME_COMPLETED
                };

                String selection = ChapterContract.ChapterEntry.COLUMN_NAME_TOPIC + " = ?";
                String[] selectionArgs = {String.valueOf(topicItem._ID)};

                Cursor cursor = db.query(
                        ChapterContract.ChapterEntry.TABLE_NAME,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        selection,              // The columns for the WHERE clause
                        selectionArgs,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        null               // The sort order
                );*/
        }
    }

}