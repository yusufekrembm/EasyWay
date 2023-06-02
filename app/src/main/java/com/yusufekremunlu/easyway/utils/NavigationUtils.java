package com.yusufekremunlu.easyway.utils;

import android.app.Activity;
import android.content.Intent;

import com.yusufekremunlu.easyway.ui.HomeActivity;

public class NavigationUtils {
    public static void startHomeActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }
}
