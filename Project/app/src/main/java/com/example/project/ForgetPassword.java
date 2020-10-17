package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText fp_email;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mAuth = FirebaseAuth.getInstance();
        fp_email = findViewById(R.id.contact);
        btn = findViewById(R.id.fp_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(fp_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Mail has been sent to your mail id to reset your Password", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginPage.class));


                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), "Error Occured" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }
}