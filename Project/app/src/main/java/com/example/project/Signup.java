package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
        EditText userid,username,contact,password,confpassword;
        Button button;
    //declare firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //yeh error ku aa rha
        userid=(EditText) findViewById(R.id.userid);
        username = (EditText) findViewById(R.id.username);
        contact = (EditText) findViewById(R.id.contact);
        password = (EditText) findViewById(R.id.password);
        confpassword = (EditText) findViewById(R.id.password2);
        button= (Button) findViewById(R.id.rg_btn);

        // to access user node
        mDatabase = FirebaseDatabase.getInstance().getReference("/user");



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //create user with firebase
            mAuth.createUserWithEmailAndPassword(userid.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mAuth.getCurrentUser().getUid();
                    String id =  mAuth.getCurrentUser().getUid();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(getApplicationContext(),Home.class));
                    user_data data = new user_data(userid.getText().toString(),contact.getText().toString(),id,username.getText().toString());
                    //firebase data base
                    mDatabase.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                            //mdialog.dismiss();
                            mAuth.getCurrentUser().getUid();
                            String id =  mAuth.getCurrentUser().getUid();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //set Users display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username.getText().toString())
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(getApplicationContext(),Home.class));
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Home.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("","error "+e);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //mdialog.dismiss();
                    Toast.makeText(getApplicationContext(),"problem"+e,Toast.LENGTH_SHORT).show();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  //  mdialog.dismiss();
                    Toast.makeText(getApplicationContext(),"problem"+e,Toast.LENGTH_SHORT).show();
                }
            });

        }
    });




    }
}