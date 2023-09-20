package com.yusufekremunlu.easyway.ui.main.youtube;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.ui.main.youtube.adapters.YoutubeAdapter;
import com.yusufekremunlu.easyway.ui.main.youtube.viewmodels.YoutubeViewModel;
import java.util.ArrayList;

public class YoutubeFragment extends Fragment {
    private YoutubeViewModel youtubeViewModel;
    private YoutubeAdapter youtubeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        youtubeViewModel = new ViewModelProvider(this).get(YoutubeViewModel.class);
        youtubeAdapter = new YoutubeAdapter(getContext(), new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        RecyclerView youtubeRecyclerView = view.findViewById(R.id.recyclerYoutubeVideos);
        GridLayoutManager newsLayoutManager = new GridLayoutManager(requireContext(), 1);
        youtubeRecyclerView.setLayoutManager(newsLayoutManager);
        youtubeRecyclerView.setAdapter(youtubeAdapter);


        SearchView searchViewYoutube = view.findViewById(R.id.search_view_youtube);
        searchViewYoutube.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                youtubeViewModel.setQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        observeData();
        return view;
    }

    private void observeData() {
        youtubeViewModel.getYoutubeVideos().observe(getViewLifecycleOwner(), youtubeItemModelList -> {
            youtubeAdapter.updateData(youtubeItemModelList);
        });
    }
}