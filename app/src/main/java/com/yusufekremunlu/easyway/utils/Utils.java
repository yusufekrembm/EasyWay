package com.yusufekremunlu.easyway.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.yusufekremunlu.easyway.ui.HomeActivity;

public class Utils {
    private static boolean isPasswordVisible;

    public static void startHomeActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    public static void showPassword(ImageView showPasswordButton, EditText passwordEditText) {
        showPasswordButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });
    }

    public static void setBackButtonClickListener(ImageButton backButton, FragmentActivity activity) {
        backButton.setOnClickListener(v -> {
            if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                activity.getSupportFragmentManager().popBackStack();
            } else {
                activity.onBackPressed();
            }
        });
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setupNextFocus(EditText currentEditText, EditText nextEditText) {
        currentEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                nextEditText.requestFocus();
                return true;
            }
            return false;
        });
    }

    public static void setupHideKeyboardOnEnter(EditText editText) {
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    public static void calculateStrengthPassword(EditText signUpPassword, ProgressBar strengthPassword, TextView strengthText) {
        strengthPassword.setVisibility(View.GONE);
        signUpPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                int passwordStrength = calculatePasswordStrength(password);
                strengthPassword.setVisibility(View.VISIBLE);
                strengthPassword.setProgress(passwordStrength);
                if (passwordStrength < 20 || password.length() < 6 || password.length() > 20) {
                    strengthText.setText("Geçersiz Şifre");
                    strengthPassword.setProgressTintList(ColorStateList.valueOf(Color.rgb(255, 0, 0)));
                } else if (passwordStrength <= 40) {
                    strengthPassword.setProgressTintList(null);
                    strengthPassword.setProgressTintList(ColorStateList.valueOf(Color.rgb(128, 128, 128)));
                    strengthText.setText("Weak");
                } else if (passwordStrength <= 60) {
                    strengthPassword.setProgressTintList(null);
                    strengthPassword.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                    strengthText.setText("Medium");
                } else if (passwordStrength <= 80) {
                    strengthPassword.setProgressTintList(null);
                    strengthPassword.setProgressTintList(ColorStateList.valueOf(Color.rgb(255, 165, 0)));
                    strengthText.setText("Strong");
                } else if (passwordStrength <= 100) {
                    strengthPassword.setProgressTintList(null);
                    strengthPassword.setProgressTintList(ColorStateList.valueOf(Color.rgb(255, 255, 153)));
                    strengthText.setText("Very Strong");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static int calculatePasswordStrength(String password) {
        int passwordStrength = 0;

        // Uzunluk faktörü
        int length = password.length();
        passwordStrength += Math.min(length * 2, 20); // Her karakter için 2 puan, maksimum 20 puan

        // Harf türleri faktörü
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasUppercase = password.matches(".*[A-Z].*");
        passwordStrength += (hasLowercase ? 20 : 0); // Küçük harf varsa 20 puan
        passwordStrength += (hasUppercase ? 20 : 0); // Büyük harf varsa 20 puan

        // Rakamlar faktörü
        boolean hasDigits = password.matches(".*\\d.*");
        passwordStrength += (hasDigits ? 20 : 0); // Rakam varsa 20 puan

        // Özel karakterler faktörü
        boolean hasSpecialChars = password.matches(".*[^a-zA-Z0-9].*");
        passwordStrength += (hasSpecialChars ? 20 : 0); // Özel karakter varsa 20 puan

        return passwordStrength;
    }
    public static void setCheckBoxTextColors(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        checkedColor,
                        uncheckedColor
                }
        );

        checkBox.setButtonTintList(colorStateList);
    }
}
