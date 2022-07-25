package com.example.firebaselearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText userNameText, emailText, passwordText;
    private Button submitBtn;
    private TextView registerLoginText;

    private boolean bLoginScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        registerLoginText = findViewById(R.id.registerLoginText);
        submitBtn = findViewById(R.id.submitBtn);

        bLoginScreen = false;
        
        View view = null;
        userNameText.setVisibility(view.GONE);

        // if user is already logged in, then open friends activity
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish();
        }

        registerLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bLoginScreen == true) {
                    // login screen
                    bLoginScreen = false;
                    submitBtn.setText("login");
                    userNameText.setVisibility(v.GONE);
                    registerLoginText.setText("Don't have an account? Register");
                }
                else if (bLoginScreen == false) {
                    // register screen
                    bLoginScreen = true;
                    submitBtn.setText("register");
                    userNameText.setVisibility(v.VISIBLE);
                    registerLoginText.setText("Already have an account? Login");
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if user data is not empty
                if(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
                    // check if we are in register mode
                    if(bLoginScreen == false && userNameText.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Empty boxes not accepted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(submitBtn.getText().toString().equals("login")) {
                    // user logging in
                    userLogin();
                }
                else if(submitBtn.getText().toString().equals("register")) {
                    // user creating an account
                    userRegister();
                }
            }
        });

    }

    private void userLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText.getText().toString().trim(), passwordText.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // open friends activity
                            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void userRegister() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            /*FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(new User(userNameText.getText().toString(), emailText.getText().toString(), ""));*/

                            // Database's URL
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-learning-cf4ae-default-rtdb.europe-west1.firebasedatabase.app/");
                            DatabaseReference myRef = database.getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                            myRef.setValue(new User(userNameText.getText().toString(), emailText.getText().toString(), ""));

                            // open friends activity
                            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                            Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}