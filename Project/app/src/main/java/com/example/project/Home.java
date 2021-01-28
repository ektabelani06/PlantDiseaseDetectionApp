package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Bitmap myBitmap;
    private int mInputSize = 224;
    private String mModelPath = "converted_model.tflite";
    private String mLabelPath = "labels.txt";
    private Classifier classifier;


    //define button
    Button cameraa, galerry;


    private Bitmap bitmap;
    private List<String> labels;
    ImageView imageView;
    Uri imageuri;
    Button buclassify;
    TextView classitext ,knowmore;




    //test start

//test end

    private static final int pic_id = 123;
    private static final int gallery_id = 111;
    private static final int  STORAGE_PERMISSION_CODE = 100;
    private static final int  CAMERA_PERMISSION_CODE = 101;

//    FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
//            .setAssetFilePath("your_model.tflite")
//            .build();
//
//











        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        cameraa = findViewById(R.id.mCameraButton);
        galerry = findViewById(R.id.mGalleryButton);
        imageView = findViewById(R.id.img_profile);
        buclassify=(Button)findViewById(R.id.mDetectButton);
        classitext=(TextView)findViewById(R.id.mResultTextView);
        knowmore = findViewById(R.id.btn_knowmore);
        knowmore.setVisibility(View.GONE);

        try
        {
            initClassifier();


        }catch (IOException e){

            e.printStackTrace();
        }

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


//test code
//        try {
//            classifier = new ImageClassifier((Activity) getApplicationContext());
//        } catch (IOException e) {
//            Log.e("my log", "Failed to initialize an image classifier.");
//        }
        //startBackgroundThread();

        buclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Classifier.Recognition> result = classifier.recognizeImage(bitmap);
                Toast.makeText(getApplicationContext(),result.get(0).toString(),Toast.LENGTH_SHORT).show();
                classitext.setText(result.get(0).toString());
                if(result.get(0).toString() == "" || result.get(0).toString() == "background" || result.get(0).toString() =="unknown" )
                {
                    //Do Nothing
                }else
                    {
                        knowmore.setVisibility(View.VISIBLE);
                        knowmore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(getApplicationContext(),result.get(0).toString(),Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), Disease_detail_activity.class);
                                intent.putExtra("disease_id", result.get(0).toString());
                                startActivity(intent);
                            }
                        });
                    }


            }
        });



    }




    private void initClassifier() throws IOException
    {
        classifier = new Classifier(getAssets(),mModelPath,mLabelPath,mInputSize);
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
            Bitmap bitmap = (Bitmap) data.getExtras()
                    .get("data");


            // Set the image in imageview  for display
            imageView.setImageBitmap(bitmap);
        }
        if(requestCode==gallery_id){
            imageuri= data.getData();
           //imageView.setImageURI(imageuri);
            //test code start
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //test code end
        }
    }


    public void addListenerOnButton5(View v) {
        Intent intent = new Intent(Home.this, chat_activity.class);
        startActivity(intent);
    }
    }