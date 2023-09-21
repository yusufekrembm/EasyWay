package com.yusufekremunlu.easyway.model.network.movies;

import androidx.annotation.NonNull;

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

    public List<MovieModel> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieResponse{" +
                "total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", page=" + page +
                ", movies=" + movies +
                '}';
    }
}
