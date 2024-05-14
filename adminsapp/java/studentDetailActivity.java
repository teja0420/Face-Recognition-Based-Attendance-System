package com.example.adminsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class studentDetailActivity extends AppCompatActivity {

    private String email;
    private ListView subjectListView;
    private Button backNameButton;
    private List<String> subjectist = new ArrayList<String>();

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Intent intent = getIntent();
        email = intent.getStringExtra("name");

        backNameButton = findViewById(R.id.backNameButton);
        backNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        subjectListView = findViewById(R.id.subjectListView);
        subjectist.add("DBMS");
        subjectist.add("SQL");
        subjectist.add("AI");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectist);
        subjectListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!subjectist.isEmpty()) {

            subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), attendanceDetailActivity.class);
                    intent.putExtra("name",email);
                    intent.putExtra("sub", subjectist.get(position));
                    startActivity(intent);
                    finish();
                }
            });

        }



    }
}
