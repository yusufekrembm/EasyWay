package com.yusufekremunlu.easyway.ui.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.utils.Utils;


public class RegisterFragment extends Fragment {
    private EditText signUpEmail;
    private EditText signUpPassword;
    private EditText rePasswordSignUp;
    private RegisterViewModel registerViewModel;
    private ImageView twitterButton;
    private ImageView githubButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //Declaring Items
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        signUpEmail = view.findViewById(R.id.signUpEmail);
        signUpPassword = view.findViewById(R.id.signUpPassword);
        rePasswordSignUp = view.findViewById(R.id.rePasswordSignUp);
        LinearLayout registerButton = view.findViewById(R.id.registerLayout);
        ProgressBar strengthPassword = view.findViewById(R.id.strengthPassword);
        TextView strengthText = view.findViewById(R.id.strengthText);
        ImageView showPasswordButton = view.findViewById(R.id.passwordVisibilityToggle);
        ImageView showRePasswordButton = view.findViewById(R.id.repasswordVisibilityToggle);
        ImageButton backButton = view.findViewById(R.id.backButton);
        twitterButton = view.findViewById(R.id.twitterSignUp);
        githubButton = view.findViewById(R.id.githubSignUp);
        ImageView googleSignUp = view.findViewById(R.id.googleSignUp);
        View goToLoginPage = view.findViewById(R.id.goToLoginPage);
        Animation buttonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_anim);
        //Navigation
        goToLoginPage.setOnClickListener(v -> navigateToRegisterFragment());
        //Sign up process
        registerButton.setOnClickListener(v -> {
            registerButton.startAnimation(buttonAnimation);
            registerUser();
        });
        googleSignUp.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
        signInGoogle();
        twitterButton.setOnClickListener(v -> signInTwitter());
        githubButton.setOnClickListener(v -> signInGithub());
        //Utils usage
        Utils.showPassword(showPasswordButton,signUpPassword);
        Utils.showPassword(showRePasswordButton,rePasswordSignUp);
        Utils.setBackButtonClickListener(backButton,requireActivity());
        Utils.setupNextFocus(signUpEmail,signUpPassword);
        Utils.setupNextFocus(signUpPassword,rePasswordSignUp);
        Utils.setupHideKeyboardOnEnter(rePasswordSignUp);
        Utils.calculateStrengthPassword(signUpPassword, strengthPassword, strengthText);
        view.setOnClickListener(v -> Utils.hideKeyboard(requireContext(), view));
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

    private void signInTwitter(){
        twitterButton.setOnClickListener(v -> registerViewModel.signInWithTwitter(getActivity()));

        registerViewModel.getSignInTwitterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Başarılı giriş durumunda yapılacak işlemler
                Toast.makeText(getActivity(), "Twitter ile giriş başarılı", Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.getSignInTwitterError().observe(getViewLifecycleOwner(), error -> {
            // Hata durumunda yapılacak işlemler
            Toast.makeText(getActivity(), "Twitter ile giriş hatası: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    private void signInGithub(){
        githubButton.setOnClickListener(v -> registerViewModel.signInWithGithub(getActivity()));


        registerViewModel.getSignInGithubSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Başarılı giriş durumunda yapılacak işlemler
                Toast.makeText(getActivity(), "Github ile giriş başarılı", Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.getSignInGithubError().observe(getViewLifecycleOwner(), error -> {
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
                            registerViewModel.signInWithGoogle(getActivity(),account);
                        } catch (ApiException e) {
                            // Oturum açma başarısız oldu
                        }
                    }
                });
    }

    private void navigateToRegisterFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
    }
}



