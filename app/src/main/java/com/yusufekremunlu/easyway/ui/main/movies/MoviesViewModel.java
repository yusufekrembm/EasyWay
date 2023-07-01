package com.yusufekremunlu.easyway.ui.main.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.model.Movie;
import com.yusufekremunlu.easyway.repositories.MovieCallback;
import com.yusufekremunlu.easyway.repositories.MovieRepository;
import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> popularMoviesLiveData;
    private MutableLiveData<List<Movie>> upComingMoviesLiveData;
    private MutableLiveData<List<Movie>> inTheatersMoviesLiveData;
    private MutableLiveData<String> errorLiveData;
    private int currentPage = 1;

    public MoviesViewModel() {
        movieRepository = MovieRepository.getInstance();
        popularMoviesLiveData = new MutableLiveData<>();
        upComingMoviesLiveData = new MutableLiveData<>();
        inTheatersMoviesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }
    public LiveData<List<Movie>> getUpComingMoviesLiveData() {
        return upComingMoviesLiveData;
    }
    public LiveData<List<Movie>> getInTheatersMoviesLiveData() {
        return inTheatersMoviesLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadPopularMovies(int page) {
        movieRepository.getPopularMovies(page, new MovieCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = popularMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                currentMovies.addAll(movies);
                popularMoviesLiveData.setValue(currentMovies);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void loadMorePopularMovies() {
        currentPage++;
        loadPopularMovies(currentPage);
    }

    public void loadUpComingMovies(int page) {
        movieRepository.getUpComingMovies(page, new MovieCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = upComingMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                currentMovies.addAll(movies);
                upComingMoviesLiveData.setValue(currentMovies);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void loadMoreUpComingMovies() {
        currentPage++;
        loadUpComingMovies(currentPage);
    }
    public void loadInTheatersMovies(int page) {
        movieRepository.getInTheatersMovies(page, new MovieCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = inTheatersMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                currentMovies.addAll(movies);
                inTheatersMoviesLiveData.setValue(currentMovies);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }


    public void loadMoreInTheaters() {
        currentPage++;
        loadInTheatersMovies(currentPage);
    }
}
