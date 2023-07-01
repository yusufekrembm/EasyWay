package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.adapters.MoviesAdapter;
import com.yusufekremunlu.easyway.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private MoviesViewModel movieViewModel;
    private MoviesAdapter popularMoviesAdapter;
    private MoviesAdapter upcomingMoviesAdapter;
    private MoviesAdapter nowPlayingMoviesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        movieViewModel.loadPopularMovies(1);
        movieViewModel.loadUpComingMovies(1);
        movieViewModel.loadInTheatersMovies(1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        RecyclerView popularRecyclerView = view.findViewById(R.id.popularRecyclerView);
        RecyclerView upcomingRecyclerView = view.findViewById(R.id.upcomingRecyclerView);
        RecyclerView nowPlayingRecyclerView = view.findViewById(R.id.inTheatersRecyclerView);

        popularMoviesAdapter = new MoviesAdapter(new ArrayList<>(), requireContext());
        upcomingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), requireContext());
        nowPlayingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), requireContext());

        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView.setLayoutManager(popularLayoutManager);
        popularRecyclerView.setAdapter(popularMoviesAdapter);

        LinearLayoutManager upcomingLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        upcomingRecyclerView.setLayoutManager(upcomingLayoutManager);
        upcomingRecyclerView.setAdapter(upcomingMoviesAdapter);

        LinearLayoutManager nowPlayingLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        nowPlayingRecyclerView.setLayoutManager(nowPlayingLayoutManager);
        nowPlayingRecyclerView.setAdapter(nowPlayingMoviesAdapter);

        popularRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    movieViewModel.loadMorePopularMovies();
                }
            }
        });

        upcomingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    movieViewModel.loadMoreUpComingMovies();
                }
            }
        });

        nowPlayingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    movieViewModel.loadMoreInTheaters();
                }
            }
        });

        // Observe LiveData for popular movies
        movieViewModel.getPopularMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                popularMoviesAdapter.setMovieList(movies);
                popularMoviesAdapter.notifyDataSetChanged();
            }
        });

        // Observe LiveData for upcoming movies
        movieViewModel.getUpComingMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                upcomingMoviesAdapter.setMovieList(movies);
                upcomingMoviesAdapter.notifyDataSetChanged();
            }
        });

        // Observe LiveData for now playing movies
        movieViewModel.getInTheatersMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                nowPlayingMoviesAdapter.setMovieList(movies);
                nowPlayingMoviesAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
