package com.example.project2chatappv2.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.MemberAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CircleImageView imageProfile;
    private TextView tvUsername;
    private TextView btnAdd;
    private TextView btnRemove;
    private RecyclerView recyclerView;

    private Intent intent;

    private UserViewModel userViewModel;
    private MemberAdapter memberAdapter;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_profile);

        btnBack = findViewById(R.id.btnBack);
        imageProfile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        btnAdd = findViewById(R.id.btnAddMember);
        btnAdd = findViewById(R.id.btnRemoveMember);
        recyclerView = findViewById(R.id.recyclerView);

        intent = getIntent();
        final String groupID = intent.getStringExtra("groupID");
        final String imageURL = intent.getStringExtra("imageURL");
        final String groupName= intent.getStringExtra("groupName");
        final ArrayList<String> listIDMember = intent.getStringArrayListExtra("member");

        if(imageURL.equals("default")){
            imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(this).load(imageURL).into(imageProfile);
        }

        tvUsername.setText(groupName);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getListUser(listIDMember);
        userViewModel.getListUserMutableLiveData().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                memberAdapter = new MemberAdapter(GroupChatProfileActivity.this, userModels);
                recyclerView.setAdapter(memberAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupChatProfileActivity.this));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatProfileActivity.this, GroupChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            imageUri = data.getData();

        }
    }
}