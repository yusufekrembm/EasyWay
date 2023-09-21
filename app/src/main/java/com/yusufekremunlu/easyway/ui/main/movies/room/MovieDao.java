package com.yusufekremunlu.easyway.ui.main.movies.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import java.util.List;


@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
    LiveData<List<MovieFav>> getAllFavMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieFav favMovie);

    @Delete
    void deleteFavMovie(MovieFav favMovie);
}

