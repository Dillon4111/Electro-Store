package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.classes.User;
import com.example.electrostore.utils.MainProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ArrayList<Product> cart = new ArrayList<Product>();
    private MainProductsAdapter mAdapter;
    RecyclerView myRecyclerView;

    private TextView totalTextView;
    private Button payNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        myRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    if (userSnap.getKey().equals(mUser.getUid())) {
                        if (userSnap.child("cart").exists()) {

                            User user = userSnap.getValue(User.class);

                            cart = user.getCart();
                            Log.d("IF", "HELLO");
                        }

                        double totalPrice = 0;
                        for (Product p : cart) {
                            totalPrice += p.getPrice();
                        }

                        totalTextView = findViewById(R.id.shoppingCartTotalText);

                        if(!cart.isEmpty()) {
                            totalTextView.setText("Total Price: €" + totalPrice);
                        }
                        else {
                            totalTextView.setText("Cart is empty");
                        }

                        myRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        myRecyclerView.setHasFixedSize(true);
                        mAdapter = new MainProductsAdapter(cart, CartActivity.this);
                        myRecyclerView.setAdapter(mAdapter);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        payNowButton = findViewById(R.id.payNowButton);
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cart.isEmpty()) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CartActivity.this);

                    List<String> productIDs = new ArrayList<>();
                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

                    for (Product p : cart) {
                        productIDs.add(p.getId());
                    }

                    for (String productID : productIDs) {
                        int occurrences = Collections.frequency(productIDs, productID);
                        hashMap.put(productID, occurrences);
                    }

                    Iterator it = hashMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Log.d("HashMap", pair.getKey() + " = " + pair.getValue());

                        reduceProductStock(pair.getKey().toString(), (Integer) pair.getValue());
                    }

                    DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
                    userDB.child(mUser.getUid()).child("cart").setValue(new ArrayList<Product>());

                    cart.clear();
                    mAdapter.notifyDataSetChanged();

                    totalTextView.setText("Cart is empty");

                    dlgAlert.setMessage("Thank you for your purchase. " +
                            "Your order will be dispatched within 2 working days.");
                    dlgAlert.setTitle("Transaction Approved");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            (dialog, which) -> {
                            });
                }
                else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CartActivity.this);
                    dlgAlert.setMessage("Shopping cart is empty.");
                    dlgAlert.setTitle("Error");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            (dialog, which) -> {
                            });
                }
            }
        });
    }

    public void reduceProductStock(String id, int count) {
        Log.d("Method", id + count);

        final DatabaseReference productDB = FirebaseDatabase.getInstance().getReference("Products");

        productDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    if (productSnap.getKey().equals(id)) {

                        long stockLevel = (long) productSnap.child("stockLevel").getValue();

                        stockLevel = stockLevel - count;

                        productDB.child(productSnap.getKey()).child("stockLevel").setValue(stockLevel);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}