package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logIn extends AppCompatActivity {
    EditText emailId,password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseAuth  = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextmail);
        password = findViewById(R.id.editTextpassword);
        btnSignUp = findViewById(R.id.buttonsignup);
        tvSignIn = findViewById(R.id.textViewsignin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email Id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(logIn.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(logIn.this, "Signup Unsuccessful,Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(logIn.this, PickTravelModeActivity.class));
                            }
                        }
                    });

                } else {
                    Toast.makeText(logIn.this, "Error Occurred !", Toast.LENGTH_SHORT).show();

                }

            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logIn.this,loggedIn.class);
                startActivity(intent);
            }
        });





    }
}