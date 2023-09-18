package com.yusufekremunlu.easyway.ui.main.movies.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import com.yusufekremunlu.easyway.ui.main.movies.room.MovieDao;
import com.yusufekremunlu.easyway.ui.main.movies.room.MovieRoomDatabase;
import java.util.List;

public class FavouritesViewModel extends ViewModel {
    private final LiveData<List<MovieFav>> favouriteMovies;
    private final MovieDao movieDao;

    public FavouritesViewModel(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        movieDao = db.movieDao();
        favouriteMovies = movieDao.getAllFavMovies();
    }

    public LiveData<List<MovieFav>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void insertFavMovie(MovieFav movie) {
        new InsertFavMovieAsyncTask(movieDao).execute(movie);
    }

    public void deleteFavMovie(MovieFav movie) {
        new DeleteFavMovieAsyncTask(movieDao).execute(movie);
    }

    private static class InsertFavMovieAsyncTask extends AsyncTask<MovieFav, Void, Void> {
        private final MovieDao movieDao;

        InsertFavMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(MovieFav... movieFavs) {
            movieDao.insert(movieFavs[0]);
            return null;
        }
    }

    private static class DeleteFavMovieAsyncTask extends AsyncTask<MovieFav, Void, Void> {
        private final MovieDao movieDao;

        DeleteFavMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(MovieFav... movieFavs) {
            movieDao.deleteFavMovie(movieFavs[0]);
            return null;
        }
    }
}

