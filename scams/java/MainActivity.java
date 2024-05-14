package com.example.scams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //defining view objects
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;

    private TextView textViewSignin;

    //private String email;

    private ProgressDialog progressDialog;

    private LinearLayout backgroundRelativeLayout;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("S.C.A.M.S");
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }


        backgroundRelativeLayout = (LinearLayout) findViewById(R.id.backgroundRelativeLayout);

        //initializing views
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        signupButton = (Button) findViewById(R.id.signupButton);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        signupButton.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        backgroundRelativeLayout.setOnClickListener(this);

    }


    private void registerUser(){

        //getting email and password from edit texts
        final String email = emailEditText.getText().toString().trim();
        String password  = passwordEditText.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //finish();
                            Intent intent = new Intent(MainActivity.this,userDetailsActivity.class);
                            startActivity(intent);

                        }else{
                            //display some message here
                            Toast.makeText(MainActivity.this,"Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == signupButton){
            registerUser();
            Intent intent = new Intent(this, userDetailsActivity.class);
            startActivity(intent);
        }
        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == backgroundRelativeLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
