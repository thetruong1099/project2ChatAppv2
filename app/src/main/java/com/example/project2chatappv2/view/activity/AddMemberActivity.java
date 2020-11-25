package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.AddMemberAdapter;
import com.example.project2chatappv2.adapter.ChoseMemberAdapter;
import com.example.project2chatappv2.adapter.SearchUserAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.GroupChatViewModel;
import com.example.project2chatappv2.viewModel.MemberGroupChatViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView btnSave;
    private EditText edtSearch;
    private Button btnAddMember;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewMember;

    private Intent intent;

    private Boolean status = false;

    private UserViewModel userViewModel;
    private MemberGroupChatViewModel memberGroupChatViewModel;
    private GroupChatViewModel groupChatViewModel;

    private AddMemberAdapter addMemberAdapter;
    private ChoseMemberAdapter choseMemberAdapter;

    private List<UserModel> listUser;
    private String groupID;
    private List<String> listIDMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edtSearch = findViewById(R.id.edtSearch);
        btnAddMember = findViewById(R.id.btnAddMember);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewMember = findViewById(R.id.recyclerViewMember);

        listUser = new ArrayList<>();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        memberGroupChatViewModel = new MemberGroupChatViewModel();

        intent = getIntent();
        status = intent.getBooleanExtra("status", false);
        groupID = intent.getStringExtra("groupID");
        listIDMember = intent.getStringArrayListExtra("listID");
        final String groupName = intent.getStringExtra("groupName");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == true){
                    Intent intent = new Intent(AddMemberActivity.this, AddGroupChatActivity.class);
                    intent.putExtra("groupName", groupName);
                    intent.putExtra("status", true);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(AddMemberActivity.this, GroupChatProfileActivity.class);
                    intent.putExtra("groupID", groupID);
                    startActivity(intent);
                    finish();
                }
            }
        });



        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, final int i, int i1, int i2) {
                userViewModel.searchUser(charSequence.toString().toLowerCase());
                userViewModel.getListUserMutableLiveData().observe(AddMemberActivity.this, new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(final List<UserModel> listUserModels) {
                        addMemberAdapter = new AddMemberAdapter(AddMemberActivity.this, listUserModels);
                        recyclerView.setAdapter(addMemberAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AddMemberActivity.this));
                        memberGroupChatViewModel.setListMutableLiveData(addMemberAdapter.getUserModelList());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberGroupChatViewModel.getListMutableLiveData().observe(AddMemberActivity.this, new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(List<UserModel> userModels) {
                        choseMemberAdapter = new ChoseMemberAdapter(AddMemberActivity.this, listUser);
                        recyclerViewMember.setAdapter(choseMemberAdapter);
                        recyclerViewMember.setLayoutManager(new LinearLayoutManager(AddMemberActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> listId = new ArrayList<>();
                ArrayList<String> listUsername = new ArrayList<>();
                ArrayList<String> listImage = new ArrayList<>();
                if (listUser!=null){
                    for(int i=0;i<listUser.size(); i++){
                        listId.add(listUser.get(i).getId());
                    }
        
                    for(int i=0;i<listUser.size(); i++){
                        listUsername.add(listUser.get(i).getUsername());
                    }

                    for(int i=0;i<listUser.size(); i++){
                        listImage.add(listUser.get(i).getImageURL());
                    }
                }

                if (status == true){
                    Intent intent = new Intent(AddMemberActivity.this, AddGroupChatActivity.class);
                    intent.putExtra("groupName", groupName);
                    intent.putExtra("status", true);
                    intent.putStringArrayListExtra("listID", listId);
                    intent.putStringArrayListExtra("listUsername", listUsername);
                    intent.putStringArrayListExtra("listImage", listImage);
                    startActivity(intent);
                    finish();
                }
                else {
                    listIDMember.addAll(listId);
                    if (listIDMember.size()>0){
                        groupChatViewModel = new ViewModelProvider(AddMemberActivity.this).get(GroupChatViewModel.class);
                        groupChatViewModel.addMember(groupID, listIDMember);
                        Intent intent = new Intent(AddMemberActivity.this, GroupChatProfileActivity.class);
                        intent.putExtra("groupID", groupID);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}