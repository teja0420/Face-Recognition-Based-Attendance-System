package com.example.adminsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class attendanceDetailActivity extends AppCompatActivity {

    private String email, sub;
    private ListView attendanceListView;
    private Button backSubButton;

    private DatabaseReference mDatabaseRef;
    private List<String> attendanceList = new ArrayList<String>();

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);

        Intent intent = getIntent();
        email = intent.getStringExtra("name");
        sub = intent.getStringExtra("sub");
        //Toast.makeText(this, email, Toast.LENGTH_SHORT).show();


        attendanceListView = findViewById(R.id.attendanceListView);
        backSubButton = findViewById(R.id.backSubButton);

        backSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attendanceDetails");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child: dataSnapshot.getChildren()){
//                    Log.i("valueeeeeeeeee",String.valueOf(child.child("date").getValue()));
//                        Log.i("dataa",String.valueOf(child.getValue()));
//                        Log.i("email",email);
//                    Log.i("sub",sub);

                    if(child.child("userName").getValue().equals(email) && child.child("subject").getValue().equals(sub) ){
                        Log.i("valueeeeeeeeee", String.valueOf(child.child("date").getValue()));
                        String dataaa = String.valueOf(child.child("date").getValue());
                        dataaa = "Date: "+dataaa.replace("_","   Time: ");
                        attendanceList.add(dataaa);

                    }

                }
                Log.i("finalll", String.valueOf(attendanceList));
                showData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData() {

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, attendanceList);
        attendanceListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
