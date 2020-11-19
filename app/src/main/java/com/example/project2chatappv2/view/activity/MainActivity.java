package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    private LinearLayout layout_profile;
    private LinearLayout layout_search;
    private CircleImageView image_profile;
    private TextView tvUsername;

    private AuthencationViewModel authencationViewModel;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.mainfragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        layout_profile = findViewById(R.id.layout_profile);
        image_profile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);

        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                userViewModel.getUserProfile(firebaseUser.getUid());
            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserModelMutableLiveData().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                tvUsername.setText(userModel.getUsername());

                if(userModel.getImageURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(MainActivity.this).load(userModel.getImageURL()).into(image_profile);
                }
            }
        });

        layout_search = findViewById(R.id.layout_search);

        layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });
    }
}