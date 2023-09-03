package com.yusufekremunlu.easyway.model.entity.movies;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MoviePerson implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profile_path;
    @SerializedName("known_for_department")
    private String known_for_department;
    @SerializedName("biography")
    private String biography;

    public MoviePerson(int id, String name, String profile_path, String known_for_department, String biography) {
        this.id = id;
        this.name = name;
        this.profile_path = profile_path;
        this.known_for_department = known_for_department;
        this.biography = biography;
    }

    protected MoviePerson(Parcel in) {
        id = in.readInt();
        name = in.readString();
        profile_path = in.readString();
        known_for_department = in.readString();
        biography = in.readString();
    }

    public static final Creator<MoviePerson> CREATOR = new Creator<MoviePerson>() {
        @Override
        public MoviePerson createFromParcel(Parcel in) {
            return new MoviePerson(in);
        }

        @Override
        public MoviePerson[] newArray(int size) {
            return new MoviePerson[size];
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

    public String getKnown_for_department() {
        return known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        this.known_for_department = known_for_department;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profile_path);
        dest.writeString(known_for_department);
        dest.writeString(biography);
    }
}