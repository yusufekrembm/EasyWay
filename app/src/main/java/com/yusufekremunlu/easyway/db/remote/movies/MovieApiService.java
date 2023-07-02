package com.yusufekremunlu.easyway.db.remote.movies;

import com.yusufekremunlu.easyway.model.entity.movie.Movies;
import com.yusufekremunlu.easyway.model.network.movies.MoviesResponse;
import com.yusufekremunlu.easyway.utils.Credentials;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/popular")
    Call<MoviesResponse> fetchPopularList(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/upcoming")
    Call<MoviesResponse> fetchUpcomingList(@Query("page") int page);

    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/now_playing")
    Call<MoviesResponse> fetchInTheatersList(@Query("page") int page);

    @GET("/" + Credentials.MOVIE_API_VERSION + "/discover/movie")
    Call<MoviesResponse> fetchDiscoverList(@Query("page") int page);

    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/{id}")
    Call<Movies> fetchDetails(@Path("id") int id);
}
