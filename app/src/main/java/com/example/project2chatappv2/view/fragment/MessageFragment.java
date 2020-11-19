package com.example.project2chatappv2.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.ReceiverMessageAdapter;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.SearchUserActivity;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.MessageViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;

    private AuthencationViewModel authencationViewModel;
    private UserViewModel userViewModel;
    private MessageViewModel messageViewModel;
    private ReceiverMessageAdapter receiverMessageAdapter;

    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        getMessage();

        return view;
    }

    private void getMessage() {
        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
            }
        });
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.receiverMessage();
        messageViewModel.getListUserIdMutableLiveData().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(final List<String> listUserID) {
                userViewModel.getListUser(listUserID);
                userViewModel.getListUserMutableLiveData().observe(getActivity(), new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(final List<UserModel> listUsers) {
                        messageViewModel.getLastMessage(listUserID, myID);
                        messageViewModel.getListMessMutableLiveData().observe(getActivity(), new Observer<List<MessageModel>>() {
                            @Override
                            public void onChanged(List<MessageModel> listLastMessage) {
                                receiverMessageAdapter = new ReceiverMessageAdapter(listUsers, getContext(), listLastMessage, myID);
                                recyclerView.setAdapter(receiverMessageAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            }
                        });
                    }
                });
            }
        });
    }
}