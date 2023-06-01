package com.yusufekremunlu.easyway.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yusufekremunlu.easyway.R;

public class LoginFragment extends Fragment {
    private EditText emailEditText, passwordEditText;
    private LoginViewModel loginViewModel;
    private ImageButton backButton;
    private ImageView showPasswordButton;
    private ImageView twitterLogin;
    private boolean isPasswordVisible = false;
    private ActivityResultLauncher<Intent> signInLauncher;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView githubLogin;

    public LoginFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        backButton = view.findViewById(R.id.backButton);
        showPasswordButton = view.findViewById(R.id.passwordVisibilityToggle);
        twitterLogin = view.findViewById(R.id.twitterLogInButton);
        githubLogin = view.findViewById(R.id.githubLogin);

        TextView loginTextBut = view.findViewById(R.id.loginTextBut);
        loginTextBut.setOnClickListener(v -> loginUser());
        view.setOnClickListener(v -> hideKeyboard());

        View goToSignUpPage = view.findViewById(R.id.goToSignUpPage);
        goToSignUpPage.setOnClickListener(v -> navigateToRegisterFragment());

        view.findViewById(R.id.googleLogin).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
        twitterLogin.setOnClickListener(v -> signInTwitter());
        githubLogin.setOnClickListener(v -> signInGithub());

        signInGoogle();
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
    private void signInTwitter(){
        twitterLogin.setOnClickListener(v -> loginViewModel.signInWithTwitter(getActivity()));

        loginViewModel.getSignInTwitterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Başarılı giriş durumunda yapılacak işlemler
                Toast.makeText(getActivity(), "Twitter ile giriş başarılı", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getSignInTwitterError().observe(getViewLifecycleOwner(), error -> {
            // Hata durumunda yapılacak işlemler
            Toast.makeText(getActivity(), "Twitter ile giriş hatası: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    private void signInGithub(){
        githubLogin.setOnClickListener(v -> loginViewModel.signInWithGithub(getActivity()));

        loginViewModel.getSignInGithubSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Başarılı giriş durumunda yapılacak işlemler
                Toast.makeText(getActivity(), "Github ile giriş başarılı", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getSignInGithubError().observe(getViewLifecycleOwner(), error -> {
            // Hata durumunda yapılacak işlemler
            Toast.makeText(getActivity(), "Github ile giriş hatası: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    private void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            loginViewModel.signInWithGoogle(getActivity(),account);
                        } catch (ApiException e) {
                            // Oturum açma başarısız oldu
                        }
                    }
                });
    }
}
