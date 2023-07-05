package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movies.MovieVideoModel;

import java.util.List;

public class VideosResponse {
    @SerializedName("results")
    private List<MovieVideoModel> videos;
    public List<MovieVideoModel> getVideos() {
        return videos;
    }
}
