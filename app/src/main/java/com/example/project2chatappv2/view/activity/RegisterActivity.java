package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsernameRegister;
    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;
    private Button btnRegister;

    private AuthencationViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtUsernameRegister = findViewById(R.id.edtUsernameRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        registerViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsernameRegister.getText().toString();
                String email = edtEmailRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString();

                if(username.length()>0 && email.length()>0 &&password.length()>0){
                    registerViewModel.register(username, email, password);
                }
            }
        });

        registerViewModel.getStatusLoginMutableleLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean statusRegister) {
                if(statusRegister){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}