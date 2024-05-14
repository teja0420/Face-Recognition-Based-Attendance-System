package com.example.adminsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class classRecordActivity extends AppCompatActivity {

    private ListView studentListView;
    private Button backButton;

    private List<String> studentList = new ArrayList<String>();
    private List<String> studentEmailList = new ArrayList<String>();

    private ArrayAdapter adapter;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_record);

        studentListView = findViewById(R.id.studentListView);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userDetails");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child: dataSnapshot.getChildren()){

                    for(DataSnapshot i : child.getChildren()) {

                        if (i.getKey().equals("name")) {
                            studentList.add(String.valueOf(i.getValue()));
                        }
                        if(i.getKey().equals("emailid")){
                            studentEmailList.add(String.valueOf(i.getValue()));
                        }
                    }

                }
                Log.i("strunfde valuee", String.valueOf(studentList));
                setList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setList() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList);
        studentListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!studentList.isEmpty()) {

            studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), studentDetailActivity.class);
                    intent.putExtra("name", studentEmailList.get(position));
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
