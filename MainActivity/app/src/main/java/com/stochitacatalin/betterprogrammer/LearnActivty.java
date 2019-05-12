package com.stochitacatalin.betterprogrammer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.stochitacatalin.betterprogrammer.Database.DatabaseHelper;
import com.stochitacatalin.betterprogrammer.Database.TopicContract;

import java.util.ArrayList;

public class LearnActivty extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ArrayList<TopicItem> topics;
    TopicsAdapter topicsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_activty);

        dbHelper = new DatabaseHelper(this);

        topics = fillTopics();
        GridView gridView = findViewById(R.id.gridView);
        topicsAdapter = new TopicsAdapter(this,topics);
        gridView.setAdapter(topicsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicItem topic = ((TopicItem)parent.getItemAtPosition(position));
                Intent intent = new Intent(LearnActivty.this,TopicActivity.class);
                intent.putExtra("topic",topic);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<TopicItem> chapterItems = fillTopics();
        topics.clear();
        topics.addAll(chapterItems);

        topicsAdapter.notifyDataSetChanged();
    }

    ArrayList<TopicItem> fillTopics(){
        ArrayList<TopicItem> topics = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                TopicContract.TopicEntry.COLUMN_NAME_TITLE,
               // TopicContract.TopicEntry.COLUMN_NAME_CHAPTERS,
               // TopicContract.TopicEntry.COLUMN_NAME_COMPLETED,
              //  TopicContract.TopicEntry.COLUMN_NAME_ACTIVITY,
                TopicContract.TopicEntry.COLUMN_NAME_ICON
        };

// Filter results WHERE "title" = 'My Title'
      //  String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
    //    String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder = TopicContract.TopicEntry.COLUMN_NAME_ORDER + " ASC";

        Cursor cursor = db.query(
                TopicContract.TopicEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            topics.add(new TopicItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }

        cursor.close();

        for(TopicItem topicItem:topics){
            TopicContract.TopicEntry.setChaptersCompleted(db,topicItem);
        }

        return topics;
    }
}
