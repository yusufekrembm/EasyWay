package com.yusufekremunlu.easyway.model.entity.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieVideoModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    public MovieVideoModel(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    protected MovieVideoModel(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieVideoModel> CREATOR = new Creator<MovieVideoModel>() {
        @Override
        public MovieVideoModel createFromParcel(Parcel in) {
            return new MovieVideoModel(in);
        }

        @Override
        public MovieVideoModel[] newArray(int size) {
            return new MovieVideoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
