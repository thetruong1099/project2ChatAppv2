package com.example.project2chatappv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project2chatappv2.adapter.AddMemberAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.AddMemberActivity;

import java.util.List;

public class MemberGroupChatViewModel extends ViewModel {

    private MutableLiveData<List<UserModel>> listMutableLiveData;

    public MemberGroupChatViewModel() {
        listMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<UserModel>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void setListMutableLiveData(List<UserModel> userModels) {
        listMutableLiveData.postValue(userModels);
    }
}
