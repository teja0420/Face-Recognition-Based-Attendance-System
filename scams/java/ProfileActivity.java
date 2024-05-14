package com.example.scams;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener{

    //firebase auth object

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Uri photoURI;
    String mCurrentPhotoPath;

    private attendanceUpload attendance_Upload;

    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRefUser;

    private Upload info;
    private String regNO;

    private ProgressBar progressBar;
    private ProgressBar mProgressBar;
    //private int mProgressStatus = 0;
    //private Handler mHandler = new Handler();

    private ProgressDialog progressDialog;

    private int EnterKey=0;
    private Uri ImageURL;

    static final int REQUEST_IMAGE_CAPTURE=1;
    private String sub;
    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button attendanceButton;
    private Button viewRecordButton;

    private String userID;
    private String name;
    private String temp="123";

    public String registrationNumber;


    private File getOutputMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void captureFace(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    private void qrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(0);
            }
        },500);


        switch (requestCode) {
            case 49374:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (result != null) {

                    if (result.getContents() == null) {

                        Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();

                    } else {

                        sub = result.getContents();
                        String[] ttt = sub.split("-");
                        sub = ttt[0];
                        captureFace();

                    }

                } else {

                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();

                }

            case CAMERA_REQUEST:

                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    EnterKey = 1;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String name_file = randomGenerator();
                    StorageReference reference = FirebaseStorage.getInstance().getReference("attendanceDetails").child(name_file);
                    final UploadTask uploadTask = reference.putBytes(imageBytes);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ImageURL = uri;
                                    final DateFormat sdf = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
                                    Date date = new Date();

                                    String datee = sdf.format(date);

                                    attendance_Upload = new attendanceUpload(ImageURL.toString(),registrationNumber,sub,firebaseAuth.getInstance().getCurrentUser().getEmail(),datee);

                                    String attendanceuploadID = mDatabaseRef.push().getKey();

                                    mDatabaseRef.child(attendanceuploadID).setValue(attendance_Upload);
                                    //progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
                else{
                    Log.i("content","upload faied");
                    //Toast.makeText(this, "Upload failed Please try again", Toast.LENGTH_SHORT).show();

                }
        }
    }

    private String randomGenerator() {

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        isStoragePermissionGranted();
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attendanceDetails");
        mDatabaseRefUser = FirebaseDatabase.getInstance().getReference("userDetails");

        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        progressBar = new ProgressBar(this);


        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        attendanceButton = (Button) findViewById(R.id.attendanceButton);
        viewRecordButton = (Button) findViewById(R.id.viewRecordButton);

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null){
//                    Log.i("step0","complete");
//                }else{
//
//                }
//            }
//        };


        //progressDialog.setMessage("Registering Please Wait...");
        //progressDialog.show();
        mDatabaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if(child.child("emailid").getValue(String.class).equals(firebaseAuth.getInstance().getCurrentUser().getEmail())){
                        registrationNumber = String.valueOf(child.child("registrationNumber").getValue());
                        name = String.valueOf(child.child("name").getValue());
                        break;
                    }
                    //displaying logged in user name
                    Toast.makeText(ProfileActivity.this, name, Toast.LENGTH_SHORT).show();
                }

                setTitle(name+" Feed");
                name = "Welcome "+name;
                textViewUserEmail.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("step-1","complete");
            }
        });

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        attendanceButton.setOnClickListener(this);
        viewRecordButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            name = "";
            startActivity(new Intent(this, LoginActivity.class));
        }else if(view == attendanceButton){

            qrScanner();

        }else if(view == viewRecordButton){

            Intent intent = new Intent(this,viewRecordActivity.class);
            startActivity(intent);

        }



    }

}
