package com.yusufekremunlu.easyway.ui.main.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.FavouritesAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.FavouritesViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.ViewModelFactory;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    private FavouritesAdapter favouritesAdapter;
    RecyclerView favouritesRecyclerView;
    private FavouritesViewModel favouritesViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouritesViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication())).get(FavouritesViewModel.class);
        favouritesAdapter = new FavouritesAdapter(new ArrayList<>(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        favouritesRecyclerView = view.findViewById(R.id.favouritesRecyclerView);
        favouritesRecyclerView.setLayoutManager(gridLayoutManager);
        favouritesRecyclerView.setAdapter(favouritesAdapter);
        favouritesViewModel.getFavouriteMovies().observe(getViewLifecycleOwner(), favouriteMovies -> {
            favouritesAdapter.setFavouriteList(favouriteMovies);
        });

        return view;
    }
}


