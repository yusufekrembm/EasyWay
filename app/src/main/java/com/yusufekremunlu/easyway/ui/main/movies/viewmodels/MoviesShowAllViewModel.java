package com.yusufekremunlu.easyway.ui.main.movies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yusufekremunlu.easyway.db.repository.movies.MovieRepository;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;

import java.util.List;

public class MoviesShowAllViewModel extends ViewModel {
    private MovieRepository movieRepository;

    public MoviesShowAllViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getDiscoverMovies() {
        return movieRepository.getDiscoverMovies();
    }
}
