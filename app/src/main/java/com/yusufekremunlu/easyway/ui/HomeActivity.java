package com.yusufekremunlu.easyway.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yusufekremunlu.easyway.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationProcesses();
    }

    private void performLogout() {
        // Oturumu kapatma işlemleri
        // Örneğin, kullanıcının oturum bilgilerini sıfırlayabilirsiniz:
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", "");
        editor.putString("password", "");
        editor.apply();

        // Ana ekrana yönlendirme
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void bottomNavigationProcesses(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.setItemIconTintList(null);

        // FloatingActionButton'ı tanımlayın ve görünümü bulun
        FloatingActionButton fab = findViewById(R.id.fab);

        // FloatingActionButton için tıklama olayı dinleyicisi ekleyin
        fab.setOnClickListener(v -> {
            bottomNav.setSelectedItemId(R.id.fragment_qr);
        });
    }
}