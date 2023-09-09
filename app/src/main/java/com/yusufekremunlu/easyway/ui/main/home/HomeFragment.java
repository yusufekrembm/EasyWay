package com.yusufekremunlu.easyway.ui.main.home;

import static com.yusufekremunlu.easyway.utils.Constants.getNews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;
import com.yusufekremunlu.easyway.ui.main.home.adapters.NewsAdapter;
import com.yusufekremunlu.easyway.ui.main.home.viewmodels.NewsViewModel;
import com.yusufekremunlu.easyway.utils.MovieListType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements SelectListener,View.OnClickListener{
    private NewsViewModel newsViewModel;
    private NewsAdapter newsAdapter;
    ProgressDialog progressDialog;

    Button cat_btn1,cat_btn2,cat_btn3,cat_btn4,cat_btn5,cat_btn6,cat_btn7;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        newsAdapter = new NewsAdapter(getContext(),new ArrayList<>(),this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner countrySpinner = view.findViewById(R.id.spinnerCountry);
        ArrayList<String> countryCodes = new ArrayList<>(Arrays.asList("us", "fr", "de", "tr"));
        ArrayList<String> countryNames = new ArrayList<>();
        for (String code : countryCodes) {
            String name = getNews(code);
            countryNames.add(name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, countryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountryCode = countryCodes.get(position);
                newsViewModel.setCountry(selectedCountryCode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        RecyclerView newsRecyclerView = view.findViewById(R.id.recyclerNewsCategoryHeader);
        GridLayoutManager newsLayoutManager = new GridLayoutManager(requireContext(),1);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        newsRecyclerView.setAdapter(newsAdapter);

        cat_btn1 = view.findViewById(R.id.cat_btn_1);
        cat_btn2 = view.findViewById(R.id.cat_btn2);
        cat_btn3 = view.findViewById(R.id.cat_btn3);
        cat_btn4 = view.findViewById(R.id.cat_btn_4);
        cat_btn5 = view.findViewById(R.id.cat_btn_5);
        cat_btn6 = view.findViewById(R.id.cat_btn_6);
        cat_btn7 = view.findViewById(R.id.cat_btn_7);
        cat_btn1.setOnClickListener(this);
        cat_btn2.setOnClickListener(this);
        cat_btn3.setOnClickListener(this);
        cat_btn4.setOnClickListener(this);
        cat_btn5.setOnClickListener(this);
        cat_btn6.setOnClickListener(this);
        cat_btn7.setOnClickListener(this);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsViewModel.setQuery(query);
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
        newsViewModel.getTopHeadNews().observe(getViewLifecycleOwner(), newsHeadlines -> {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Fetching news");
            progressDialog.show();
            newsAdapter.updateData(newsHeadlines);
            if(newsAdapter!=null){
                progressDialog.dismiss();
            }
        });
        newsViewModel.getEverythingNews().observe(getViewLifecycleOwner(), newsHeadlines -> {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Fetching news");
            progressDialog.show();
            newsAdapter.updateData(newsHeadlines);
            if(newsAdapter!=null){
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        progressDialog.setTitle("Fetching new articles of "+ category);
        newsViewModel.setCategory(category);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("news", headlines);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_fragment_home_to_homeFragmentDetails, bundle);
    }
}

