package com.example.atyourfingertips;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Location_login extends AppCompatActivity {
    EditText llusername,llpassword,llemail;
    Button locationLogin;
    DatabaseReference locationDeskRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_login);
        llusername= findViewById(R.id.edt_location_login_user_name);
        llemail= findViewById(R.id.edt_location_login_email);
        llpassword= findViewById(R.id.edt_location_login_password);
        locationLogin = findViewById(R.id.btn_location_login_signin);

        locationDeskRef = FirebaseDatabase.getInstance().getReference().child("location_desk");


        locationLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username, email, and password
                String username = llusername.getText().toString().trim();
                String email = llemail.getText().toString().trim();
                String password = llpassword.getText().toString().trim();

                // Check if any field is empty
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Location_login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Retrieve user details from Firebase
                locationDeskRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User exists, check email and password
                            String storedEmail = dataSnapshot.child("Email").getValue(String.class);
                            String storedPassword = dataSnapshot.child("Password").getValue(String.class);

                            // Check if entered email and password match the stored values
                            if (email.equals(storedEmail) && password.equals(storedPassword)) {
                                // Login successful
                                Toast.makeText(Location_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                // Add your further actions here, such as navigating to another activity
                            } else {
                                // Invalid credentials
                                Toast.makeText(Location_login.this, "Invalid username, email, or password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User does not exist
                            Toast.makeText(Location_login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                        Toast.makeText(Location_login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}