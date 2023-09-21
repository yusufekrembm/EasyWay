package com.yusufekremunlu.easyway.model.entity.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeIdModel {
    @SerializedName("videoId")
    @Expose
    private String videoId;

    public String getVideoId() {
        return videoId;
    }
}