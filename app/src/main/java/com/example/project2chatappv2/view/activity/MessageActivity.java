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
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.MessageViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CircleImageView image_profile;
    private TextView tvUsername;
    private EditText edtChat;
    private ImageView btnSend;
    private ImageView btnDetail;

    private RecyclerView recyclerView;

    private Intent intent;

    private MessageViewModel messageViewModel;
    private AuthencationViewModel authencationViewModel;

    private  String myId;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btnBack = findViewById(R.id.btnBack);
        image_profile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        edtChat = findViewById(R.id.edtChat);
        btnSend = findViewById(R.id.btnSend);
        btnDetail = findViewById(R.id.btnDetail);
        recyclerView = findViewById(R.id.recyclerViewMessage);

        intent = getIntent();
        final String userReceiverId = intent.getStringExtra("userid");
        final String username = intent.getStringExtra("username");
        final String imageprofile = intent.getStringExtra("image_profile");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvUsername.setText(username);
        if(imageprofile.equals("default")){
            image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(this).load(imageprofile).into(image_profile);
        }

        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myId = firebaseUser.getUid();
            }
        });

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        messageViewModel.readMess(userReceiverId);

        messageViewModel.getListMessMutableLiveData().observe(MessageActivity.this, new Observer<List<MessageModel>>() {
            @Override
            public void onChanged(List<MessageModel> messageModels) {
                messageAdapter = new MessageAdapter(MessageActivity.this, messageModels, imageprofile, myId);
                recyclerView.setAdapter(messageAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = edtChat.getText().toString();
                if (!mess.equals("")){
                    messageViewModel.sendMess(userReceiverId, mess);
                }
                edtChat.setText("");
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, UserGuestActivity.class);
                intent.putExtra("userid", userReceiverId);
                intent.putExtra("username", username);
                intent.putExtra("image_profile", imageprofile);
                startActivity(intent);
            }
        });
    }
}