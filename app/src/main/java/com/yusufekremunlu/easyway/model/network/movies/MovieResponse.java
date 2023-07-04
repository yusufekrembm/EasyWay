package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;

import java.util.List;

public class MovieResponse {
    @SerializedName("total_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieModel> movies;

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getPage() {
        return page;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }
}
