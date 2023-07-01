package com.yusufekremunlu.easyway.services;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.Movie;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> results;

    public MoviesResponse(int page, List<Movie> results) {
        this.page = page;
        this.results = results;
    }


    public List<Movie> getResults() {
        return results;
    }
}
