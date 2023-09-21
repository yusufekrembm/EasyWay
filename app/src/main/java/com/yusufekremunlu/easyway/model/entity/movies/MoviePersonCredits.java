package com.yusufekremunlu.easyway.model.entity.movies;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class MoviePersonCredits implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("original_title")
    private String original_title;

    protected MoviePersonCredits(Parcel in) {
        id = in.readInt();
        mediaType = in.readString();
        poster_path = in.readString();
        original_title = in.readString();
    }

    public static final Creator<MoviePersonCredits> CREATOR = new Creator<MoviePersonCredits>() {
        @Override
        public MoviePersonCredits createFromParcel(Parcel in) {
            return new MoviePersonCredits(in);
        }

        @Override
        public MoviePersonCredits[] newArray(int size) {
            return new MoviePersonCredits[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mediaType);
        dest.writeString(poster_path);
        dest.writeString(original_title);
    }
}
