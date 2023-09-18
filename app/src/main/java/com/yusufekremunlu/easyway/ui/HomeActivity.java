package com.yusufekremunlu.easyway.ui;

import static android.content.ContentValues.TAG;

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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.yusufekremunlu.easyway.R;

public class HomeActivity extends AppCompatActivity {
    private static final int CAMERA_REQ_CODE = 100;
    private static final int PERMISSIONS_LENGTH = 2;
    private static final int REQUEST_CODE_SCAN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationProcesses();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        getAAID();
        getToken();
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
            }
            else if (destination.getId() == R.id.homeFragmentDetails) {
                bottomNav.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }  else {
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
    public void getAAID() {
        Task<AAIDResult> idResult = HmsInstanceId.getInstance(getApplicationContext()).getAAID();
        idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
            @Override
            public void onSuccess(AAIDResult aaidResult) {
                // Called when the AAID is obtained.
                String aaid = aaidResult.getId();
                Log.d(TAG, "getAAID success:" + aaid );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Called when the AAID fails to be obtained.
                Log.d(TAG, "getAAID failure:" + e);
            }
        });
    }
    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = "108599129";

                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(getApplicationContext()).getToken(appId, tokenScope);
                    Log.i(TAG, "get token: " + token);

                    // Check whether the token is null.
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }
    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }
}