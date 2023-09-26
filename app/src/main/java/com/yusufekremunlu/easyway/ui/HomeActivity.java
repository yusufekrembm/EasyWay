package com.yusufekremunlu.easyway.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yusufekremunlu.easyway.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationProcesses();
    }

    private void bottomNavigationProcesses() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.setItemIconTintList(null);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.showAllFragment) {
                bottomNav.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.favouritesFragment) {
                bottomNav.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.movieDetailsFragment) {
                bottomNav.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.movieCastDetails) {
                bottomNav.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.favouritesDetailFragment) {
                bottomNav.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.homeFragmentDetails) {
                bottomNav.setVisibility(View.GONE);
            } else {
                bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }
}