package com.example.atyourfingertips;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Listofstationary extends AppCompatActivity {private RecyclerView recyclerView;
    private CafeAdapter cafeAdapter;
    private List<String> hotelNames;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cafe);

        recyclerView = findViewById(R.id._cafe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hotelNames = new ArrayList<>();
        cafeAdapter = new CafeAdapter(this, hotelNames);
        recyclerView.setAdapter(cafeAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("stationary");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String cafeName = snapshot.getValue(String.class);
                    hotelNames.add(cafeName);
                }
                cafeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}