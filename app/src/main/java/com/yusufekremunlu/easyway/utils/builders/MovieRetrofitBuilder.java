package com.yusufekremunlu.easyway.utils.builders;

import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Response;


public class MovieRetrofitBuilder {
    private static OkHttpClient okHttp = new OkHttpClient.Builder().addInterceptor(new RequestInterceptor()).build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Credentials.MOVIE_BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build();

    public static <T> T buildService(Class<T> serviceType) {
        return retrofit.create(serviceType);
    }

    private static class RequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            okhttp3.HttpUrl url = oldRequest.url().newBuilder()
                    .addQueryParameter("language", "en-US")
                    .addQueryParameter("api_key", Credentials.MOVIE_API_KEY)
                    .build();
            Request newRequest = oldRequest.newBuilder().url(url).build();
            return chain.proceed(newRequest);
        }
    }
}

