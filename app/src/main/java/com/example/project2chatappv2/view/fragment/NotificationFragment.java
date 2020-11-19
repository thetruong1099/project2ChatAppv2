package com.example.project2chatappv2.view.fragment;

import android.content.Context;
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
import com.example.project2chatappv2.adapter.NotificationAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.FriendsViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;

    private NotificationAdapter notificationAdapter;

    private AuthencationViewModel authencationViewModel;
    private FriendsViewModel friendsViewModel;
    private UserViewModel userViewModel;

    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        getNotifiaction();
        return view;
    }

    private void getNotifiaction() {
        authencationViewModel = new ViewModelProvider(getActivity()).get(AuthencationViewModel.class);
        friendsViewModel = new ViewModelProvider(getActivity()).get(FriendsViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        authencationViewModel.getFirebaseUserMutableLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
                friendsViewModel.getInvitation(myID);
            }
        });
        friendsViewModel.getListIdMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (list!=null){
                    userViewModel.getListUser(list);
                    userViewModel.getListUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                        @Override
                        public void onChanged(List<UserModel> listUserModels) {
                            if (listUserModels!=null){
                                notificationAdapter = new NotificationAdapter(getContext(), listUserModels, myID);
                                recyclerView.setAdapter(notificationAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            }

                        }
                    });
                }


            }
        });

    }
}