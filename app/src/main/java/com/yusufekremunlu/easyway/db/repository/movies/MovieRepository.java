package com.yusufekremunlu.easyway.db.repository.movies;

import androidx.lifecycle.LiveData;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import java.util.List;

public class MovieRepository {
    private static MovieRepository instance;
    private MovieApiClient movieApiClient;

    public static MovieRepository getInstance(){
        if(instance==null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getTrendingMovies(){
        return movieApiClient.getTrendingMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieApiClient.getPopularMovies();
    }
    public LiveData<List<MovieModel>> getUpComingMovies(){
        return movieApiClient.getUpComingMovies();
    }
}

