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

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.MemberAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.GroupChatViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AddGroupChatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView btnSave;
    private ImageView btnAddMember;
    private EditText edtGroupName;
    private RecyclerView recyclerView;

    private ArrayList<String> listId;
    private ArrayList<String> listUsername;
    private ArrayList<String> listImage;
    private List<UserModel> listUser;

    private MemberAdapter memberAdapter;

    private AuthencationViewModel authencationViewModel;
    private GroupChatViewModel groupChatViewModel;

    private String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnAddMember = findViewById(R.id.btnAddGroupMember);
        edtGroupName = findViewById(R.id.edtGroupname);
        recyclerView = findViewById(R.id.recyclerView);

        listId = new ArrayList<>();
        listUsername = new ArrayList<>();
        listImage = new ArrayList<>();
        listUser = new ArrayList<>();

        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);

        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
            }
        });

        setEdtGroupName();
        setRecycleView();
        setButtonEvent();


    }

    private void setEdtGroupName(){
        Intent intent = getIntent();
        String groupName= intent.getStringExtra("groupName");
        Boolean stastus = intent.getBooleanExtra("status", false);
        listId = intent.getStringArrayListExtra("listID");
        listUsername = intent.getStringArrayListExtra("listUsername");
        listImage = intent.getStringArrayListExtra("listImage");
        if (listId!=null){
            for(int i=0; i<listId.size();i++){
                listUser.add(new UserModel(listUsername.get(i), listImage.get(i)));
            }
        }

        if (stastus==true){
            edtGroupName.setText(groupName);
        }

    }

    private void setRecycleView(){
        memberAdapter = new MemberAdapter(AddGroupChatActivity.this, listUser);
        recyclerView.setAdapter(memberAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddGroupChatActivity.this));
    }

    private void setButtonEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGroupChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGroupChatActivity.this, AddMemberActivity.class);
                intent.putExtra("groupName", edtGroupName.getText().toString());
                intent.putExtra("status", true);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupChatViewModel.createGroupChat(edtGroupName.getText().toString(), myID, listId);
                Intent intent = new Intent(AddGroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}