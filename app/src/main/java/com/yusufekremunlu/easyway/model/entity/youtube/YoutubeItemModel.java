package com.yusufekremunlu.easyway.model.entity.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeItemModel {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("id")
    @Expose
    private YoutubeIdModel id;
    @SerializedName("snippet")
    @Expose
    private YoutubeSnippet snippet;

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

    public YoutubeIdModel getId() {
        return id;
    }

    public void setId(YoutubeIdModel id) {
        this.id = id;
    }

    public YoutubeSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YoutubeSnippet snippet) {
        this.snippet = snippet;
    }

}