package com.stochitacatalin.betterprogrammer.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 18;
    public static final String DATABASE_NAME = "betterprogrammer";
    public static final String ASSETS_PATH = "databases";
    private Context context;
    private SharedPreferences preferences;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName()+".database_versions", Context.MODE_PRIVATE);
    }

    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
       // db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private Boolean installedDatabaseIsOutdated() {
        return preferences.getInt(DATABASE_NAME, 0) < DATABASE_VERSION;
    }

    private void writeDatabaseVersionInPreferences() {
        preferences.edit().putInt(DATABASE_NAME, DATABASE_VERSION).apply ();
    }

    private void installDatabaseFromAssets() {
        try {
        InputStream inputStream = context.getAssets().open(ASSETS_PATH+"/"+DATABASE_NAME+".sqlite3");
            File outputFile = new File(context.getDatabasePath(DATABASE_NAME).getPath());
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            copyFile(inputStream,outputStream);

            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Throwable exception) {
            throw new RuntimeException("The "+DATABASE_NAME+" database couldn't be installed.", exception);
        }
    }

    private void installOrUpdateIfNecessary() {
       // if (installedDatabaseIsOutdated()) {
        if (true) {
            context.deleteDatabase(DATABASE_NAME);
            installDatabaseFromAssets();
            writeDatabaseVersionInPreferences();
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        throw new RuntimeException("The "+DATABASE_NAME+" database is not writable.");
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        installOrUpdateIfNecessary();
        return super.getReadableDatabase();
    }

    private void copyFile(InputStream in, OutputStream out){
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1)
                    break;
                out.write(buffer, 0, bytesRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}