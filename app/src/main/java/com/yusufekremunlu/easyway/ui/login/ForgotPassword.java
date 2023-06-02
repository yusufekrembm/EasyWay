package com.yusufekremunlu.easyway.ui.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yusufekremunlu.easyway.R;

public class ForgotPassword extends Fragment {
    private LoginViewModel viewModel;
    private ImageButton backButton;
    private TextView sendLinkForgotPassword;
    private EditText editText;

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

        sendLinkForgotPassword = view.findViewById(R.id.loginTextBut);
        backButton = view.findViewById(R.id.backButton);
        editText = view.findViewById(R.id.forgotEmailText);

        sendEmailLinkForgotPassword();
        backButton();
        // Inflate the layout for this fragment
        return view;
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

    private void sendEmailLinkForgotPassword(){
        sendLinkForgotPassword.setOnClickListener(v -> {
            String email = editText.getText().toString(); // EditText'ten e-posta adresini alın
            viewModel.resetPassword(email, requireContext());
        });
    }


}