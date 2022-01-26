package com.seip.firebaseapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seip.firebaseapp.databinding.FragmentLoginBinding;
import com.seip.firebaseapp.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    public FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private boolean islogin = true;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.loginbtn.setOnClickListener(view -> {
            islogin  = true;
            authenticate();
        });
        binding.registarsp.setOnClickListener(view -> {
            islogin  = false;
            authenticate();
        });
        loginViewModel.getErrMsgLiveData().observe(getViewLifecycleOwner(), s -> {
            binding.errMSG.setText(s);
        });
        loginViewModel.authLiveData.observe(getViewLifecycleOwner(), authstate -> {
            if(authstate == LoginViewModel.Authstate.AUTHENTICATED){
                Navigation.findNavController(container).popBackStack();
            }
        });
        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.namesp.getText().toString();
        final String pass  = binding.Passwordsp.getText().toString();
        // TODO: 1/23/2022 validate empty fields
        if(islogin){
            loginViewModel.login(email,pass);
        }else {
            loginViewModel.registar(email, pass);
        }
    }
}