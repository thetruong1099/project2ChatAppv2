package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.SearchUserAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    private EditText edtSearch;
    private RecyclerView recyclerView;
    private ImageView btnBack;

    private UserViewModel userViewModel;
    private SearchUserAdapter searchUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);


        edtSearch = findViewById(R.id.edtSearch);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerViewSearch);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userViewModel.searchUser(charSequence.toString().toLowerCase());
                userViewModel.getListUserMutableLiveData().observe(SearchUserActivity.this, new Observer<List<UserModel>>() {
                        @Override
                        public void onChanged(List<UserModel> listUserModels) {
                            searchUserAdapter = new SearchUserAdapter(SearchUserActivity.this, listUserModels, false);
                            recyclerView.setAdapter(searchUserAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this));
                        }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchUserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}