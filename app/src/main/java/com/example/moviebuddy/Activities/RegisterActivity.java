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

import com.example.moviebuddy.PrefManager;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //This activity allows a user to register
    //Based on https://github.com/bikashthapa01/firebase-authentication-android/blob/master/app/src/main/java/net/smallacademy/authenticatorapp/Register.java

    Button btnRegister, btnLogin, btnIntro;
    EditText etEmail, etName, etPassword;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userID;
    int SQLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        btnRegister = findViewById(R.id.btnRegisterNew);
        btnLogin = findViewById(R.id.btngotoLogIn);
        etEmail = findViewById(R.id.etregisteremail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etregisterpassword);
        btnIntro = findViewById(R.id.btnIntro);

        //If the user is already signed in, send them to the main activity
        if(auth.getCurrentUser() != null) {

            userID = auth.getCurrentUser().getUid();

            DocumentReference documentReference = fStore.collection("Users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Nothing retrieved" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        //Launches login activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        //Show intro slides
        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code based on: IS4407 Lecture Material, Gleeson Michael
                //Code from: Microsoft Team -> Teams -> IS4447 Tutorial Series -> File -> Bonus - Happy Christmas
                PrefManager PrefManagerActivity = new PrefManager(getApplicationContext());
                // make first time launch TRUE
                PrefManagerActivity.setFirstTimeLaunch(true);
                startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
                finish();
                //End Code
            }
        });

        //Validate the user has entered details, then send them to firestore
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        final String fullName = etName.getText().toString();

        //Validation
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPassword.setError("Password is Required.");
            return;
        }

        if(TextUtils.isEmpty(fullName)){
            etName.setError("Name is Required.");
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Password Must be >= 6 Characters");
            return;
        }


        //Creating a user with firebase auth, I also create a user object in the Cloud Firestore so I have a corresponding table there
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                    JSONParser jsonParser= new JSONParser();

                    jsonParser.createUser(RegisterActivity.this, new JSONParser.CreateUserResponseListener() {
                        @Override
                        public void onError(String message) {
                            //Toast.makeText(RegisterActivity.this, "Error - CreateUser", Toast.LENGTH_SHORT).show();

                            jsonParser.SearchUsers(RegisterActivity.this, new JSONParser.SearchUsersResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(RegisterActivity.this, "Error - SearchUsers", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(List<User> userlist) {
                                    SQLID = userlist.get(0).getId();

                                    userID = auth.getCurrentUser().getUid();

                                    DocumentReference documentReference = fStore.collection("Users").document(userID);
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("username",fullName);
                                    user.put("email",email);
                                    user.put("id",SQLID);
                                    //Insert new user
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("SuccessStore", "onSuccess: user Profile is created for "+ userID);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("FailStore", "onFailure: " + e.toString());
                                        }
                                    });

                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }
                            },fullName,1);
                        }

                        @Override
                        public void onResponse(String username) {

                        }
                    }, email, fullName);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
});} }


