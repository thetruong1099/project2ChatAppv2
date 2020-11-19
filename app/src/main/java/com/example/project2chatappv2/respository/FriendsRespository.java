package com.example.project2chatappv2.respository;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FriendsRespository {

    private Application application;
    private DatabaseReference reference;

    private MutableLiveData<List<String>> listIdMutableLiveData;
    private MutableLiveData<Boolean> statusISFriendMutableLiveData;

    private boolean statusISFriend = false;

    public FriendsRespository(Application application) {
        this.application = application;
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        listIdMutableLiveData = new MutableLiveData<>();
        statusISFriendMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<String>> getListIdMutableLiveData() {
        return listIdMutableLiveData;
    }

    public MutableLiveData<Boolean> getStatusISFriendMutableLiveData() {
        return statusISFriendMutableLiveData;
    }

    public void makeFriends(String myID, String friendID) {
        reference.child(friendID).child("Invitation").child(myID).child("id").setValue(myID);
    }

    public void getInvitation(String myID){
        final List<String> listId = new ArrayList<>();
        reference.child(myID).child("Invitation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listId.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    listId.add(userModel.getId());
                }
                if (listId.size()>0){
                    listIdMutableLiveData.postValue(listId);
                }
                else {
                    listId.clear();
                    listIdMutableLiveData.setValue(listId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeInvitationFriendsList(String myID, String friendID){
        reference.child(myID).child("Invitation").child(friendID).removeValue();
    }

    public void addFriends(String myID, String friendID) {
        reference.child(myID).child("IsFriends").child(friendID).child("id").setValue(friendID);
    }

    public void removeFriendList(String myID, String friendID){
        reference.child(myID).child("IsFriends").child(friendID).removeValue();
    }

    public void getListFriend(String myID){
        final List<String> listId = new ArrayList<>();
        reference.child(myID).child("IsFriends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listId.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    listId.add(userModel.getId());
                }
                if (listId.size()>0){
                    listIdMutableLiveData.postValue(listId);
                }
                else {
                    listId.clear();
                    listIdMutableLiveData.setValue(listId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void checkFriend(String myID, String friendID){
        reference.child(myID).child("IsFriends").child(friendID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel!=null){
                    statusISFriend = true;
                }
                System.out.println( statusISFriend);
                statusISFriendMutableLiveData.postValue(statusISFriend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
