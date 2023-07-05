package com.yusufekremunlu.easyway.db.remote.movies;

import com.yusufekremunlu.easyway.model.network.movies.CreditsResponse;
import com.yusufekremunlu.easyway.model.network.movies.MovieResponse;
import com.yusufekremunlu.easyway.model.network.movies.VideosResponse;
import com.yusufekremunlu.easyway.utils.Credentials;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {
    @GET("/" + Credentials.MOVIE_API_VERSION + "/trending/movie/day")
    Call<MovieResponse> fetchTrendingMovies(
            @Query("page") int page
    );

    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/popular")
    Call<MovieResponse> fetchPopularMovies(
            @Query("page") int page
    );
    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/upcoming")
    Call<MovieResponse> fetchUpComingMovies(
            @Query("page") int page
    );
    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/{movie_id}/credits")
    Call<CreditsResponse> fetchMovieCasts(
            @Path("movie_id") int movie_id
    );
    @GET("/" + Credentials.MOVIE_API_VERSION + "/movie/{movie_id}/videos")
    Call<VideosResponse> fetchMovieVideos(
            @Path("movie_id") int movie_id
    );
}
