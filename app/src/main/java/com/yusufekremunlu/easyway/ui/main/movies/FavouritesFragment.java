package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.FavouritesAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.FavouritesViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.ViewModelFactory;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.OnItemClickListener {
    private FavouritesAdapter favouritesAdapter;
    RecyclerView favouritesRecyclerView;
    private FavouritesViewModel favouritesViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouritesViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication())).get(FavouritesViewModel.class);
        favouritesAdapter = new FavouritesAdapter(new ArrayList<>(), getContext());
        favouritesAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        favouritesRecyclerView = view.findViewById(R.id.favouritesRecyclerView);
        favouritesRecyclerView.setLayoutManager(gridLayoutManager);
        favouritesRecyclerView.setAdapter(favouritesAdapter);

        TextView noResultTextView = view.findViewById(R.id.noResultsTextView);

        favouritesViewModel.getFavouriteMovies().observe(getViewLifecycleOwner(), favouriteMovies -> {
            if (favouriteMovies.isEmpty()) {
                noResultTextView.setVisibility(View.VISIBLE);
                favouritesRecyclerView.setVisibility(View.GONE);
            } else {
                noResultTextView.setVisibility(View.GONE);
                favouritesRecyclerView.setVisibility(View.VISIBLE);
                favouritesAdapter.setFavouriteList(favouriteMovies);
            }
        });

        return view;
    }


    @Override
    public void onItemClick(MovieFav moviefav) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", moviefav);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_favouritesFragment_to_favouritesDetailFragment, bundle);
    }
}


