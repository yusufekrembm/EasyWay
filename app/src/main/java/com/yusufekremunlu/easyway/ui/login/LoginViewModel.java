package com.yusufekremunlu.easyway.ui.login;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth mAuth;

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
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


