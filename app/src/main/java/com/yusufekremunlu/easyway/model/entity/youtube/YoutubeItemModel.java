package com.yusufekremunlu.easyway.model.entity.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeItemModel {
    @SerializedName("id")
    @Expose
    private YoutubeIdModel id;
    
    public YoutubeIdModel getId() {
        return id;
    }
    public void setId(YoutubeIdModel id) {
        this.id = id;
    }
}