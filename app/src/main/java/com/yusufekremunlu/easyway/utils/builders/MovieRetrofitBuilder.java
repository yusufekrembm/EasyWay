package com.yusufekremunlu.easyway.utils.builders;

import com.yusufekremunlu.easyway.db.remote.movies.MovieApiInterface;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Response;

public class MovieRetrofitBuilder {
    private static OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
            .addInterceptor(new RequestInterceptor());

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Credentials.MOVIE_BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApiInterface movieApiInterface = retrofit.create(MovieApiInterface.class);

    public static MovieApiInterface getMovieApiInterface() {
        return movieApiInterface;
    }

    private static class RequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            HttpUrl url = oldRequest.url().newBuilder()
                    .addQueryParameter("language", "en-US")
                    .addQueryParameter("api_key", Credentials.MOVIE_API_KEY)
                    .build();
            Request newRequest = oldRequest.newBuilder().url(url).build();
            return chain.proceed(newRequest);
        }
    }
}
