package com.yusufekremunlu.easyway.model.network.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubeItemModel;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubePageInfo;

import java.util.List;

public class YoutubeApiResponse {
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("regionCode")
    @Expose
    private String regionCode;
    @SerializedName("pageInfo")
    @Expose
    private YoutubePageInfo pageInfo;
    @SerializedName("items")
    @Expose
    private List<YoutubeItemModel> youtubeItemModelList;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public YoutubePageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(YoutubePageInfo youtubePageInfo) {
        this.pageInfo = youtubePageInfo;
    }

    public List<YoutubeItemModel> getYoutubeItemModelList() {
        return youtubeItemModelList;
    }

    public void setItems(List<YoutubeItemModel> youtubeItemModelList) {
        this.youtubeItemModelList = youtubeItemModelList;
    }
}
