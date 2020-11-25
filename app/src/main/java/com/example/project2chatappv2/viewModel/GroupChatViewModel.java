package com.example.project2chatappv2.viewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

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
    private MutableLiveData<GroupChatModel> groupChatModelMutableLiveData;

    public GroupChatViewModel(@NonNull Application application) {
        super(application);

        groupChatRespository = new GroupChatRespository(application);
        listGroupMutableLiveData = groupChatRespository.getListGroupMutableLiveData();
        listMessageMutableLiveData = groupChatRespository.getListMessageMutableLiveData();
        groupChatModelMutableLiveData = groupChatRespository.getGroupChatModelMutableLiveData();
    }

    public MutableLiveData<List<GroupChatModel>> getListGroupMutableLiveData() {
        return listGroupMutableLiveData;
    }

    public MutableLiveData<List<MessageModel>> getListMessageMutableLiveData() {
        return listMessageMutableLiveData;
    }

    public MutableLiveData<GroupChatModel> getGroupChatModelMutableLiveData() {
        return groupChatModelMutableLiveData;
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

    public void uploadImage(Uri imageUri, Context context, String groupID){
        groupChatRespository.uploadImage(imageUri, context, groupID);
    }

    public void getGroupChatProfile(String groupID){
        groupChatRespository.getGroupChatProfile(groupID);
    }

    public void updateGroupName(String groupID, String groupName){
        groupChatRespository.updateGroupName(groupID, groupName);
    }

    public void addMember(String groupID, List<String> listID){
        groupChatRespository.addMember(groupID, listID);
    }

}
