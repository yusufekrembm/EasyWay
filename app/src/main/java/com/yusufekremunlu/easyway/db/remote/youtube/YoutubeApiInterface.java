package com.yusufekremunlu.easyway.db.remote.youtube;

import com.yusufekremunlu.easyway.model.network.youtube.YoutubeApiResponse;
import com.yusufekremunlu.easyway.utils.Credentials;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApiInterface {
    @GET(Credentials.YOUTUBE_API_VERSION + "/search")
    Call<YoutubeApiResponse> callAllYoutubeVideos(
            @Query("q") String query
    );
}
