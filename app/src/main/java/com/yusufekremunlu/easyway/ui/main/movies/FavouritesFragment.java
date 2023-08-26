package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.FavouritesAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.FavouritesViewModel;
import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    private FavouritesViewModel sharedViewModel;
    private FavouritesAdapter favouritesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
        favouritesAdapter = new FavouritesAdapter(new ArrayList<>(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        RecyclerView favoriteMoviesRecycler = view.findViewById(R.id.favouritesRecyclerView);
        LinearLayoutManager layoutCast = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteMoviesRecycler.setLayoutManager(layoutCast);
        favoriteMoviesRecycler.setAdapter(favouritesAdapter);
        sharedViewModel.getSelectedMovie().observe(getViewLifecycleOwner(), movie -> {
            favouritesAdapter.setFavouriteList(movie);
        });


        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }
}


