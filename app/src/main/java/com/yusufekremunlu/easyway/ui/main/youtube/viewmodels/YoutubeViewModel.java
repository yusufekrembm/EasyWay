package com.yusufekremunlu.easyway.ui.main.youtube.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yusufekremunlu.easyway.db.repository.youtube.YoutubeRepository;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubeItemModel;
import java.util.List;

public class YoutubeViewModel extends ViewModel {
    private MutableLiveData<List<YoutubeItemModel>> mYoutubeVideos;
    private String mQuery;

    public LiveData<List<YoutubeItemModel>> getYoutubeVideos() {
        if (mYoutubeVideos == null) {
            mYoutubeVideos = YoutubeRepository.getInstance().getAllYoutubeVideosFromApi(mQuery);
        }
        return mYoutubeVideos;
    }
    public void setQuery(String query) {
        mQuery = query;
        mYoutubeVideos = YoutubeRepository.getInstance().getAllYoutubeVideosFromApi(mQuery);
    }
}
