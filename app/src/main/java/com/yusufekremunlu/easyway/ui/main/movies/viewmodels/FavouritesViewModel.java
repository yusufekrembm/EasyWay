package com.yusufekremunlu.easyway.ui.main.movies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;

import java.util.List;

public class FavouritesViewModel extends ViewModel {
        private final MutableLiveData<List<MovieModel>> selectedMovie = new MutableLiveData<>();

        public void setSelectedMovie(List<MovieModel> movie) {
            selectedMovie.setValue(movie);
        }

        public LiveData<List<MovieModel>> getSelectedMovie() {
            return selectedMovie;
        }
}
