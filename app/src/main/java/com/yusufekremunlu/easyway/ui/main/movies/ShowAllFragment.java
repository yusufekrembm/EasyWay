package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.ShowAllAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesViewModel;
import com.yusufekremunlu.easyway.utils.MovieListType;

import java.util.ArrayList;
import java.util.List;

public class ShowAllFragment extends Fragment implements ShowAllAdapter.OnItemClickListener {
    private MoviesViewModel moviesViewModel;
    private RecyclerView showAllRecyclerView;
    private ShowAllAdapter showAllAdapter;
    private ProgressBar showAllProgressBar;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        showAllAdapter = new ShowAllAdapter(new ArrayList<>(), getContext());
        showAllAdapter.setOnItemClickListener(this);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding binding = FragmentShowAllBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        showAllRecyclerView = rootView.findViewById(R.id.showAllRecyclerView);
        showAllProgressBar = rootView.findViewById(R.id.moviesProgressBar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        showAllRecyclerView.setLayoutManager(gridLayoutManager);
        showAllRecyclerView.setAdapter(showAllAdapter);
        showAllProgressBar.setVisibility(View.VISIBLE);
        Toolbar myToolbar = binding.myToolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(myToolbar);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("movieListType")) {
            MovieListType movieListType = (MovieListType) bundle.getSerializable("movieListType");
            observeDataForListType(movieListType);
        }
        return rootView;
    }

    private void observeDataForListType(MovieListType movieListType) {
        switch (movieListType) {
            case TRENDING:
                moviesViewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            case POPULAR:
                moviesViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            case UPCOMING:
                moviesViewModel.getUpComingMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_filters, menu);
        MenuItem searchItem = menu.findItem(R.id.searchMovie);
        MenuItem filterItem = menu.findItem(R.id.filterMovie);
        SearchView searchView = (SearchView) searchItem.getActionView();
        filterItem.setOnMenuItemClickListener(item -> {

            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.searchMovie) {
            return true;
        } else if (itemId == R.id.filterMovie) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(MovieModel movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_showAllFragment_to_movieDetailsFragment, bundle);
    }
}

