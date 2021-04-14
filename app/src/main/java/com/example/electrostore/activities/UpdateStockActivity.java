package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.utils.MainProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateStockActivity extends AppCompatActivity {

    private DatabaseReference productsDB;

    private ArrayList<Product> myDataset = new ArrayList<Product>();
    private MainProductsAdapter mAdapter;
    RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        myRecyclerView = (RecyclerView) findViewById(R.id.updateStockRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        productsDB = FirebaseDatabase.getInstance().getReference("Products");
        productsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
//                    String name = productSnapshot.child("name").getValue().toString();
//                    String price = productSnapshot.child("price").getValue().toString();
//                    String description = productSnapshot.child("description").getValue().toString();
//                    String manufact = productSnapshot.child("manufacturer").getValue().toString();
//                    List<String> images = (List<String>) productSnapshot.child("images").getValue();
//                    String productID = productSnapshot.getKey();
//                    Product product = new Product();
//                    product.setId(productID);
//                    product.setName(name);
//                    product.setPrice(Double.parseDouble(price));
//                    product.setImages(images);
//                    product.setDescription(description);
//                    product.setManufacturer(manufact);
//                    product.setCategory(productSnapshot.child("category").getValue().toString());
//                    long stock = (long) productSnapshot.child("stockLevel").getValue();
//                    product.setStockLevel((int) stock);
                    Product product = productSnapshot.getValue(Product.class);
                    product.setId(productSnapshot.getKey());
                    myDataset.add(product);
                }
                myRecyclerView.setLayoutManager(new LinearLayoutManager((UpdateStockActivity.this)));
                myRecyclerView.setHasFixedSize(true);
                mAdapter = new MainProductsAdapter(myDataset, UpdateStockActivity.this);
                myRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}