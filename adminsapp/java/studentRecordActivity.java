package com.example.adminsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class studentRecordActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText subEditText;
    private Button searchButton;
    private String email;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_record);

        nameEditText = findViewById(R.id.nameEditText);
        subEditText = findViewById(R.id.subjectEditText);

        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("userDetails");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Log.i("name___", String.valueOf(child.child("name").getValue()));
                            if(String.valueOf(child.child("name").getValue()).equals(nameEditText.getText().toString().trim())){
                                email = String.valueOf(child.child("emailid").getValue());

                            }
                        }
                        Intent intent = new Intent(getApplicationContext(), attendanceDetailActivity.class);
                        //Toast.makeText(studentRecordActivity.this, "sdsdf "+email, Toast.LENGTH_SHORT).show();
                        intent.putExtra("name",email);
                        intent.putExtra("sub", subEditText.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
