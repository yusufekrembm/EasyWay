package com.yusufekremunlu.easyway.ui.main.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.db.repository.movies.MovieRepository;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import java.util.List;

public class MoviesViewModel extends ViewModel {
    private MovieRepository movieRepository;

    public MoviesViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getTrendingMovies() {
        return movieRepository.getTrendingMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies() {
        return movieRepository.getPopularMovies();
    }
    public LiveData<List<MovieModel>> getUpComingMovies() {
        return movieRepository.getUpComingMovies();
    }
}



