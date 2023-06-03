package com.yusufekremunlu.easyway.ui.login;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.yusufekremunlu.easyway.utils.Utils;

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
        return signInGithubSuccess;
    }

    public LiveData<String> getSignInGithubError() {
        return signInGithubError;
    }

    public LiveData<Boolean> getSignInTwitterSuccess() {
        return signInTwitterSuccess;
    }

    public LiveData<String> getSignInTwitterError() {
        return signInTwitterError;
    }


    public void signInWithGoogle(Activity activity, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Utils.startHomeActivity(activity);
                        // İlgili işlemleri gerçekleştirin
                    } else {
                        // Oturum açma başarısız
                    }
                });
    }


    public void signInWithTwitter(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("login", "true");

        mAuth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            signInTwitterSuccess.setValue(true);
                            Utils.startHomeActivity(activity);
                        })
                .addOnFailureListener(
                        e -> {
                            signInTwitterError.setValue(e.getMessage());
                        });
    }

    public void signInWithGithub(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", "true");
        mAuth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(authResult -> {
                    // Handle success.
                    signInGithubSuccess.setValue(true);
                    Utils.startHomeActivity(activity);
                })
                .addOnFailureListener(e -> {
                    // Handle failure.
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

    public void resetPassword(String email, Context context) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Şifre sıfırlama e-postası başarıyla gönderildi
                        Toast.makeText(context, "Şifre sıfırlama e-postası gönderildi.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Şifre sıfırlama e-postası gönderilirken bir hata oluştu
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(context, "Şifre sıfırlama e-postası gönderilirken hata oluştu: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



