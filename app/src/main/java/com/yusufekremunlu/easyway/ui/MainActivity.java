package com.yusufekremunlu.easyway.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!TextUtils.isEmpty(savedEmail) && !TextUtils.isEmpty(savedPassword)) {
            // Kullanıcı giriş bilgileri mevcut, otomatik olarak ana ekrana yönlendir.
            startActivity(new Intent(this, HomeActivity.class));
            finish(); // Bu, geri tuşuna basıldığında Login sayfasına geri dönmemek için kullanılır.
            return;
        }
        setContentView(R.layout.activity_main);
    }
}