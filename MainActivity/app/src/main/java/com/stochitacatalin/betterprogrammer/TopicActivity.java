package com.stochitacatalin.betterprogrammer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stochitacatalin.betterprogrammer.Database.ChapterContract;
import com.stochitacatalin.betterprogrammer.Database.DatabaseHelper;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    TopicItem topic;
    ArrayList<ChapterItem> chapters;
    ChaptersAdapter chaptersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        topic = (TopicItem) getIntent().getSerializableExtra("topic");

        ListView listView = findViewById(R.id.listView);

        SQLiteDatabase databaseHelper = new DatabaseHelper(this).getReadableDatabase();
        chapters = retriveChapters(databaseHelper);
        chaptersAdapter = new ChaptersAdapter(this,chapters);
        listView.setAdapter(chaptersAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChapterItem chapterItem = chapters.get(position);

                Intent intent = new Intent(TopicActivity.this,GamesLearnActivity.class);
                intent.putExtra("topic",topic);
                intent.putExtra("chapter",chapterItem);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        ArrayList<ChapterItem> chapterItems = retriveChapters(db);
        chapters.clear();
        chapters.addAll(chapterItems);

        chaptersAdapter.notifyDataSetChanged();
    }

    ArrayList<ChapterItem> retriveChapters(SQLiteDatabase db){
        ArrayList<ChapterItem> chapters = new ArrayList<>();

        String[] projection = {
                BaseColumns._ID,
                ChapterContract.ChapterEntry.COLUMN_NAME_TOPIC,
                ChapterContract.ChapterEntry.COLUMN_NAME_TITLE,
           //     ChapterContract.ChapterEntry.COLUMN_NAME_SECTIONS,
           //     ChapterContract.ChapterEntry.COLUMN_NAME_COMPLETED,
        };

// Filter results WHERE "title" = 'My Title'
        String selection = ChapterContract.ChapterEntry.COLUMN_NAME_TOPIC + " = ?";
        String [] selectionArgs = {String.valueOf(topic._ID)};

// How you want the results sorted in the resulting Cursor
        String sortOrder = ChapterContract.ChapterEntry.COLUMN_NAME_ORDER + " ASC";

        Cursor cursor = db.query(
                ChapterContract.ChapterEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            chapters.add(new ChapterItem(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)));//, cursor.getInt(3), cursor.getInt(4)));
        }
        cursor.close();

        for(ChapterItem chapterItem:chapters){
            ChapterContract.ChapterEntry.setSectionsCompleted(db,chapterItem);
        }

        return chapters;
    }
}
