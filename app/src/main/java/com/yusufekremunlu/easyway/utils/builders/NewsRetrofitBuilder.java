package com.yusufekremunlu.easyway.utils.builders;

import androidx.annotation.NonNull;

import com.yusufekremunlu.easyway.utils.Credentials;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofitBuilder {
    private static final OkHttpClient okHttp = new OkHttpClient.Builder().addInterceptor(new RequestInterceptor()).build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Credentials.NEW_BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build();

    public static <T> T buildService(Class<T> serviceType) {
        return retrofit.create(serviceType);
    }

    private static class RequestInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            okhttp3.HttpUrl url = oldRequest.url().newBuilder()
                    .addQueryParameter("apiKey", Credentials.NEWS_API_KEY)
                    .build();
            Request newRequest = oldRequest.newBuilder().url(url).build();
            return chain.proceed(newRequest);
        }
    }
}

