package com.yusufekremunlu.easyway.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.yusufekremunlu.easyway.R;

public class HomeActivity extends AppCompatActivity {
    private Button logOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Çıkış işlemi
                performLogout();
            }
        });
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
}