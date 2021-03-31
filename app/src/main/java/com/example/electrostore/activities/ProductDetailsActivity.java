package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

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

        buyButton = findViewById(R.id.addToCartButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");

                if(product.getStockLevel() > 0) {

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

                                    Toast.makeText(ProductDetailsActivity.this, "Added to cart",
                                            Toast.LENGTH_SHORT).show();

                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProductDetailsActivity.this);
                    dlgAlert.setMessage("Item is out of stock.");
                    dlgAlert.setTitle("Error");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            (DialogInterface.OnClickListener) (dialog, which) -> {
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}