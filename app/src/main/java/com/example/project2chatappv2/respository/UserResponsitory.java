package com.example.project2chatappv2.respository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.airbnb.lottie.animation.content.Content;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.UserProfileActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserResponsitory {

    private Application application;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private MutableLiveData<UserModel> userModelMutableLiveData;

    private StorageReference storageReference;

    private MutableLiveData<List<UserModel>> listUserMutableLiveData;
    private List<UserModel> listUsers;

    public UserResponsitory(Application application) {
        this.application = application;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userModelMutableLiveData = new MutableLiveData<>();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        listUserMutableLiveData = new MutableLiveData<>();
        listUsers = new ArrayList<>();

    }

    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    public MutableLiveData<List<UserModel>> getListUserMutableLiveData() {
        return listUserMutableLiveData;
    }

    public void getUserProfile(String id){
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                userModelMutableLiveData.postValue(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getListUserProfile(final List<String> listId){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsers.clear();
                for (String id: listId){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if(id.equals(userModel.getId())) listUsers.add(userModel);
                    }
                }
                if (listUsers.size()>0){
                    listUserMutableLiveData.postValue(listUsers);
                }
                else {
                    listUsers.clear();
                    listUserMutableLiveData.setValue(listUsers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUsername(String username){
        reference.child(firebaseUser.getUid()).child("username").setValue(username);
        reference.child(firebaseUser.getUid()).child("search").setValue(username.toLowerCase());
    }

    private String getFIleExtension(Uri uri, Context context){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(Uri imageUri, final Context context){
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
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL", photoLink);
                            reference.updateChildren(map);
                        }
                    }
                }
            });
        }
    }

    public void searchUsers (String keyword){
        if (keyword.length()!=0){
            Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search").startAt(keyword).endAt(keyword+"\uf8ff");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listUsers.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (!userModel.getId().equals(firebaseUser.getUid())){
                            listUsers.add(userModel);
                        }
                    }
                    listUserMutableLiveData.postValue(listUsers);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            listUsers.clear();
            listUserMutableLiveData.setValue(listUsers);
        }
    }
}
