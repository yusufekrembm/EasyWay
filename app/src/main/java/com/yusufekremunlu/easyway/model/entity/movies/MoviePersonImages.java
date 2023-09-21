package com.yusufekremunlu.easyway.model.entity.movies;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class MoviePersonImages implements Parcelable {
    @SerializedName("file_path")
    private String file_path;

    protected MoviePersonImages(Parcel in) {
        file_path = in.readString();
    }

    public static final Creator<MoviePersonImages> CREATOR = new Creator<MoviePersonImages>() {
        @Override
        public MoviePersonImages createFromParcel(Parcel in) {
            return new MoviePersonImages(in);
        }

        @Override
        public MoviePersonImages[] newArray(int size) {
            return new MoviePersonImages[size];
        }
    };

    public String getFile_path() {
        return file_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(file_path);
    }
}
