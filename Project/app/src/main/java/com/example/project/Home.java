package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Bitmap myBitmap;
    Uri picUri;
    //define button
    Button cameraa, galerry;
    ImageView click_image_id;

    private static final int pic_id = 123;
    private static final int gallery_id = 111;
    private static final int  STORAGE_PERMISSION_CODE = 100;
    private static final int  CAMERA_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        cameraa = findViewById(R.id.mCameraButton);
        galerry = findViewById(R.id.mGalleryButton);
        click_image_id = findViewById(R.id.img_profile);
        checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

        cameraa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);

                // Start the activity with camera_intent,
                // and request pic id
                startActivityForResult(camera_intent, pic_id);
            }
        });

        galerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openGallery();
            }
        });


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, gallery_id);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Home.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(Home.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Home.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(Home.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Home.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(Home.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


        public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            //check if user is logged in then logout else tell he/she can't logout
            case R.id.logout: {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser == null)
                {
                    Toast.makeText(getApplicationContext(),"You Cannot logout until you login",Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), Welcome.class));
                }

            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // Set the image in imageview  for display
            click_image_id.setImageBitmap(photo);
        }
        if(requestCode==gallery_id){
            picUri = data.getData();
            click_image_id.setImageURI(picUri);
        }
    }

    }