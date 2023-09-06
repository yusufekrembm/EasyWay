package com.yusufekremunlu.easyway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.ml.scan.HmsScanFrame;
import com.huawei.hms.ml.scan.HmsScanFrameOptions;
import com.yusufekremunlu.easyway.R;

import java.io.IOException;

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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }

    private void bottomNavigationProcesses() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.setItemIconTintList(null);

        FloatingActionButton fab = findViewById(R.id.fab);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.showAllFragment) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.favouritesFragment) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.movieDetailsFragment) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.movieCastDetails) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.favouritesDetailFragment) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else {
                fab.setVisibility(View.VISIBLE);
                bottomNav.setVisibility(View.VISIBLE);
            }
        });

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
        HmsScanAnalyzerOptions option = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create();
        ScanUtil.startScan(HomeActivity.this, REQUEST_CODE_SCAN, option);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE && grantResults.length == PERMISSIONS_LENGTH && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startQRScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            int errorCode = data.getIntExtra(ScanUtil.RESULT_CODE, ScanUtil.SUCCESS);
            if (errorCode == ScanUtil.SUCCESS) {
                HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
                if (obj != null && !TextUtils.isEmpty(obj.getOriginalValue())) {
                    String scannedData = obj.getOriginalValue();
                    openWebPage(scannedData);
                }
            }
            if (errorCode == ScanUtil.ERROR_NO_READ_PERMISSION) {
                Toast.makeText(this, "Tarama iptal edildi", Toast.LENGTH_SHORT).show();
            }
        }

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