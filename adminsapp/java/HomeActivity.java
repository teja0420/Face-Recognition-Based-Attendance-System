package com.example.adminsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button classRecordButton;
    private Button studentRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        classRecordButton = findViewById(R.id.classRecordButton);
        studentRecordButton = findViewById(R.id.studentDetailButton);

        classRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.adminsapp.classRecordActivity.class));
            }
        });

        studentRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.adminsapp.studentRecordActivity.class));
            }
        });

    }
}
