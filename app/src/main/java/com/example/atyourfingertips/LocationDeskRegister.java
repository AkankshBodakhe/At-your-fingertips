package com.example.atyourfingertips;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LocationDeskRegister extends AppCompatActivity {

    private Spinner spinnerLocation;
    EditText username, email, password, confirm_pass;
    Button registerLocationButton;
    DatabaseReference rootRef;
    Map<String, DatabaseReference> locationRefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_desk_register);

        // Firebase root reference
        rootRef = FirebaseDatabase.getInstance().getReference();

        // Initialize location references
        locationRefs = new HashMap<>();
        locationRefs.put("Cafes", rootRef.child("cafes"));
        locationRefs.put("Restaurants", rootRef.child("restaurants"));
        locationRefs.put("Hotels", rootRef.child("hotels"));
        locationRefs.put("Clubs", rootRef.child("clubs"));

        spinnerLocation = findViewById(R.id.spinner_location);
        username = findViewById(R.id.edt_location_user_name);
        email = findViewById(R.id.edt_location_email);
        password = findViewById(R.id.edt_location_password);
        confirm_pass = findViewById(R.id.edt_confirm_pass);
        registerLocationButton = findViewById(R.id.btn_register_location_desk);

        String[] locations = {"Cafes", "Restaurants", "Hotels", "Clubs"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        registerLocationButton.setOnClickListener(v -> {
            String userName = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPassword = confirm_pass.getText().toString().trim();
            String selectedLocation = spinnerLocation.getSelectedItem().toString();

            if (!isValidUsername(userName)) {
                Toast.makeText(LocationDeskRegister.this, "Invalid username", Toast.LENGTH_SHORT).show();
                return;
            }
            

            // Password validation
            if (!userPassword.equals(confirmPassword)) {
                Toast.makeText(LocationDeskRegister.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email validation
            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(LocationDeskRegister.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the reference based on selected location
            DatabaseReference locationRef = locationRefs.get(selectedLocation);

            // Storing data in Firebase under the location desk section
            DatabaseReference locationDeskRef = rootRef.child("location_desk").child(userName);
            locationDeskRef.child("Email").setValue(userEmail);
            locationDeskRef.child("Password").setValue(userPassword);

            // Storing data in Firebase under the specific location section
            DatabaseReference userRef = locationRef.child(userName);
            userRef.child("Email").setValue(userEmail);
            userRef.child("Password").setValue(userPassword);

            Toast.makeText(LocationDeskRegister.this, "Registration successful", Toast.LENGTH_SHORT).show();
        });


    }

    private boolean isValidUsername(String userName) {
        return !userName.contains(".") && !userName.contains("#") &&
                !userName.contains("$") && !userName.contains("[") &&
                !userName.contains("]") && !userName.contains("@");
    }
}
