package com.yusufekremunlu.easyway.ui.main.home.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;
import com.yusufekremunlu.easyway.db.repository.news.NewsRepository;
import java.util.List;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<List<NewsHeadlines>> mTopHeadNews;
    private MutableLiveData<List<NewsHeadlines>> mEverythingNews;
    private String selectedCountry = "us";
    private String selectedCategory = "business";
    private String mQuery;

    public LiveData<List<NewsHeadlines>> getTopHeadNews() {
        if (mTopHeadNews == null) {
            mTopHeadNews = NewsRepository.getInstance().getTopHeadNewsFromApi(selectedCountry, selectedCategory);
        }
        return mTopHeadNews;
    }
    public LiveData<List<NewsHeadlines>> getEverythingNews() {
        if (mEverythingNews == null) {
            mEverythingNews = NewsRepository.getInstance().getEverythingNewsFromApi(mQuery);
        }
        return mEverythingNews;
    }

    public void setCountry(String country) {
        selectedCountry = country;
        mTopHeadNews = NewsRepository.getInstance().getTopHeadNewsFromApi(selectedCountry, selectedCategory);
    }

    public void setCategory(String category) {
        selectedCategory = category;
        mTopHeadNews = NewsRepository.getInstance().getTopHeadNewsFromApi(selectedCountry, selectedCategory);
    }
    public void setQuery(String query) {
        mQuery = query;
        mTopHeadNews = NewsRepository.getInstance().getEverythingNewsFromApi(mQuery);
    }
}
