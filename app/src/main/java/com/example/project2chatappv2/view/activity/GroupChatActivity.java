package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.MessageAdapter;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.GroupChatViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CircleImageView image_profile;
    private TextView tvUsername;
    private EditText edtChat;
    private ImageView btnSend;
    private ImageView btnDetail;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    private Intent intent;

    private GroupChatViewModel groupChatViewModel;
    private AuthencationViewModel authencationViewModel;
    private UserViewModel userViewModel;

    private String myID;
    private List<UserModel> listMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        btnBack = findViewById(R.id.btnBack);
        image_profile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        edtChat = findViewById(R.id.edtChat);
        btnSend = findViewById(R.id.btnSend);
        btnDetail = findViewById(R.id.btnDetail);
        recyclerView = findViewById(R.id.recyclerViewMessage);

        intent = getIntent();
        final String groupID = intent.getStringExtra("groupID");
        final String imageURL = intent.getStringExtra("imageURL");
        final String groupName= intent.getStringExtra("groupName");
        final ArrayList<String> listIDMember = intent.getStringArrayListExtra("member");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(imageURL.equals("default")){
            image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(this).load(imageURL).into(image_profile);
        }

        tvUsername.setText(groupName);


        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, GroupChatProfileActivity.class);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
                finish();
            }
        });

        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
            }
        });
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getListUser(listIDMember);
        userViewModel.getListUserMutableLiveData().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                listMember.addAll(userModels);
            }
        });

        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        groupChatViewModel.readMessage(groupID);
        groupChatViewModel.getListMessageMutableLiveData().observe(this, new Observer<List<MessageModel>>() {
            @Override
            public void onChanged(List<MessageModel> messageModels) {
                messageAdapter = new MessageAdapter(GroupChatActivity.this,messageModels ,listMember, myID);
                recyclerView.setAdapter(messageAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupChatActivity.this);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtChat.getText().toString();
                if (!message.equals("")){
                    groupChatViewModel.sendMessage(groupID, myID,message);
                }
                edtChat.setText("");
            }
        });
    }
}