package com.yusufekremunlu.easyway.db.repository.movies;

import androidx.lifecycle.LiveData;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import com.yusufekremunlu.easyway.model.entity.movies.MovieVideoModel;

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
    public LiveData<List<MovieCastModel>> getCastModelMovies(){
        return movieApiClient.getCastModelMovies();
    }
    public LiveData<List<MovieVideoModel>> getVideoModelMovies(){
        return movieApiClient.getVideoModelMovies();
    }
    public LiveData<List<MoviePersonImages>> getPersonImagesModelMovies(){
        return movieApiClient.getPersonImagesModelMovies();
    }
    public LiveData<List<MoviePersonCredits>> getPersonCreditsModelMovies(){
        return movieApiClient.getPersonCreditsModelMovies();
    }
}

