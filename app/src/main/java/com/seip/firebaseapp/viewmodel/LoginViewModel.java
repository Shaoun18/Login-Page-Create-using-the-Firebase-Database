package com.seip.firebaseapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    public enum Authstate {
        AUTHENTICATED, UNAUTHENTICATED
    }
    public MutableLiveData<Authstate> authLiveData;
    private MutableLiveData<String> errMsgLiveData;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public LoginViewModel(){
        authLiveData = new MutableLiveData<>();
        errMsgLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null) {
            authLiveData.postValue(Authstate.UNAUTHENTICATED);
        }else {
            authLiveData.postValue(Authstate.AUTHENTICATED);
        }
    }

    public FirebaseUser getUser() {
        return user;
    }

    public LiveData<String> getErrMsgLiveData() {
        return errMsgLiveData;
    }

    public void login(String email, String Password){
        auth.signInWithEmailAndPassword(email, Password).addOnSuccessListener(authResult -> {
            user = authResult.getUser();
            authLiveData.postValue(Authstate.AUTHENTICATED);
        }).addOnFailureListener(e -> {
            errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }
    public void registar (String email, String Password){
        auth.createUserWithEmailAndPassword(email, Password).addOnSuccessListener(authResult -> {
            user = authResult.getUser();
            authLiveData.postValue(Authstate.AUTHENTICATED);
        }).addOnFailureListener(e -> {
            errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }
    public void logout(){

        auth.signOut();
    }
}
