package com.yusufekremunlu.easyway.model.entity.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeThumbnails {
    @SerializedName("default")
    @Expose
    private YoutubeDefault _default;
    @SerializedName("medium")
    @Expose
    private YoutubeMedium medium;
    @SerializedName("high")
    @Expose
    private YoutubeHigh high;

    public YoutubeDefault getDefault() {
        return _default;
    }

    public void setDefault(YoutubeDefault _default) {
        this._default = _default;
    }

    public YoutubeMedium getMedium() {
        return medium;
    }

    public void setMedium(YoutubeMedium medium) {
        this.medium = medium;
    }

    public YoutubeHigh getHigh() {
        return high;
    }

    public void setHigh(YoutubeHigh high) {
        this.high = high;
    }

}