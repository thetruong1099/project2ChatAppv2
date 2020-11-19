package com.example.project2chatappv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project2chatappv2.R;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;

public class StartActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;

    private AuthencationViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnLogin = findViewById(R.id.btnLoginSA);
        btnRegister = findViewById(R.id.btnRegisterSA);

        loginViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);

        loginViewModel.getStatusLoginMutableleLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean statusLogin) {
                if (statusLogin){
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}