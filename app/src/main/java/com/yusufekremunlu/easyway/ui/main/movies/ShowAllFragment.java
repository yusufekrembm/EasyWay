package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.ShowAllAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesShowAllViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowAllFragment extends Fragment implements ShowAllAdapter.OnItemClickListener {
    private MoviesShowAllViewModel moviesShowAllViewModel;
    private RecyclerView showAllRecyclerView;
    private List<MovieModel> discoverListFull = new ArrayList<>();
    private ShowAllAdapter showAllAdapter;
    private ProgressBar showAllProgressBar;
    private final MovieApiClient movieApiClient = MovieApiClient.getInstance();

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesShowAllViewModel = new ViewModelProvider(this).get(MoviesShowAllViewModel.class);
        showAllAdapter = new ShowAllAdapter(new ArrayList<>(), getContext());
        showAllAdapter.setOnItemClickListener(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding binding = FragmentShowAllBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        showAllRecyclerView = rootView.findViewById(R.id.showAllRecyclerView);
        showAllProgressBar = rootView.findViewById(R.id.moviesProgressBar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        showAllRecyclerView.setLayoutManager(gridLayoutManager);
        showAllRecyclerView.setAdapter(showAllAdapter);
        showAllProgressBar.setVisibility(View.VISIBLE);
        loadNextPages();
        observeData();
        Toolbar myToolbar = binding.myToolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(myToolbar);
        observeAnyChange();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_filters, menu);
        MenuItem searchItem = menu.findItem(R.id.searchMovie);
        MenuItem filterItem = menu.findItem(R.id.filterMovie);
        SearchView searchView = (SearchView) searchItem.getActionView();
        filterItem.setOnMenuItemClickListener(item -> {
            showFilterPopup();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                moviesShowAllViewModel.searchMovieApi(query, 1);
                boolean noResults = true;
                for (MovieModel movie : discoverListFull) {
                    if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        noResults = false;
                        break;
                    }
                }

                TextView noResultsTextView = requireView().findViewById(R.id.noResultsTextView);
                if (noResults) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showAllAdapter.setDiscoverList(discoverListFull);
                } else {
                    moviesShowAllViewModel.searchMovieApi(newText, 1);
                }
                return true;
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

    private void observeAnyChange() {
        moviesShowAllViewModel.getDiscoverMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels) {
                        Log.v("Tag", "onChanged : " + movieModel.getTitle());
                    }
                }
            }
        });
    }

    private void observeData() {
        moviesShowAllViewModel.getDiscoverMovies().observe(getViewLifecycleOwner(), movieModels -> {
            discoverListFull = new ArrayList<>(movieModels);
            showAllAdapter.setDiscoverList(discoverListFull);
            showAllProgressBar.setVisibility(View.INVISIBLE);
        });
    }

    private void loadNextPages() {
        showAllRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
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
    private void showFilterPopup() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.movie_popup_layout);
        EditText startDateEditText = dialog.findViewById(R.id.start_date_edittext);
        EditText endDateEditText = dialog.findViewById(R.id.end_date_edittext);
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));
        SeekBar imdbRatingSeekBar = dialog.findViewById(R.id.imdb_rating_seekbar);
        TextView imdbRangeText = dialog.findViewById(R.id.imdb_range_text);

        imdbRatingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int selectedRating = progress + 1;
                imdbRangeText.setText("IMDb Range: " + selectedRating + " - " + (selectedRating + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        dialog.show();
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int year = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            year = calendar.get(Calendar.YEAR);
        }
        int month = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            month = calendar.get(Calendar.MONTH);
        }
        int day = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
                    editText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

}

