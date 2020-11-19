package com.example.project2chatappv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.respository.FriendsRespository;

import java.util.HashMap;
import java.util.List;

public class FriendsViewModel extends AndroidViewModel {

    private FriendsRespository friendsRespository;
    private MutableLiveData<List<String>> listIdMutableLiveData;
    private MutableLiveData<Boolean> statusISFriendMutableLiveData;

    public FriendsViewModel(@NonNull Application application) {
        super(application);

        friendsRespository = new FriendsRespository(application);
        listIdMutableLiveData = friendsRespository.getListIdMutableLiveData();
        statusISFriendMutableLiveData = friendsRespository.getStatusISFriendMutableLiveData();
    }


    public MutableLiveData<List<String>> getListIdMutableLiveData() {
        return listIdMutableLiveData;
    }

    public MutableLiveData<Boolean> getStatusISFriendMutableLiveData() {
        return statusISFriendMutableLiveData;
    }

    public void makeFriend(String myID, String friendID){
        friendsRespository.makeFriends(myID, friendID);
    }

    public void removeInvitationFriendsList(String myID, String friendID){
        friendsRespository.removeInvitationFriendsList(myID, friendID);
    }

    public void getInvitation(String myID){
        friendsRespository.getInvitation(myID);
    }

    public void isFriend(String myID, String friendID){
        friendsRespository.addFriends(myID, friendID);
    }

    public void removeFriendList(String myID, String friendID){
        friendsRespository.removeFriendList(myID, friendID);
    }

    public void getListFriend(String myID){
        friendsRespository.getListFriend(myID);
    }

    public void checkFriend(String myID, String friendID){
        friendsRespository.checkFriend(myID, friendID);
    }

}
