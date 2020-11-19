package com.example.project2chatappv2.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView tvUsername;
    private ImageView btnBack;
    private Button btnChangeUsername;
    private Button btnLogout;

    private AuthencationViewModel authencationViewModel;
    private UserViewModel userViewModel;


    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;

    private String myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        image_profile = findViewById(R.id.image_profile);
        tvUsername = findViewById(R.id.tvUsername);
        btnBack = findViewById(R.id.btnBack);
        btnChangeUsername = findViewById(R.id.btnChangeUsername);
        btnLogout = findViewById(R.id.btnLogout);

        authencationViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        authencationViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                myId = firebaseUser.getUid();
                userViewModel.getUserProfile(myId);
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
                    Glide.with(UserProfileActivity.this).load(userModel.getImageURL()).into(image_profile);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlertDialog();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogout();
            }
        });
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
            userViewModel.uploadImage(imageUri, UserProfileActivity.this);
        }
    }

    private void displayAlertDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertDialog = layoutInflater.inflate(R.layout.custom_dialog_change_username, null);
        final EditText edtChangUsername = alertDialog.findViewById(R.id.edtChangeUsername);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Username");
        alert.setView(alertDialog);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username= edtChangUsername.getText().toString();
                userViewModel.updateUsername(username);
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void setLogout() {
        authencationViewModel.logout();
        authencationViewModel.getStatusLoginMutableleLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
            if (status){
                Intent intent = new Intent(UserProfileActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            }
        });

    }
}