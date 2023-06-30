package com.yusufekremunlu.easyway.ui.main.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.model.Movie;
import com.yusufekremunlu.easyway.repositories.MovieCallback;
import com.yusufekremunlu.easyway.repositories.MovieRepository;

import java.util.List;

public class MoviesViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> popularMoviesLiveData;
    private MutableLiveData<List<Movie>> upcomingMoviesLiveData;
    private MutableLiveData<String> errorLiveData;

    public MoviesViewModel() {
        movieRepository = MovieRepository.getInstance();
        popularMoviesLiveData = new MutableLiveData<>();
        upcomingMoviesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }

    public LiveData<List<Movie>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadPopularMovies(int page) {
        movieRepository.getPopularMovies(page, new MovieCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                popularMoviesLiveData.setValue(movies);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }
}
