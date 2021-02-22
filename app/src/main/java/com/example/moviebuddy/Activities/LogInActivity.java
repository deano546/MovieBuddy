package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity {

    //Allows the user to sign in
    //Adapted from https://github.com/bikashthapa01/firebase-authentication-android/blob/master/app/src/main/java/net/smallacademy/authenticatorapp/Login.java


    Button btnSignin, btnRegister;
    EditText etEmail, etPassword;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userID, isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        btnSignin = findViewById(R.id.btnSignin);
        btnRegister = findViewById(R.id.btngotoRegister);
        etEmail = findViewById(R.id.etsigninemail);
        etPassword = findViewById(R.id.etsigninpassword);

        //If the user is already signed in, send them to the main activity
        if(auth.getCurrentUser() != null) {

            userID = auth.getCurrentUser().getUid();

            DocumentReference documentReference = fStore.collection("Users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();


                        //This determines whether the user is sent to the regular section or the admin section
                        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LogInActivity.this, "Nothing retrieved" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }



        //Passes the email and password to firebase auth, which then determines if the details are correct
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                //Validation
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    etPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userID = auth.getCurrentUser().getUid();

                            Log.d("UID******",userID);
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();


                                        //This determines whether the user is sent to the regular section or the admin section
                                        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(LogInActivity.this, "Nothing retrieved" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}