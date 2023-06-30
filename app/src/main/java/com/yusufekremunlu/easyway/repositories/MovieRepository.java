package com.yusufekremunlu.easyway.repositories;

import com.yusufekremunlu.easyway.model.Movie;
import com.yusufekremunlu.easyway.services.MoviesResponse;
import com.yusufekremunlu.easyway.services.TheMovieDataBaseAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static MovieRepository instance;
    private final TheMovieDataBaseAPI.MovieService movieService;

    private MovieRepository() {
        movieService = TheMovieDataBaseAPI.getRetrofit().create(TheMovieDataBaseAPI.MovieService.class);
    }

    public static synchronized MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    public void getPopularMovies(int page, final MovieCallback<List<Movie>> callback) {
        Call<MoviesResponse> call = movieService.fetchPopularList(page);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        List<Movie> movies = moviesResponse.getResults();
                        callback.onSuccess(movies);
                    } else {
                        callback.onError("Movies response is null");
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }
}
