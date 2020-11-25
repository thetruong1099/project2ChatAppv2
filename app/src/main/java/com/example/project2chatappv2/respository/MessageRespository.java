package com.example.project2chatappv2.respository;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageRespository {

    private Application application;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<MessageModel>> listMessMutableLiveData;
    private List<MessageModel> listMess;
    private List<String> userList;
    private MutableLiveData<List<String>> listUserIdMutableLiveData;


    public MessageRespository(Application application) {
        this.application = application;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        listMessMutableLiveData = new MutableLiveData<>();
        listUserIdMutableLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<MessageModel>> getListMessMutableLiveData() {
        return listMessMutableLiveData;
    }

    public MutableLiveData<List<String>> getListUserIdMutableLiveData() {
        return listUserIdMutableLiveData;
    }

    public void sendMessage(String idReceiver, String message){
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", firebaseUser.getUid());
        hashMap.put("receiver",idReceiver);
        hashMap.put("message", message);
        hashMap.put("time", ServerValue.TIMESTAMP);

        databaseReference.push().setValue(hashMap);

    }

    public void readMessage(final String idReceiver){
        listMess = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMess.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    if (messageModel.getSender().equals(firebaseUser.getUid())&&messageModel.getReceiver().equals(idReceiver)
                            || messageModel.getSender().equals(idReceiver)&&messageModel.getReceiver().equals(firebaseUser.getUid())){
                        listMess.add(messageModel);
                    }
                }
                if(listMess!=null){
                    listMessMutableLiveData.postValue(listMess);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void receiverMess(){
        userList = new ArrayList<>();
        final  List<String> userIdList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    if (messageModel.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(messageModel.getSender());
                    }
                    if (messageModel.getSender().equals(firebaseUser.getUid())){
                        userList.add(messageModel.getReceiver());
                    }
                }
                if (userList.size()>0){
                    userIdList.add(userList.get(userList.size()-1));
                    for(int i = userList.size()-2; i>=0;i--){
                        boolean flag=false;
                        for (int j = 0;j<userIdList.size();j++){
                            if(userList.get(i).equals(userIdList.get(j))){
                                flag =true;
                                break;
                            }
                        }
                        if (flag == false) userIdList.add(userList.get(i));
                    }
                }
                listUserIdMutableLiveData.postValue(userIdList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getLastMess(final List<String> listId, final String myID){
        final List<MessageModel> listMessage = new ArrayList<>();
        final List<MessageModel> listLasttMessage = new ArrayList<>();
        databaseReference.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listMessage.add(dataSnapshot.getValue(MessageModel.class));
                }

                for (int i=0; i<listId.size(); i++){
                    for (int j = listMessage.size()-1; j>=0 ;j--){
                        if (listMessage.get(j).getReceiver().equals(listId.get(i))&&listMessage.get(j).getSender().equals(myID)
                                ||listMessage.get(j).getSender().equals(listId.get(i))&&listMessage.get(j).getReceiver().equals(myID)){
                            listLasttMessage.add(listMessage.get(j));
                            break;
                        }
                    }
                }
                listMessMutableLiveData.postValue(listLasttMessage);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
