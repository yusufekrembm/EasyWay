package com.yusufekremunlu.easyway.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzer;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.yusufekremunlu.easyway.R;

public class HomeActivity extends AppCompatActivity {
    private static final int CAMERA_REQ_CODE = 100;
    private static final int STORAGE_REQ_CODE = 101;
    private static final int PERMISSIONS_LENGTH = 2;
    private static final int REQUEST_CODE_SCAN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationProcesses();
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

        fab.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                startQRScan();
            } else {
                requestCameraPermission();
            }
        });
    }
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
    }

    private void startQRScan() {
        HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create();
        ScanUtil.startScan(HomeActivity.this, REQUEST_CODE_SCAN, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE && grantResults.length == PERMISSIONS_LENGTH && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startQRScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            int errorCode = data.getIntExtra(ScanUtil.RESULT_CODE, ScanUtil.SUCCESS);
            if (errorCode == ScanUtil.ERROR_NO_READ_PERMISSION && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestStoragePermission();
            } else {
                HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
                if (obj != null && !TextUtils.isEmpty(obj.getOriginalValue())) {
                    String scannedData = obj.getOriginalValue();
                    openWebPage(scannedData);
                } else {
                    Toast.makeText(this, "Tarama iptal edildi", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQ_CODE);
    }
    private void openWebPage(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}