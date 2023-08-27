package com.yusufekremunlu.easyway.model.entity.movies;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class MovieFav {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "backdrop_path")
    public String backdrop_path;

    public MovieFav(int uid, String title, String backdrop_path) {
        this.uid = uid;
        this.title = title;
        this.backdrop_path = backdrop_path;
    }

}

