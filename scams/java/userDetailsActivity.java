package com.example.scams;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class userDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button uploadImageButton;
    private Button uploadButton;

    private EditText nameEditText;
    private EditText emailIDEditText;
    private EditText registrationNumberEditText;
    private EditText smartCardEditText;
    private EditText classEditText;

    private ProgressBar progressBar;
    private Uri picUri;
    private Uri photoURI;

    private Upload upload;
    private String emailID;

    private RelativeLayout backgroundRelativeLayout;

    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    String mCurrentPhotoPath;

    private File getOutputMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp3",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadimg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
        takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = getOutputMediaFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI=FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if(!(nameEditText.getText().toString().equals("")) && !(registrationNumberEditText.getText().toString().trim().equals("")) && !(smartCardEditText.getText().toString().trim().equals("")) && !(classEditText.getText().toString().trim().equals(""))) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }else{
                    Toast.makeText(userDetailsActivity.this, "Please Enter all the details...", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Uri uri = photoURI;

            final StorageReference filepath = mStorage.child("userDetails").child(uri.getLastPathSegment());

            filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);

                    Toast.makeText(userDetailsActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloaduri= uri;

                            upload = new Upload(emailIDEditText.getText().toString().trim(), nameEditText.getText().toString().trim(), downloaduri.toString(), registrationNumberEditText.getText().toString().trim(), smartCardEditText.getText().toString().trim(), classEditText.getText().toString().trim());

                            String uploadID = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadID).setValue(upload);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress  =  (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(userDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Upload failed Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void afterSignUp() {
        finish();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setTitle("Registration Form");

        nameEditText = findViewById(R.id.nameEditText);
        emailIDEditText = findViewById(R.id.emailIDEeditText);
        registrationNumberEditText = findViewById(R.id.registrationNumberEditText);
        smartCardEditText = findViewById(R.id.smartCardEditText);
        classEditText = findViewById(R.id.classEditText);

        uploadImageButton = findViewById(R.id.uploadImageButton);
        uploadButton = findViewById(R.id.uploadButton);

        progressBar = new ProgressBar(this);

        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userDetails");

        uploadImageButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        backgroundRelativeLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == uploadImageButton){
            uploadimg();
        }
        if(view == uploadButton){
            uploadDB();
        }
        if(view == backgroundRelativeLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
    private void uploadDB() {
        Toast.makeText(this, "Registration complete", Toast.LENGTH_SHORT).show();
        afterSignUp();
    }
}
