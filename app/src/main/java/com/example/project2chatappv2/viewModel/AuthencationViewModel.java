package com.example.project2chatappv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.respository.AuthecationRespository;
import com.google.firebase.auth.FirebaseUser;

public class AuthencationViewModel extends AndroidViewModel {

    private AuthecationRespository authRespository;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> statusLoginMutableleLiveData;
    private MutableLiveData<Boolean> statusLogoutMutableleLiveData;


    public AuthencationViewModel(@NonNull Application application) {
        super(application);

        authRespository = new AuthecationRespository(application);
        firebaseUserMutableLiveData = authRespository.getFirebaseUserMutableLiveData();
        statusLoginMutableleLiveData = authRespository.getStatusLoginMutableleLiveData();
        statusLogoutMutableleLiveData = authRespository.getStatusLogoutMutableleLiveData();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getStatusLoginMutableleLiveData() {
        return statusLoginMutableleLiveData;
    }

    public MutableLiveData<Boolean> getStatusLogoutMutableleLiveData() {
        return statusLogoutMutableleLiveData;
    }

    public void login(String email, String password){
        authRespository.login(email, password);
    }

    public void register(String username, String email, String password){
        authRespository.register(username, email, password);
    }

    public void logout(){
        authRespository.logout();
    }
}
