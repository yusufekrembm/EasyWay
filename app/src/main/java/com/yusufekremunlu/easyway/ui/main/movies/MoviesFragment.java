package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.adapters.MoviesAdapter;
import com.yusufekremunlu.easyway.model.Movie;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesFragment extends Fragment {

    private MoviesViewModel movieViewModel;
    private MoviesAdapter moviesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        movieViewModel.loadPopularMovies(1); // Örnek olarak 1. sayfayı yüklüyoruz

        moviesAdapter = new MoviesAdapter(movieViewModel.getPopularMoviesLiveData().getValue(), requireContext());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.popularRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moviesAdapter);

        @SuppressLint("NotifyDataSetChanged") final Observer<List<Movie>> popularMoviesObserver = movies -> {

            moviesAdapter.setMovieList(movies);
            moviesAdapter.notifyDataSetChanged();
        };

        // Observer'ı ViewModel'deki LiveData'ya bağlıyoruz
        movieViewModel.getPopularMoviesLiveData().observe(getViewLifecycleOwner(), popularMoviesObserver);

        return view;
    }
}

