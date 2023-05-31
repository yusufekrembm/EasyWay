package com.yusufekremunlu.easyway.ui.register;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

