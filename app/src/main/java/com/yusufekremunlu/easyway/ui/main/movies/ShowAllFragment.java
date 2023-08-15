package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.graphics.Movie;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_filters, menu);
        MenuItem searchItem = menu.findItem(R.id.searchMovie);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Metin gönderildiğinde yapılacak işlemler
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Eğer metin boşsa, orijinal veri listesini kullan
                    showAllAdapter.setDiscoverList(discoverListFull);
                } else {
                    // Metne göre filtreleme yap
                    List<MovieModel> filteredList = new ArrayList<>();
                    for (MovieModel item : discoverListFull) {
                        if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    showAllAdapter.setDiscoverList(filteredList);
                }

                // Adapter'i güncelle
                showAllAdapter.notifyDataSetChanged();

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
    private void observeData() {
        moviesShowAllViewModel.getDiscoverMovies().observe(getViewLifecycleOwner(), movieModels -> {
            discoverListFull = new ArrayList<>(movieModels); // Tüm verileri kaydet
            showAllAdapter.setDiscoverList(discoverListFull); // Tüm verileri RecyclerView'a ata
            showAllProgressBar.setVisibility(View.INVISIBLE);
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

