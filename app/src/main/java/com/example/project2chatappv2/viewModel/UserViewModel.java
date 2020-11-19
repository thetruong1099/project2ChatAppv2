package com.example.project2chatappv2.viewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.respository.UserResponsitory;
import com.google.firebase.storage.StorageTask;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserResponsitory userResponsitory;
    private MutableLiveData<UserModel> userModelMutableLiveData;
    private MutableLiveData<List<UserModel>> listUserMutableLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userResponsitory = new UserResponsitory(application);
        userModelMutableLiveData = userResponsitory.getUserModelMutableLiveData();
        listUserMutableLiveData = userResponsitory.getListUserMutableLiveData();
    }

    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    public MutableLiveData<List<UserModel>> getListUserMutableLiveData() {
        return listUserMutableLiveData;
    }

    public void getUserProfile(String id){
        userResponsitory.getUserProfile(id);
    }

    public void getListUser(List<String> listId){
        userResponsitory.getListUserProfile(listId);
    }

    public void updateUsername(String username){
        userResponsitory.updateUsername(username);
    }

    public void uploadImage(Uri imageUri, final Context context){
        userResponsitory.uploadImage(imageUri, context);
    }

    public void searchUser(String keyword){
        userResponsitory.searchUsers(keyword);
    }

}
