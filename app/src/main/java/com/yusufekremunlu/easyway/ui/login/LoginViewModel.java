package com.yusufekremunlu.easyway.ui.login;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final MutableLiveData<Boolean> signInGithubSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signInGithubError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signInTwitterSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signInTwitterError = new MutableLiveData<>();

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getSignInGithubSuccess() {
        return signInTwitterSuccess;
    }

    public LiveData<String> getSignInGithubError() {
        return signInTwitterError;
    }

    public LiveData<Boolean> getSignInTwitterSuccess() {
        return signInTwitterSuccess;
    }

    public LiveData<String> getSignInTwitterError() {
        return signInTwitterError;
    }


    public void signInWithGoogle(Activity activity, GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Oturum açma başarılı
                        FirebaseUser user = mAuth.getCurrentUser();
                        // İlgili işlemleri gerçekleştirin
                    } else {
                        // Oturum açma başarısız
                    }
                });
    }


    public void signInWithTwitter(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        mAuth
                .startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            signInTwitterSuccess.setValue(true);
                        })
                .addOnFailureListener(
                        e -> {
                            signInTwitterError.setValue(e.getMessage());
                        });
    }

    public void signInWithGithub(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");

        mAuth
                .startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            signInGithubSuccess.setValue(true);
                        })
                .addOnFailureListener(
                        e -> {
                            signInGithubError.setValue(e.getMessage());
                        });

    }
    public void loginUser(String email, String password, OnRegistrationListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            listener.onRegistrationSuccess("Login successful.");
                        } else {
                            listener.onRegistrationError("Login failed. Please verify your email.");
                        }
                    } else {
                        listener.onRegistrationError("Login failed. Please check your credentials.");
                    }
                });
    }

    public interface OnRegistrationListener {
        void onRegistrationSuccess(String successMessage);
        void onRegistrationError(String errorMessage);
    }
}


