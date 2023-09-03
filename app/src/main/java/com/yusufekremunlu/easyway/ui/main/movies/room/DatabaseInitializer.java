package com.yusufekremunlu.easyway.ui.main.movies.room;

import android.content.Context;

import androidx.room.Room;

public class DatabaseInitializer {
    private static MovieRoomDatabase movieDatabase;

    public static MovieRoomDatabase getInstance(Context context) {
        if (movieDatabase == null) {
            movieDatabase = Room.databaseBuilder(context, MovieRoomDatabase.class, "watch_lists")
                    .build();
        }
        return movieDatabase;
    }
}
