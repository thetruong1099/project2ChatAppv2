package com.example.project2chatappv2.respository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private MutableLiveData<GroupChatModel> groupChatModelMutableLiveData;

    private StorageReference storageReference;

    public GroupChatRespository(Application application) {
        this.application = application;
        databaseReference = FirebaseDatabase.getInstance().getReference("GroupChat");

        groupChatModelList = new ArrayList<>();
        listGroupMutableLiveData = new MutableLiveData<>();
        listMessage = new ArrayList<>();
        listMessageMutableLiveData = new MutableLiveData<>();
        groupChatModelMutableLiveData = new MutableLiveData<>();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
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
                    if (listID!=null){
                        for(String id: listID){
                            if(id.equals(myID)){
                                groupChatModel.setGroupID(dataSnapshot.getKey());
                                groupChatModelList.add(groupChatModel);
                                break;
                            }
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

    private String getFIleExtension(Uri uri, Context context){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(Uri imageUri, final Context context, final String groupID){
        if (imageUri != null){

            final StorageReference fileReference = storageReference.child(String.valueOf(System.currentTimeMillis())+"."+getFIleExtension(imageUri, context));

            UploadTask uploadTask = fileReference.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful() ){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        if (downloadUri!=null){
                            String photoLink = downloadUri.toString();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL", photoLink);
                            databaseReference.child(groupID).updateChildren(map);
                        }
                    }
                }
            });
        }
    }

    public void getGroupChatProfile(String groupID){
        databaseReference.child(groupID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupChatModel groupChatModel = snapshot.getValue(GroupChatModel.class);
                groupChatModelMutableLiveData.postValue(groupChatModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateGroupName(String groupID, String groupName){
        databaseReference.child(groupID).child("groupName").setValue(groupName);
    }

    public void addMember(String groupID, List<String> listID){
        databaseReference.child(groupID).child("member").setValue(listID);
    }
}
