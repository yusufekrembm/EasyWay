package com.yusufekremunlu.easyway.ui.main.movies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.db.repository.movies.MovieRepository;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import java.util.List;

public class MovieCastViewModel extends ViewModel {
    private final MovieRepository movieRepository;

    public MovieCastViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MoviePersonImages>> getPersonImagesModelMovies() {
        return movieRepository.getPersonImagesModelMovies();
    }

    public LiveData<List<MoviePersonCredits>> getPersonCreditsModelMovies() {
        return movieRepository.getPersonCreditsModelMovies();
    }
}
