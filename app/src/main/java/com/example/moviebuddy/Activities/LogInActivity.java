package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    TextView tvforgotPassword;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userID, isAdmin;
    EditText txt; // user input bar

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
        tvforgotPassword = findViewById(R.id.tvforgotPW);


        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogInActivity.this, "Yes?", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertName = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme);
                final EditText editTextName1 = new EditText(LogInActivity.this);
                // add line after initializing editTextName1
                editTextName1.setHint("Please Enter your email");
                editTextName1.setTextColor(Color.GRAY);

                alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Reset Password</font>"));
                // titles can be used regardless of a custom layout or not
                alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(LogInActivity.this);
                layoutName.setOrientation(LinearLayout.VERTICAL);
                layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (!(Patterns.EMAIL_ADDRESS.matcher(editTextName1.getText().toString()).matches())) {
                            editTextName1.setError("Please enter a valid email");
                            editTextName1.requestFocus();

                        }
                        else {
                            FirebaseAuth auth = FirebaseAuth.getInstance();

                            auth.sendPasswordResetEmail(editTextName1.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LogInActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(LogInActivity.this, "Please ensure you entered a valid email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }



                    }
                });

                alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel(); // closes dialog alertName.show() // display the dialog

                    }
                });


                alertName.show();




            }
        });

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

                if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                    etEmail.setError("Please enter a valid email");
                    etEmail.requestFocus();
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