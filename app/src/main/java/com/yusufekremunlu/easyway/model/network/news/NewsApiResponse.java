package com.yusufekremunlu.easyway.model.network.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;
import java.util.List;

public class NewsApiResponse {
    @SerializedName("articles")
    @Expose
    private List<NewsHeadlines> articles;
    public List<NewsHeadlines> getArticles() {
        return articles;
    }
}