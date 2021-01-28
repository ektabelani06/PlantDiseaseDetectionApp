package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        getSupportActionBar().hide(); //hide the title bar
        t = findViewById(R.id.acc);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EnterDetail.class));
            }
        });
    }
    public void addListenerOnButton1(View v) {
        Intent intent = new Intent(Welcome.this, LoginPage.class);
        startActivity(intent);


    }
    public void addListenerOnButton2(View v) {
        Intent intent = new Intent(Welcome.this, Signup.class);
        startActivity(intent);
    }

    public void addListenerOnButton3(View v) {
        Intent intent = new Intent(Welcome.this, ForgetPassword.class);
        startActivity(intent);
    }

    //public void openPage(){
        //Intent intent = new Intent(Welcome.this, LoginPage.class);
        //startActivity(intent);
    //}

   // public void openPage1(){
     //   Intent intent1 = new Intent(Welcome.this, Signup.class);
       // startActivity(intent1);
    //}



}