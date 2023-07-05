package com.yusufekremunlu.easyway.model.entity.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieCastModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profile_path;

    public MovieCastModel(int id, String name, String profile_path) {
        this.id = id;
        this.name = name;
        this.profile_path = profile_path;
    }

    protected MovieCastModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        profile_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profile_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieCastModel> CREATOR = new Creator<MovieCastModel>() {
        @Override
        public MovieCastModel createFromParcel(Parcel in) {
            return new MovieCastModel(in);
        }

        @Override
        public MovieCastModel[] newArray(int size) {
            return new MovieCastModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
