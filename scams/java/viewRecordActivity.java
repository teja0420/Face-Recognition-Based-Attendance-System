package com.example.scams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewRecordActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabaseRef;
    private List<String> subjectList = new ArrayList<String>();
    private Button backBut;

    private TextView subject_dbms;
    private TextView subject_aoa;
    private TextView subject_nlp;
    public int length_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attendanceDetails");
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    //attendanceUpload details = dataSnapshot.getValue(attendanceUpload.class);

                    if (child.child("userName").getValue(String.class).equals(currentFirebaseUser.getEmail())){

                        subjectList.add(child.child("subject").getValue(String.class));

                    }

                }
                length_string = subjectList.size();

                display();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(viewRecordActivity.this, "Data extraction failed, please try again later...", Toast.LENGTH_SHORT).show();
            }
        });

        subject_dbms = (TextView) findViewById(R.id.subject_DBMS);
        subject_aoa = (TextView) findViewById(R.id.subject_AOA);
        subject_nlp = (TextView) findViewById(R.id.subject_NLP);
        backBut = (Button) findViewById(R.id.backButton);

        backBut.setOnClickListener(this);
    }

    private void display() {

        int dbms=0;
        int aoa=0;
        int nlp=0;

        //Iterator i = subjectList.iterator();

        for(int coun=0; coun < length_string; coun++){
            //while(i.hasNext()){
            //Log.i("value", (String) i.next());

            String answer = subjectList.get(coun);

            if(answer.equals("DBMS")){
                dbms++;
            }

            if(answer.equals("SQL")){
                aoa++;
            }

            if(answer.equals("AI")){
                nlp++;
            }
        }

        String str_dbms = "DBMS : "+String.valueOf(dbms)+"/10";
        String str_aoa = "SQL : "+String.valueOf(aoa)+"/10";
        String str_nlp = "AI : "+String.valueOf(nlp)+"/10";

        subject_dbms.setText(str_dbms);
        subject_aoa.setText(str_aoa);
        subject_nlp.setText(str_nlp);

    }


    @Override
    public void onClick(View view) {
        if(view == backBut){

            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);

        }
    }
}
