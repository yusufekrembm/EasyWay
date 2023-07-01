package com.yusufekremunlu.easyway.services;

import com.yusufekremunlu.easyway.model.Movie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TheMovieDataBaseAPI {
    private static Retrofit retrofit;
    private static final int API_VERSION = 3;
    public static final String API_KEY = "d7289dbb6944cf037226fd769dc03924";;
    public static final String BASE_API_URL = "https://api.themoviedb.org/";

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
                urlBuilder.addQueryParameter("api_key", API_KEY);
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

