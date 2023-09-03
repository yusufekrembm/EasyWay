package com.yusufekremunlu.easyway.model.network.movies;

import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import java.util.List;

public class CreditsResponse {
    @SerializedName("cast")
    private List<MovieCastModel> casts;
    public List<MovieCastModel> getCasts() {
        return casts;
    }
}
