package com.yusufekremunlu.easyway.db.repository.youtube;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.yusufekremunlu.easyway.db.remote.youtube.YoutubeApiInterface;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubeItemModel;
import com.yusufekremunlu.easyway.model.network.youtube.YoutubeApiResponse;
import com.yusufekremunlu.easyway.utils.builders.YoutubeRetrofitBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeRepository {
    static YoutubeApiInterface youtubeApiInterface = YoutubeRetrofitBuilder.buildService(YoutubeApiInterface.class);
    private static YoutubeRepository instance;
    private final MutableLiveData<List<YoutubeItemModel>> mYoutubeVideos;

    public static YoutubeRepository getInstance() {
        if (instance == null) {
            instance = new YoutubeRepository();
        }
        return instance;
    }

    private YoutubeRepository() {
        mYoutubeVideos = new MutableLiveData<>();
    }

    public MutableLiveData<List<YoutubeItemModel>> getAllYoutubeVideosFromApi(String query) {
        Call<YoutubeApiResponse> youtubeApiResponseCall = youtubeApiInterface.callAllYoutubeVideos(query);
        youtubeApiResponseCall.enqueue(new Callback<YoutubeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeApiResponse> call, @NonNull Response<YoutubeApiResponse> response) {
                if (response.isSuccessful()) {
                    YoutubeApiResponse youtubeApiResponse = response.body();
                    if (youtubeApiResponse != null) {
                        List<YoutubeItemModel> youtubeItemModelList = youtubeApiResponse.getYoutubeItemModelList();
                        mYoutubeVideos.postValue(youtubeItemModelList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeApiResponse> call, @NonNull Throwable t) {

            }
        });
        return mYoutubeVideos;
    }
}

