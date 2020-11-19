package com.example.project2chatappv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.respository.GroupChatRespository;

import java.util.List;

public class GroupChatViewModel extends AndroidViewModel {

    private GroupChatRespository groupChatRespository;
    private MutableLiveData<List<GroupChatModel>> listGroupMutableLiveData;
    private MutableLiveData<List<MessageModel>> listMessageMutableLiveData;

    public GroupChatViewModel(@NonNull Application application) {
        super(application);

        groupChatRespository = new GroupChatRespository(application);
        listGroupMutableLiveData = groupChatRespository.getListGroupMutableLiveData();
        listMessageMutableLiveData = groupChatRespository.getListMessageMutableLiveData();
    }

    public MutableLiveData<List<GroupChatModel>> getListGroupMutableLiveData() {
        return listGroupMutableLiveData;
    }

    public MutableLiveData<List<MessageModel>> getListMessageMutableLiveData() {
        return listMessageMutableLiveData;
    }

    public void createGroupChat(String groupName, String myId, List<String> listId){
        groupChatRespository.createGroup(groupName, myId,listId);
    }

    public void getGroupChat(String myID){
        groupChatRespository.getGroupChat(myID);
    }

    public void sendMessage(String idGroup, String idSender, String message){
        groupChatRespository.sendMess(idGroup, idSender, message);
    }

    public void readMessage(String idGroup){
        groupChatRespository.readMessage(idGroup);
    }
}
