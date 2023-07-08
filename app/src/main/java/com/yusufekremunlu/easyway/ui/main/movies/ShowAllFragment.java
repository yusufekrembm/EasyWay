package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.ShowAllAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesShowAllViewModel;
import java.util.ArrayList;

public class ShowAllFragment extends Fragment implements ShowAllAdapter.OnItemClickListener {
    private MoviesShowAllViewModel moviesShowAllViewModel;
    private RecyclerView showAllRecyclerView;
    private ShowAllAdapter showAllAdapter;
    private ProgressBar showAllProgressBar;
    private MovieApiClient movieApiClient = MovieApiClient.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesShowAllViewModel = new ViewModelProvider(this).get(MoviesShowAllViewModel.class);
        showAllAdapter = new ShowAllAdapter(new ArrayList<>(), getContext());
        showAllAdapter.setOnItemClickListener(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);
        showAllRecyclerView = view.findViewById(R.id.showAllRecyclerView);
        showAllProgressBar = view.findViewById(R.id.moviesProgressBar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        showAllRecyclerView.setLayoutManager(gridLayoutManager);
        showAllRecyclerView.setAdapter(showAllAdapter);
        showAllProgressBar.setVisibility(View.VISIBLE);

        loadNextPages();
        observeData();
        return view;
    }

    private void observeData() {
        moviesShowAllViewModel.getDiscoverMovies().observe(getViewLifecycleOwner(), movieModels -> {
            showAllAdapter.setDiscoverList(movieModels);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                }
            }, 500);
        });
    }

    private void loadNextPages() {
        showAllRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    movieApiClient.loadMoreDiscoverMovies();
                }
            }
        });
    }

    @Override
    public void onItemClick(MovieModel movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_showAllFragment_to_movieDetailsFragment, bundle);
    }
}

