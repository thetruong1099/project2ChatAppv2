package com.example.project2chatappv2.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.GroupChatAdapter;
import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.view.activity.AddGroupChatActivity;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.GroupChatViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class GroupFragment extends Fragment {

    private ImageView btnCreateGroupChat;
    private RecyclerView recyclerView;

    private GroupChatViewModel groupChatViewModel;
    private AuthencationViewModel authencationViewModel;

    private GroupChatAdapter groupChatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_group, container, false);

        btnCreateGroupChat = view.findViewById(R.id.btnCreateGroupChat);
        recyclerView = view.findViewById(R.id.recyclerView);

        btnCreateGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddGroupChatActivity.class);
                startActivity(intent);
            }
        });



        groupChatViewModel = new ViewModelProvider(getActivity()).get(GroupChatViewModel.class);
        authencationViewModel = new ViewModelProvider(getActivity()).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                groupChatViewModel.getGroupChat(firebaseUser.getUid());
            }
        });

        groupChatViewModel.getListGroupMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<GroupChatModel>>() {
            @Override
            public void onChanged(List<GroupChatModel> groupChatModels) {

                groupChatAdapter = new GroupChatAdapter(getContext(), groupChatModels);
                recyclerView.setAdapter(groupChatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return view;
    }
}