package com.majinnaibu.monstercards;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.room.Room;

import com.majinnaibu.monstercards.data.MonsterRepository;

public class MonsterCardsApplication extends Application {

    private AppDatabase m_db;
    private MonsterRepository m_monsterLibraryRepository;

    public MonsterRepository getMonsterRepository() {
        return m_monsterLibraryRepository;
    }

    public static MonsterCardsApplication getInstance(Context context) {
        return (MonsterCardsApplication) context.getApplicationContext();
    }

    public MonsterCardsApplication() {
    }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        m_db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "monsters").build();
        m_monsterLibraryRepository = new MonsterRepository(m_db);
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}