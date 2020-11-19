package com.example.project2chatappv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.respository.MessageRespository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private MessageRespository messageRespository;
    private MutableLiveData<List<MessageModel>> listMessMutableLiveData;
    private MutableLiveData<List<String>> listUserIdMutableLiveData;


    public MessageViewModel(@NonNull Application application) {
        super(application);

        messageRespository = new MessageRespository(application);
        listMessMutableLiveData = messageRespository.getListMessMutableLiveData();
        listUserIdMutableLiveData = messageRespository.getListUserIdMutableLiveData();
    }

    public MutableLiveData<List<MessageModel>> getListMessMutableLiveData() {
        return listMessMutableLiveData;
    }

    public MutableLiveData<List<String>> getListUserIdMutableLiveData() {
        return listUserIdMutableLiveData;
    }

    public void sendMess(String receiver, String message){
        messageRespository.sendMessage(receiver,message);
    }

    public void readMess(String receiver){
        messageRespository.readMessage(receiver);
    }

    public void receiverMessage(){
        messageRespository.receiverMess();
    }

    public void getLastMessage(List<String> listID, String myID){
        messageRespository.getLastMess(listID, myID);
    }
}
