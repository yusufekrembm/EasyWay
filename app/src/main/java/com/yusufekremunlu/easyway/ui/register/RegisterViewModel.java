package com.yusufekremunlu.easyway.ui.register;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth mAuth;

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
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
                        listener.onRegistrationSuccess();
                    } else {
                        // Kullanıcı kaydı başarısız
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Kayıt hatası";
                        listener.onRegistrationError(errorMessage);
                    }
                });
    }

    public interface OnRegistrationListener {
        void onRegistrationSuccess();
        void onRegistrationError(String errorMessage);
    }
}

