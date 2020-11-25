package com.example.project2chatappv2.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.MemberAdapter;
import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.GroupChatViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CircleImageView imageProfile;
    private TextView tvUsername;
    private ImageView btnEditGroupName;
    private Button btnAdd;
    private Button btnRemove;
    private RecyclerView recyclerView;

    private Intent intent;

    private UserViewModel userViewModel;
    private GroupChatViewModel groupChatViewModel;
    private MemberAdapter memberAdapter;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;

    private String groupID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_profile);

        btnBack = findViewById(R.id.btnBack);
        imageProfile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        btnEditGroupName = findViewById(R.id.btnEditGroupName);
        btnAdd = findViewById(R.id.btnAddMember);
        btnRemove = findViewById(R.id.btnRemoveMember);
        recyclerView = findViewById(R.id.recyclerView);

        intent = getIntent();
        groupID = intent.getStringExtra("groupID");
        final ArrayList<String> listIDMember = new ArrayList<>();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        groupChatViewModel.getGroupChatProfile(groupID);
        groupChatViewModel.getGroupChatModelMutableLiveData().observe(this, new Observer<GroupChatModel>() {
            @Override
            public void onChanged(GroupChatModel groupChatModel) {
                String imageURL = groupChatModel.getImageURL();
                if(imageURL.equals("default")){
                    imageProfile.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(GroupChatProfileActivity.this).load(imageURL).into(imageProfile);
                }
                String groupName = groupChatModel.getGroupName();
                tvUsername.setText(groupName);

                listIDMember.addAll(groupChatModel.getMember());
                userViewModel.getListUser(listIDMember);
            }
        });

        userViewModel.getListUserMutableLiveData().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                System.out.println(userModels.size());
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

        btnEditGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatProfileActivity.this, AddMemberActivity.class);
                intent.putExtra("status", false);
                intent.putExtra("groupID", groupID);
                intent.putStringArrayListExtra("listID", listIDMember);
                startActivity(intent);
                finish();
            }
        });

    }

    private void displayAlertDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertDialog = layoutInflater.inflate(R.layout.custom_dialog_change_username, null);
        final EditText edtChangUsername = alertDialog.findViewById(R.id.edtChangeUsername);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Groupname");
        alert.setView(alertDialog);
        alert.setCancelable(false);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupname= edtChangUsername.getText().toString();
                groupChatViewModel.updateGroupName(groupID, groupname);
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
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
            groupChatViewModel.uploadImage(imageUri, GroupChatProfileActivity.this, groupID);
        }
    }
}