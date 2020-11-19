package com.example.project2chatappv2.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.adapter.FriendAdapter;
import com.example.project2chatappv2.adapter.NotificationAdapter;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.UserProfileActivity;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.FriendsViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FrientsFragment extends Fragment {
    private RecyclerView recyclerView;

    private AuthencationViewModel authencationViewModel;
    private FriendsViewModel friendsViewModel;
    private UserViewModel userViewModel;

    private FriendAdapter friendAdapter;
    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_frients, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        authencationViewModel = new ViewModelProvider(getActivity()).get(AuthencationViewModel.class);
        friendsViewModel = new ViewModelProvider(getActivity()).get(FriendsViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        authencationViewModel.getFirebaseUserMutableLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
                friendsViewModel.getListFriend(myID);
            }
        });

        friendsViewModel.getListIdMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (list!=null){
                    userViewModel.getListUser(list);
                    userViewModel.getListUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                        @Override
                        public void onChanged(List<UserModel> userModels) {
                            friendAdapter = new FriendAdapter(getContext(), userModels);
                            recyclerView.setAdapter(friendAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    });
                }

            }
        });
        return view;
    }
}