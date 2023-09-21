package com.yusufekremunlu.easyway.db.repository.news;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.yusufekremunlu.easyway.db.remote.news.NewsApiInterface;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;
import com.yusufekremunlu.easyway.model.network.news.NewsApiResponse;
import com.yusufekremunlu.easyway.utils.builders.NewsRetrofitBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    static NewsApiInterface newsApiInterface = NewsRetrofitBuilder.buildService(NewsApiInterface.class);
    private static NewsRepository instance;
    private final MutableLiveData<List<NewsHeadlines>> mTopHeadNews;
    private final MutableLiveData<List<NewsHeadlines>> mEverythingNews;

    public static NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepository();
        }
        return instance;
    }

    private NewsRepository() {
        mTopHeadNews = new MutableLiveData<>();
        mEverythingNews = new MutableLiveData<>();
    }

    public MutableLiveData<List<NewsHeadlines>> getTopHeadNewsFromApi(String country, String category) {
        Call<NewsApiResponse> newsApiResponseCall = newsApiInterface.callHeadlines(100, country, category);
        newsApiResponseCall.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful()) {
                    NewsApiResponse newsApiResponse = response.body();
                    if (newsApiResponse != null) {
                        List<NewsHeadlines> newsHeadlinesModels = newsApiResponse.getArticles();
                        mTopHeadNews.postValue(newsHeadlinesModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {

            }
        });
        return mTopHeadNews;
    }

    public MutableLiveData<List<NewsHeadlines>> getEverythingNewsFromApi(String query) {
        Call<NewsApiResponse> newsApiResponseCall = newsApiInterface.callEverything(query);
        newsApiResponseCall.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful()) {
                    NewsApiResponse newsApiResponse = response.body();
                    if (newsApiResponse != null) {
                        List<NewsHeadlines> newsHeadlinesModels = newsApiResponse.getArticles();
                        mEverythingNews.postValue(newsHeadlinesModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {

            }
        });
        return mEverythingNews;
    }
}
