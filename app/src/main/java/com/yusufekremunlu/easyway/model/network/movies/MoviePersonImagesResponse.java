package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import java.util.List;

public class MoviePersonImagesResponse {
    @SerializedName("profiles")
    private List<MoviePersonImages> results;
    public List<MoviePersonImages> getImages() {
        return results;
    }
}
