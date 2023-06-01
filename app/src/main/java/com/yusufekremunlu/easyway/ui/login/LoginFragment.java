package com.yusufekremunlu.easyway.ui.login;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yusufekremunlu.easyway.R;

public class LoginFragment extends Fragment {
    private EditText emailEditText, passwordEditText;
    private LoginViewModel loginViewModel;
    private ImageButton backButton;
    private ImageView showPasswordButton;
    private boolean isPasswordVisible = false;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        backButton = view.findViewById(R.id.backButton);
        showPasswordButton = view.findViewById(R.id.passwordVisibilityToggle);

        TextView loginTextBut = view.findViewById(R.id.loginTextBut);
        loginTextBut.setOnClickListener(v -> loginUser());
        view.setOnClickListener(v -> hideKeyboard());

        View goToSignUpPage = view.findViewById(R.id.goToSignUpPage);
        goToSignUpPage.setOnClickListener(v -> navigateToRegisterFragment());

        backButton();
        showPassword();
        keyboardProcess();

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return;
        }


        loginViewModel.loginUser(email, password, new LoginViewModel.OnRegistrationListener() {
            @Override
            public void onRegistrationSuccess(String successMessage) {
                Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show();
                // Handle successful login, e.g., navigate to another fragment
            }

            @Override
            public void onRegistrationError(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPassword() {
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    private void backButton() {
        backButton.setOnClickListener(v -> {
            // Bir önceki fragmenta geri dönmek için:
            if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                requireActivity().onBackPressed();
            }
        });
    }

    private void navigateToRegisterFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void keyboardProcess() {
        emailEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                passwordEditText.requestFocus();

                return true;
            }
            return false;
        });
        passwordEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }
}
