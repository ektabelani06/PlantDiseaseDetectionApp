package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText username,password;
    Button login;
    boolean isPasswordValid;
    boolean isusername;

    //declare firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firebase sign_in code
                //Sign in with email and password
                //start of login code
                mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // mdialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), Home.class));

                            Toast.makeText(getApplicationContext(), "login complete", Toast.LENGTH_SHORT).show();


                        } else {
                          //  mdialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                // end of login code itna part tere set validation method ke ander dalna jaha sab successs hone ke baad chalna cvhaiye
                // ismei ab tk loading box nahi dalei hai baadmei dal denge

                SetValidation();
            }
        });
    }
        public void SetValidation() {
            if (username.getText().toString().isEmpty()){
                username.setError(getResources().getString(R.string.name_error));
                isusername = false;
            }
           else if (password.getText().toString().isEmpty()) {
                password.setError(getResources().getString(R.string.password_error));
                isPasswordValid = false;
            } else if (password.getText().length() < 6) {
                password.setError(getResources().getString(R.string.error_invalid_password));
                isPasswordValid = false;
            } else {
                isPasswordValid = true;
            }
            if (isusername && isPasswordValid) {
                Intent intent = new Intent(LoginPage.this, Home.class);


                //basically yaha ayega code
                // ye wala intent nikal dena
                //similar;y in registration
                startActivity(intent);
            }
        }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
    }

}

