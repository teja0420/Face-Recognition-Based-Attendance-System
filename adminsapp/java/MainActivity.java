package com.example.adminsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText emailidEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailidEditText = findViewById(R.id.emailidEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailidEditText.getText().toString().trim();
                String pass = passwordEditText.getText().toString().trim();

                if(email.equals("admin@gmail.com") && pass.equals("admin123")){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
            }
        });

    }
}