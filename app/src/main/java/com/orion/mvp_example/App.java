package com.orion.mvp_example;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.orion.mvp_example.database.DatabaseHelper;

/**
 * Created by orion on 2.4.17.
 */

public class App extends Application {
    private static DatabaseHelper mDatabase;

    @Override
    public void onCreate() {

        mDatabase = new DatabaseHelper(this);
        SQLiteDatabase database = mDatabase.getWritableDatabase();
        database.close();

        super.onCreate();
    }

    public static DatabaseHelper getDatabase() {
        return mDatabase;
    }
}
