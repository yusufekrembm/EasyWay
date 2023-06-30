package com.yusufekremunlu.easyway.services;

import com.yusufekremunlu.easyway.model.Movie;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TheMovieDataBaseAPI {
    private static Retrofit retrofit;
    private static final int API_VERSION = 3;
    public static final String API_KEY = "d7289dbb6944cf037226fd769dc03924";
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/w185";
    private static final String BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w780";
    private static final String BASE_PROFILE_URL = "https://image.tmdb.org/t/p/w185";
    private static final String BASE_YT_IMG_URL = "https://img.youtube.com/vi/";
    private static final String BASE_YT_WATCH_URL = "https://www.youtube.com/watch?v=";
    public static final String BASE_API_URL = "https://api.themoviedb.org/";
    public static final float MAX_RATING = 10f;

    public static String getPosterUrl(String path) {
        return BASE_POSTER_URL + path;
    }

    public static String getBackdropUrl(String path) {
        return BASE_BACKDROP_URL + path;
    }

    public static String getProfileUrl(String path) {
        return BASE_PROFILE_URL + path;
    }

    public static String getYoutubeImageUrl(String youtubeId) {
        return BASE_YT_IMG_URL + youtubeId + "/hqdefault.jpg";
    }

    public static String getYoutubeWatchUrl(String youtubeId) {
        return BASE_YT_WATCH_URL + youtubeId;
    }

    public static String getRuntimeDateFormat() {
        return "yyyy-MM-dd";
    }

    public interface MovieService {
        @GET("/" + API_VERSION + "/movie/popular")
        Call<MoviesResponse> fetchPopularList(@Query("page") int page);

        @GET("/" + API_VERSION + "/movie/upcoming")
        Call<MoviesResponse> fetchUpcomingList(@Query("page") int page);

        @GET("/" + API_VERSION + "/movie/now_playing")
        Call<MoviesResponse> fetchInTheatersList(@Query("page") int page);

        @GET("/" + API_VERSION + "/discover/movie")
        Call<MoviesResponse> fetchDiscoverList(@Query("page") int page);

        @GET("/" + API_VERSION + "/movie/{id}")
        Call<Movie> fetchDetails(@Path("id") int id);
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                HttpUrl.Builder urlBuilder = originalRequest.url().newBuilder();
                urlBuilder.addQueryParameter("api_key", API_KEY); // API anahtarınızı buraya ekleyin
                Request newRequest = originalRequest.newBuilder().url(urlBuilder.build()).build();
                return chain.proceed(newRequest);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}

