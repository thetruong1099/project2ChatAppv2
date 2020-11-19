package com.example.project2chatappv2.respository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AuthecationRespository {

    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> statusLoginMutableleLiveData;
    private MutableLiveData<Boolean> statusLogoutMutableleLiveData;
    private DatabaseReference reference;

    public AuthecationRespository(Application application) {
        this.application = application;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUserMutableLiveData = new MutableLiveData<>();
        statusLoginMutableleLiveData = new MutableLiveData<>();
        statusLogoutMutableleLiveData = new MutableLiveData<>();


        statusLoginMutableleLiveData.postValue(false);
        statusLogoutMutableleLiveData.postValue(false);

        if (firebaseAuth.getCurrentUser() != null){
            firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            statusLoginMutableleLiveData.postValue(true);
        }
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
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    statusLoginMutableleLiveData.postValue(true);
                }else{
                    Toast.makeText(application, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status","offline");
                    hashMap.put("search", username.toLowerCase());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            statusLoginMutableleLiveData.postValue(true);
                        }
                    });

                }else{
                    Toast.makeText(application, "Register failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logout(){
        firebaseAuth.signOut();
        statusLogoutMutableleLiveData.postValue(true);
    }
}
