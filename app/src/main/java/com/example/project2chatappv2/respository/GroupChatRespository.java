package com.example.project2chatappv2.respository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupChatRespository {

    private Application application;
    private DatabaseReference databaseReference;
    private List<GroupChatModel> groupChatModelList;
    private MutableLiveData<List<GroupChatModel>> listGroupMutableLiveData;
    private List<MessageModel> listMessage;
    private MutableLiveData<List<MessageModel>> listMessageMutableLiveData;

    public GroupChatRespository(Application application) {
        this.application = application;
        databaseReference = FirebaseDatabase.getInstance().getReference("GroupChat");

        groupChatModelList = new ArrayList<>();
        listGroupMutableLiveData = new MutableLiveData<>();
        listMessage = new ArrayList<>();
        listMessageMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<GroupChatModel>> getListGroupMutableLiveData() {
        return listGroupMutableLiveData;
    }

    public MutableLiveData<List<MessageModel>> getListMessageMutableLiveData() {
        return listMessageMutableLiveData;
    }

    public void createGroup(String groupName, String myID, List<String> listId){
        listId.add(myID);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("groupName", groupName);
        hashMap.put("member", listId);
        hashMap.put("imageURL", "default");
        hashMap.put("admin", myID);
        databaseReference.push().setValue(hashMap);
    }

    public void getGroupChat(final String myID){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatModelList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    GroupChatModel groupChatModel = dataSnapshot.getValue(GroupChatModel.class);
                    ArrayList<String> listID = groupChatModel.getMember();
                    for(String id: listID){
                        if(id.equals(myID)){
                            groupChatModel.setGroupID(dataSnapshot.getKey());
                            groupChatModelList.add(groupChatModel);
                            break;
                        }
                    }
                }
                listGroupMutableLiveData.postValue(groupChatModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMess(String idGroup, String idSender, String message){
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", idSender);
        hashMap.put("message", message);
        hashMap.put("time", ServerValue.TIMESTAMP);
        databaseReference.child(idGroup).child("message").push().setValue(hashMap);
    }

    public void readMessage(String idGroup){
        databaseReference.child(idGroup).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMessage.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    listMessage.add(messageModel);
                }
                listMessageMutableLiveData.postValue(listMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
