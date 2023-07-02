package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movie.Movies;

import java.util.List;


public class MoviesResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movies> movies;

    public MoviesResponse(int page, List<Movies> movies) {
        this.page = page;
        this.movies = movies;
    }

    public List<Movies> getMovies() {
        return movies;
    }
}
