package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrostore.R;
import com.example.electrostore.classes.Product;
import com.example.electrostore.utils.MainProductsAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private List<String> sortSpinnerList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private boolean isAdmin;
    private ArrayList<Product> myDataset = new ArrayList<Product>();
    private MainProductsAdapter mAdapter;
    RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        checkIfAdmin();

        myRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        sortSpinnerList = new ArrayList<>();
        sortSpinnerList.add("Sort By...");
        sortSpinnerList.add("Title Ascending");
        sortSpinnerList.add("Title Descending");
        sortSpinnerList.add("Price Ascending");
        sortSpinnerList.add("Price Descending");
        sortSpinnerList.add("Manufacturer Ascending");
        sortSpinnerList.add("Manufacturer Descending");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, sortSpinnerList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        Spinner sortSpinner = findViewById(R.id.sortSpinner);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(categoryAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (position == 1) {
                        Collections.sort(myDataset, Comparator.comparing(Product::getName));
                    }
                    else if (position == 2) {
                        Collections.sort(myDataset, Comparator.comparing(Product::getName).reversed());
                    }
                    else if (position == 3) {
                        Log.d("Case", String.valueOf(position));
                        myDataset.sort(Comparator.comparing(Product::getPrice));
                    }
                    else if (position == 4) {
                        Log.d("Case", String.valueOf(position));
                        myDataset.sort(Comparator.comparing(Product::getPrice).reversed());
                    }
                    else if (position == 5) {
                        Collections.sort(myDataset, Comparator.comparing(Product::getManufacturer));
                    }
                    else if (position == 6) {
                        Collections.sort(myDataset, Comparator.comparing(Product::getManufacturer).reversed());
                    }

                    mAdapter.filteredList(myDataset);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText searchTitleEdit = findViewById(R.id.nameSearch);
        EditText searchCatEdit = findViewById(R.id.categorySearch);
        EditText searchManuEdit = findViewById(R.id.manufactSearch);
        sortSpinner = findViewById(R.id.sortSpinner);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        DatabaseReference productsDB = FirebaseDatabase.getInstance().getReference("Products");
        productsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue().toString();
                    String price = productSnapshot.child("price").getValue().toString();
                    String description = productSnapshot.child("description").getValue().toString();
                    String manufact = productSnapshot.child("manufacturer").getValue().toString();
                    List<String> images = (List<String>) productSnapshot.child("images").getValue();
                    String productID = productSnapshot.getKey();
                    Product product = new Product();
                    product.setId(productID);
                    product.setName(name);
                    product.setPrice(Double.parseDouble(price));
                    product.setImages(images);
                    product.setDescription(description);
                    product.setManufacturer(manufact);
                    product.setCategory(productSnapshot.child("category").getValue().toString());
                    long stock = (long) productSnapshot.child("stockLevel").getValue();
                    product.setStockLevel((int) stock);
                    myDataset.add(product);
                }
                myRecyclerView.setLayoutManager(new LinearLayoutManager((MainActivity.this)));
                myRecyclerView.setHasFixedSize(true);
                mAdapter = new MainProductsAdapter(myDataset, MainActivity.this);
                myRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchTitleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), 1);
            }
        });

        searchCatEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), 2);
            }
        });

        searchManuEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), 3);
            }
        });
    }

    public void filter(String text, int i) {
        ArrayList<Product> products = new ArrayList<>();

        switch (i) {
            case (1):
                for (Product product : myDataset) {
                    if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                        products.add(product);
                    }
                }
            case (2):
                for (Product product : myDataset) {
                    if (product.getCategory().toLowerCase().contains(text.toLowerCase())) {
                        products.add(product);
                    }
                }
            case (3):
                for (Product product : myDataset) {
                    if (product.getManufacturer().toLowerCase().contains(text.toLowerCase())) {
                        products.add(product);
                    }
                }
        }
        mAdapter.filteredList(products);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.nav_my_cart:
                i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
                break;
            case R.id.nav_my_orders:
                i = new Intent(MainActivity.this, MyOrdersHistory.class);
                startActivity(i);
                break;
            case R.id.nav_add_product:
                if (isAdmin) {
                    i = new Intent(MainActivity.this, AddProductActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Admin needed for access",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_update_stock:
                if (isAdmin) {
                    i = new Intent(MainActivity.this, UpdateStockActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Admin needed for access",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_customer_details:
                if (isAdmin) {
                    i = new Intent(MainActivity.this, CustomerDetailsActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Admin needed for access",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_log_out:
                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "User signed out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                finish();
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void checkIfAdmin() {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (mUser.getUid().equals(snapshot.getKey())) {
                        String admin = snapshot.child("admin").getValue().toString();

                        isAdmin = admin.equals("true");

                        Log.d("Admin = ", String.valueOf(isAdmin));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}