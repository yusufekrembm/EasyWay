package com.yusufekremunlu.easyway.db.remote.movies;

import com.yusufekremunlu.easyway.model.entity.movies.MoviePerson;
import com.yusufekremunlu.easyway.model.network.movies.CreditsResponse;
import com.yusufekremunlu.easyway.model.network.movies.MoviePersonCreditsResponse;
import com.yusufekremunlu.easyway.model.network.movies.MoviePersonImagesResponse;
import com.yusufekremunlu.easyway.model.network.movies.MovieResponse;
import com.yusufekremunlu.easyway.model.network.movies.VideosResponse;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.util.List;

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
    @GET("/" + Credentials.MOVIE_API_VERSION + "/person/{person_id}")
    Call<MoviePerson> fetchPersonDetails(
            @Path("person_id") int person_id
    );
    @GET("/" + Credentials.MOVIE_API_VERSION + "/person/{person_id}/images")
    Call<MoviePersonImagesResponse> fetchPersonImages(
            @Path("person_id") int person_id
    );
    @GET("/" + Credentials.MOVIE_API_VERSION + "/person/{person_id}/movie_credits")
    Call<MoviePersonCreditsResponse> fetchPersonCredits(
            @Path("person_id") int person_id
    );
}
