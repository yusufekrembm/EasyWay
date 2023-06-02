package com.yusufekremunlu.easyway.ui.register;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth mAuth;

    private final MutableLiveData<Boolean> signInTwitterSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signInTwitterError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signInGithubSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signInGithubError = new MutableLiveData<>();

    public RegisterViewModel() {
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
                        // Oturum açma başarılı
                        FirebaseUser user = mAuth.getCurrentUser();
                        // İlgili işlemleri gerçekleştirin
                    } else {
                        // Oturum açma başarısız
                    }
                });
    }

    public void signInWithGithub(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", "true");
        mAuth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(authResult -> {
                    // Handle success.
                    signInGithubSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    // Handle failure.
                    signInGithubError.setValue(e.getMessage());
                });
    }

    public void signInWithTwitter(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("login", "true");

        mAuth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            signInTwitterSuccess.setValue(true);
                        })
                .addOnFailureListener(
                        e -> {
                            signInTwitterError.setValue(e.getMessage());
                        });
    }

    public void registerUser(String email, String password, String rePassword, OnRegistrationListener listener) {
        if (!password.equals(rePassword)) {
            listener.onRegistrationError("Parolalar eşleşmiyor");
            return;
        }

        // Gerekli alanların kontrolü
        if (email.isEmpty() || password.isEmpty()) {
            listener.onRegistrationError("Lütfen alanları doldurunuz");
            return;
        }

        // Firebase Authentication ile kullanıcı kaydı yap
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Kullanıcı kaydı başarılı
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendEmailVerification(user, listener);
                        } else {
                            listener.onRegistrationError("Kullanıcı alınamadı");
                        }
                    } else {
                        // Kullanıcı kaydı başarısız
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Kayıt hatası";
                        listener.onRegistrationError(errorMessage);
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user, OnRegistrationListener listener) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // E-posta doğrulama e-postası başarıyla gönderildi
                        listener.onRegistrationSuccess("Hesabını aktifleştirmek için lütfen mailinize gelen bağlantıya tıklayın.");
                    } else {
                        // E-posta doğrulama e-postası gönderilemedi
                        listener.onRegistrationError("E-posta doğrulama e-postası gönderilemedi");
                    }
                });
    }


    public interface OnRegistrationListener {
        void onRegistrationSuccess(String successMessage);

        void onRegistrationError(String errorMessage);
    }
}

