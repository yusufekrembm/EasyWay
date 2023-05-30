package com.yusufekremunlu.easyway.ui.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yusufekremunlu.easyway.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Gerekli boş yapıcı metod
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        View goToSignUpPage = rootView.findViewById(R.id.goToSignUp);

        goToSignUpPage.setOnClickListener(v -> navigateToRegisterFragment());

        return rootView;
    }

    private void navigateToRegisterFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }
}
