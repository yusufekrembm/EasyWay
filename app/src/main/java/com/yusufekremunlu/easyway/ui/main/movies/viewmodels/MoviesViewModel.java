package com.yusufekremunlu.easyway.ui.main.movies.viewmodels;

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
    public LiveData<List<MovieModel>> getDiscoverMovies() {
        return movieRepository.getDiscoverMovies();
    }
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }
    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
    public void searchNextPageTrending(){
        movieRepository.searchNextPageTrending();
    }
    public void searchNextPagePopular(){
        movieRepository.searchNextPagePopular();
    }
    public void searchNextPageUnComing(){
        movieRepository.searchNextPageUnComing();
    }
    public void searchNextPageDiscover(){
        movieRepository.searchNextPageDiscover();
    }
}



