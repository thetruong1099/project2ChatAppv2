package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.FriendsViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class UserGuestActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private TextView tvUsername;
    private Button btnMakeFriend;
    private Button btnUnFriend;
    private ImageView btnBack;

    private Dialog dialog;

    private Intent intent;

    private FriendsViewModel friendsViewModel;
    private AuthencationViewModel authencationViewModel;

    private String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guest);

        imageProfile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        btnMakeFriend = findViewById(R.id.btnMakeFriend);
        btnUnFriend = findViewById(R.id.btnUnFriend);
        btnBack = findViewById(R.id.btnBack);

        intent = getIntent();
        final String userID = intent.getStringExtra("userid");
        String userName = intent.getStringExtra("username");
        String imageProfileURL = intent.getStringExtra("image_profile");

        if(imageProfileURL.equals("default")){
            imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(this).load(imageProfileURL).into(imageProfile);
        }

        tvUsername.setText(userName);

        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);

        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myID = firebaseUser.getUid();
                friendsViewModel.checkFriend(myID, userID);
            }
        });

        friendsViewModel.getStatusISFriendMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status == true) {
                    btnUnFriend.setVisibility(VISIBLE);
                    btnMakeFriend.setVisibility(View.GONE);
                }

                btnMakeFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendsViewModel.makeFriend(myID, userID);
                        displayDialog("Đã gửi yêu cầu kết bạn");
                    }
                });

                btnUnFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendsViewModel.removeFriendList(myID, userID);
                        friendsViewModel.removeFriendList(userID, myID);
                        displayDialog("Hủy kết bạn"); 
                    }
                });


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(UserGuestActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayDialog(String text) {
        dialog = new Dialog(UserGuestActivity.this);
        dialog.setTitle(text);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
    }
}