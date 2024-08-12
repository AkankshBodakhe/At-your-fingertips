package com.example.atyourfingertips;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if email is stored in SharedPreferences
        if (sharedPreferences.contains("email")) {
            startActivity(new Intent(Login.this, HomeScreen.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button btn_signup = findViewById(R.id.btn_login_signup);
        EditText edt_email = findViewById(R.id.edt_email);
        EditText edt_password = findViewById(R.id.edt_password);
        Button btn_login = findViewById(R.id.btn_login);
        Button location = (Button)findViewById(R.id.btn_location_signin);



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();

                if (email.isEmpty()) {
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    edt_password.setError("Password is required");
                    edt_password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Store email in SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.apply();

                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,HomeScreen.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Invalid credentials",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Location_login.class));
            }
        });



    }
    @Override
    public void onBackPressed() {
        // Finish the activity when back button is pressed
        super.onBackPressed();
        finishAffinity();
    }
}
