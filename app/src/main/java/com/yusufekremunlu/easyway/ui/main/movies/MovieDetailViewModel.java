package com.yusufekremunlu.easyway.ui.main.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.db.repository.movies.MovieRepository;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieVideoModel;

import java.util.List;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository movieRepository;

    public MovieDetailViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieCastModel>> getCastModelMovies() {
        return movieRepository.getCastModelMovies();
    }
    public LiveData<List<MovieVideoModel>> getVideoModelMovies() {
        return movieRepository.getVideoModelMovies();
    }
}
