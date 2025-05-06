package com.davidfreemangames.idleindustrialist;

import android.app.Application;

import androidx.room.Room;

import data.MainDatabase;

public class MainApplication extends Application {
    private MainFactory mainFactory;
    private MainDatabase.AppDatabase database;

    @Override
    public void onCreate(){
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                        MainDatabase.AppDatabase.class, "idle_database")
                .fallbackToDestructiveMigration()
                .build();
        mainFactory = MainFactory.getInstance();
        mainFactory.initializeDatabase(getApplicationContext());
        mainFactory.initializeUserData(); // Load saved data on app start
    }

    public MainFactory getMainFactory() {return mainFactory;}

    public MainDatabase.AppDatabase getDatabase() {
        return database;
    }
}
