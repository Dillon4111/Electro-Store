package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference db;
    private EditText editName, editPrice, editDescription, editCategory, editManufact, editStockLvl;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("PRODUCT_INTENT");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editName = findViewById(R.id.updateProductName);
        editPrice = findViewById(R.id.updateProductPrice);
        editDescription = findViewById(R.id.updateDescription);
        editCategory = findViewById(R.id.updateCategoryText);
        editManufact = findViewById(R.id.updateManufactText);
        editStockLvl = findViewById(R.id.updateStockLevelText);

        editName.setText(product.getName());
        editPrice.setText(String.valueOf(product.getPrice()));
        editDescription.setText(product.getDescription());
        editCategory.setText(product.getCategory());
        editManufact.setText(product.getManufacturer());
        editStockLvl.setText(String.valueOf(product.getStockLevel()));

        Button addProductButton = findViewById(R.id.updateProductButton);

        addProductButton.setOnClickListener(v -> {
            db = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            String uid = mUser.getUid();
            String productName = editName.getText().toString();
            String productPrice = editPrice.getText().toString();
            double priceDouble = Double.parseDouble(productPrice);
            String productDescription = editDescription.getText().toString();
            String productManufact = editManufact.getText().toString();
            String productCategory = editCategory.getText().toString();
            int productStockLvl = Integer.parseInt(editStockLvl.getText().toString());

            if (productName.matches("") || productPrice.matches("") ||
                    productDescription.matches("") || productManufact.matches("") ||
                    editStockLvl.getText().toString().matches("")) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(EditProductActivity.this);
                dlgAlert.setMessage("Please fill in required fields(*)");
                dlgAlert.setTitle("Hold Up!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            } else {
                String id = product.getId();
                double rating = product.getOverallRating();
                int totalRatings = product.getTotalRatings();
                List<String> images = product.getImages();
                product = new Product(productName, productCategory, productDescription, productManufact,
                        priceDouble, productStockLvl, images);
                product.setTotalRatings(totalRatings);
                product.setOverallRating(rating);
                db.child("Products").child(id).setValue(product)
                        .addOnSuccessListener(aVoid -> Toast.makeText(EditProductActivity.this, "Product updated",
                                Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> Toast.makeText(EditProductActivity.this, "Write to db failed", Toast.LENGTH_LONG).show());
            }
        });

        ImageButton backButton = findViewById(R.id.updateProductBackButton);
        backButton.setOnClickListener(v -> {
            Intent i1 = new Intent(EditProductActivity.this, UpdateStockActivity.class);
            finish();
            startActivity(i1);
        });
    }
}