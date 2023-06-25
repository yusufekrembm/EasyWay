package com.yusufekremunlu.easyway.ui.auth.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.utils.Utils;

public class ForgotPassword extends Fragment {
    private LoginViewModel viewModel;
    private LinearLayout buttonForgotPassword;
    private EditText forgotEmailText;
    private Animation buttonAnimation;

    public ForgotPassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        buttonForgotPassword = view.findViewById(R.id.buttonForgotPassword);
        ImageButton backButton = view.findViewById(R.id.backButton);
        forgotEmailText = view.findViewById(R.id.forgotEmailText);
        buttonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_anim);
        //Functions
        sendEmailLinkForgotPassword();
        //Utils usage
        Utils.setBackButtonClickListener(backButton,getActivity());
        Utils.setupHideKeyboardOnEnter(forgotEmailText);
        view.setOnClickListener(v -> Utils.hideKeyboard(requireContext(),view));
        return view;
    }

    private boolean isLinkSent = false; // Başlangıçta bayrağı false olarak ayarlayın

    private void sendEmailLinkForgotPassword() {
        buttonForgotPassword.setOnClickListener(v -> {
            String email = forgotEmailText.getText().toString();
            buttonForgotPassword.startAnimation(buttonAnimation);

            if (TextUtils.isEmpty(email)) {
                // E-posta alanı boş olduğunda yapılacak işlemler
                Toast.makeText(requireContext(), "E-posta alanı boş olamaz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isLinkSent) { // Bayrak false ise bağlantıyı gönder
                viewModel.resetPassword(email, requireContext());
                isLinkSent = true; // Bayrağı true olarak ayarla
            } else {
                Toast.makeText(requireContext(), "Zaten bir bağlantı gönderildi", Toast.LENGTH_SHORT).show();
            }
        });
    }


}