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

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailLogin;
    private EditText edtPasswordLogin;
    private Button btnLogin;

    private AuthencationViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin =findViewById(R.id.btnLogin);

        loginViewModel = new ViewModelProvider(this).get(AuthencationViewModel.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmailLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();

                if (email.length()>0&&password.length()>0){
                    loginViewModel.login(email, password);
                }

            }
        });

        loginViewModel.getStatusLoginMutableleLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean statusLogin) {
                if (statusLogin){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}