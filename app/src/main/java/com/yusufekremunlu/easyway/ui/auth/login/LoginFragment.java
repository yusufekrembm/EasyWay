package com.yusufekremunlu.easyway.ui.auth.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.utils.Utils;


public class LoginFragment extends Fragment {
    private EditText emailEditText, passwordEditText;
    private LoginViewModel loginViewModel;
    private ImageView twitterLogin;
    private ActivityResultLauncher<Intent> signInLauncher;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView githubLogin;
    private CheckBox rememberMeCheckbox;

    public LoginFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Declaring Items
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        ImageButton backButton = view.findViewById(R.id.backButton);
        ImageView showPasswordButton = view.findViewById(R.id.passwordVisibilityToggle);
        twitterLogin = view.findViewById(R.id.twitterLogInButton);
        githubLogin = view.findViewById(R.id.githubLogin);
        AppCompatButton forgotPassword = view.findViewById(R.id.forgotPassword);
        LinearLayout loginButton = view.findViewById(R.id.loginButton);
        View goToSignUpPage = view.findViewById(R.id.goToSignUpPage);
        Animation buttonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_anim);
        rememberMeCheckbox = view.findViewById(R.id.rememberMeCheckBox);
        //Navigation
        goToSignUpPage.setOnClickListener(v -> navigateToRegisterFragment());
        forgotPassword.setOnClickListener(v -> navigateToForgotPasswordFragment());
        //User login processes
        loginButton.setOnClickListener(v -> {
            loginUser();
            loginButton.startAnimation(buttonAnimation);
        });
        view.findViewById(R.id.googleLogin).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
        twitterLogin.setOnClickListener(v -> signInTwitter());
        githubLogin.setOnClickListener(v -> signInGithub());
        signInGoogle();
        //Utils usage
        Utils.showPassword(showPasswordButton,passwordEditText);
        Utils.setBackButtonClickListener(backButton,getActivity());
        Utils.setupNextFocus(emailEditText,passwordEditText);
        Utils.setupHideKeyboardOnEnter(passwordEditText);
        Utils.setCheckBoxTextColors(rememberMeCheckbox,Color.rgb(255, 165, 0),Color.rgb(255, 165, 0));
        view.setOnClickListener(v -> Utils.hideKeyboard(requireContext(),view));

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

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // CheckBox durumunu kontrol edin
        boolean rememberMe = rememberMeCheckbox.isChecked();
        if (rememberMe) {
            editor.putString("email", email);
            editor.putString("password", password);
        } else {
            editor.remove("email");
            editor.remove("password");
        }
        editor.apply();

        loginViewModel.loginUser(email, password, new LoginViewModel.OnRegistrationListener() {
            @Override
            public void onRegistrationSuccess(String successMessage) {
                Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show();
                Utils.startHomeActivity(getActivity());
            }

            @Override
            public void onRegistrationError(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInTwitter() {
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

    private void signInGithub() {
        githubLogin.setOnClickListener(v -> loginViewModel.signInWithGithub(getActivity()));

        loginViewModel.getSignInGithubSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
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
        mGoogleSignInClient.signOut();

        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            loginViewModel.signInWithGoogle(getActivity(), account);
                        } catch (ApiException e) {
                            // Oturum açma başarısız oldu
                        }
                    }
                });
    }

    private void navigateToRegisterFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void navigateToForgotPasswordFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPassword2);
    }
}
