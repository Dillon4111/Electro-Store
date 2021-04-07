package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.classes.User;
import com.example.electrostore.utils.CustomerDetailsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity {

    private ArrayList<User> users = new ArrayList<>();
    private CustomerDetailsAdapter mAdapter;
    RecyclerView detailsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        detailsRecyclerView = (RecyclerView) findViewById(R.id.customerDetailsRecyclerView);
        detailsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        detailsRecyclerView.setLayoutManager(myLayoutManager);

        final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                        User user = userSnap.getValue(User.class);
                        users.add(user);
                        user.setUid(userSnap.getKey());
                }
                detailsRecyclerView.setLayoutManager(new LinearLayoutManager(CustomerDetailsActivity.this));
                detailsRecyclerView.setHasFixedSize(true);
                mAdapter = new CustomerDetailsAdapter(users, CustomerDetailsActivity.this);
                detailsRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}