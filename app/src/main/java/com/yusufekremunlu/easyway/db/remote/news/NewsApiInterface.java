package com.yusufekremunlu.easyway.db.remote.news;

import com.yusufekremunlu.easyway.model.network.news.NewsApiResponse;
import com.yusufekremunlu.easyway.utils.Credentials;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {
    @GET("/" + Credentials.NEWS_API_VERSION + "/everything")
    Call<NewsApiResponse> callEverything(
            @Query("q") String query
    );
    @GET("/" + Credentials.NEWS_API_VERSION + "/top-headlines")
    Call<NewsApiResponse> callHeadlines(
            @Query("pageSize") int pageSize,
            @Query("country") String country,
            @Query("category") String category
    );
}
