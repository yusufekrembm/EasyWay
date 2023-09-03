package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import java.util.List;

public class MoviePersonCreditsResponse {
    @SerializedName("cast")
    private List<MoviePersonCredits> results;

    public List<MoviePersonCredits> getCredits() {
        return results;
    }
}
