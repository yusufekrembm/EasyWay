package com.yusufekremunlu.easyway.ui.register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.yusufekremunlu.easyway.R;

import java.util.Objects;


public class RegisterFragment extends Fragment {
    private EditText signUpEmail;
    private EditText signUpPassword;
    private EditText rePasswordSignUp;
    private RegisterViewModel registerViewModel;
    private ProgressBar strengthPassword;
    private TextView strengthText;
    private boolean isPasswordVisible = false;
    private ImageView showPasswordButton;
    private ImageView showRePasswordButton;
    private ImageButton backButton;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        signUpEmail = view.findViewById(R.id.signUpEmail);
        signUpPassword = view.findViewById(R.id.signUpPassword);
        rePasswordSignUp = view.findViewById(R.id.rePasswordSignUp);
        TextView registerButton = view.findViewById(R.id.registerText);
        strengthPassword = view.findViewById(R.id.strengthPassword);
        strengthText = view.findViewById(R.id.strengthText);
        showPasswordButton = view.findViewById(R.id.passwordVisibilityToggle);
        showRePasswordButton = view.findViewById(R.id.repasswordVisibilityToggle);
        backButton = view.findViewById(R.id.backButton);
        registerButton.setOnClickListener(v -> registerUser());

        View goToLoginPage = view.findViewById(R.id.goToLoginPage);
        goToLoginPage.setOnClickListener(v -> navigateToRegisterFragment());

        view.setOnClickListener(v -> hideKeyboard());
        keyboardProcess();
        calculateStrengthPassword();
        showPassword();
        backButton();

        return view;
    }

    private void registerUser() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();
        String rePassword = rePasswordSignUp.getText().toString().trim();

        registerViewModel.registerUser(email, password, rePassword, new RegisterViewModel.OnRegistrationListener() {
            @Override
            public void onRegistrationSuccess(String successMessage) {
                Toast.makeText(getActivity(), "Aktivasyon maili gönderildi: " + successMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistrationError(String errorMessage) {
                Toast.makeText(getActivity(), "Kayıt hatası: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void keyboardProcess() {
        signUpPassword.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                rePasswordSignUp.requestFocus();

                return true;
            }
            return false;
        });
        rePasswordSignUp.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }


    private void calculateStrengthPassword() {
        strengthPassword.setVisibility(View.GONE);
        signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
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

    private int calculatePasswordStrength(String password) {
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

    private void showPassword() {
        showPasswordButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                signUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                signUpPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
            signUpPassword.setSelection(signUpPassword.getText().length());
        });
        showRePasswordButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                rePasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                rePasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
            rePasswordSignUp.setSelection(rePasswordSignUp.getText().length());
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    private void navigateToRegisterFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
    }

    private void backButton(){
        backButton.setOnClickListener(v -> {
            // Bir önceki fragmenta geri dönmek için:
            if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                requireActivity().onBackPressed();
            }
        });
    }
}



