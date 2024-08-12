package com.example.atyourfingertips;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.User;


public class SignUp extends AppCompatActivity {

    EditText edt_user_name, edt_email, edt_password, edt_confirm_password;
    Button btn_signup,location_desk;
    private FirebaseAuth mAuth;;

    FirebaseDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        edt_user_name = findViewById(R.id.edt_signup_user_name);
        edt_email = findViewById(R.id.edt_signup_email);
        edt_password = findViewById(R.id.edt_signup_password);
        edt_confirm_password = findViewById(R.id.edt_signup_confirm_password);
        btn_signup = findViewById(R.id.btn_signup);
        location_desk = findViewById(R.id.btn_location_desk_register);

        location_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,LocationDeskRegister.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input values from EditText fields
                String userName = edt_user_name.getText().toString().trim();
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString();
                String confirmPassword = edt_confirm_password.getText().toString();

                // Reset errors
                edt_user_name.setError(null);
                edt_email.setError(null);
                edt_password.setError(null);
                edt_confirm_password.setError(null);

                // Check if any field is empty
                if (TextUtils.isEmpty(userName)) {
                    edt_user_name.setError("Please fill in this field");
                    edt_user_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edt_email.setError("Please fill in this field");
                    edt_email.requestFocus();
                    return;
                }
                if(!email.contains("@") || !email.contains(".com")){
                    edt_email.setError("Please fill correct email address");
                    edt_email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edt_password.setError("Please fill in this field");
                    edt_password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    edt_confirm_password.setError("Please fill in this field");
                    edt_confirm_password.requestFocus();
                    return;
                }

                // Check if password and confirm password match
                if (!password.equals(confirmPassword)) {
                    edt_confirm_password.setError("Passwords do not match");
                    edt_confirm_password.requestFocus();
                    return;

                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success, update Firebase Database with user information
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userId = firebaseUser.getUid();

                                    // Create a reference to the Firebase Database
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                    // Create a User object with user information
                                    Model.User user = new User(userName, email, password); // Create a User class with appropriate fields

                                    // Set user information in the database
                                    databaseReference.setValue(user);

                                    // Notify user of successful account creation
                                    Toast.makeText(SignUp.this, "Account created", Toast.LENGTH_SHORT).show();

                                    // Move to login activity
                                    startActivity(new Intent(SignUp.this, Login.class));
                                } else {
                                    // If sign up fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });
    }
}