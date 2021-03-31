package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.utils.GlideApp;
import com.example.electrostore.utils.MainProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName, productPrice, productCategory, productManufacturer, productDescription;
    private StorageReference storageReference;
    Product product;
    private Button buyButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView imageView;
    List<Product> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("PRODUCT_INTENT");

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        productDescription = findViewById(R.id.productDescription);
        productManufacturer = findViewById(R.id.productManufacturer);

        productName.setText(product.getName());
        productPrice.setText(String.valueOf(product.getPrice()));
        productCategory.setText(product.getCategory());
        productManufacturer.setText(product.getManufacturer());
        productDescription.setText(product.getDescription());


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageView = findViewById(R.id.productImageViewDetails);

        for (String url : product.getImages()) {

            GlideApp.with(ProductDetailsActivity.this)
                    .load(storageReference.child(url))
                    .into(imageView);

            break;
        }

        buyButton = findViewById(R.id.buyNowButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");

                userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            if (userSnap.getKey().equals(mUser.getUid())) {
                                if (userSnap.child("cart").exists()) {
                                    cart = (ArrayList<Product>) userSnap.child("cart").getValue();
                                    Log.d("IF", "HELLO");
                                }

                                cart.add(product);
                                userDB.child(userSnap.getKey()).child("cart").setValue(cart);
                                cart.clear();

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}