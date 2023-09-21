package com.yusufekremunlu.easyway.model.network.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubeItemModel;

import java.util.List;

public class YoutubeApiResponse {
    @SerializedName("items")
    @Expose
    private List<YoutubeItemModel> youtubeItemModelList;

    public List<YoutubeItemModel> getYoutubeItemModelList() {
        return youtubeItemModelList;
    }

}
