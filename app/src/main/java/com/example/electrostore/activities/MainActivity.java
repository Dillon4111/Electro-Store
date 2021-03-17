package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.electrostore.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private EditText searchTitleEdit, searchCatEdit, searchManuEdit;
    private Spinner sortSpinner;
    private List<String> sortSpinnerList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        checkIfAdmin();

        searchTitleEdit = findViewById(R.id.nameSearch);
        searchCatEdit = findViewById(R.id.categorySearch);
        searchManuEdit = findViewById(R.id.manufactSearch);
        sortSpinner = findViewById(R.id.sortSpinner);

        //sortSpinnerList.add("Sort");
        sortSpinnerList.add("Ascending");
        sortSpinnerList.add("Descending");

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
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.nav_add_product:
                if(isAdmin) {
                    i = new Intent(MainActivity.this, AddProductActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this, "Admin needed for access",
                            Toast.LENGTH_LONG).show();
                }
                break;
//            case R.id.nav_search_movie:
//                i = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(i);
//                break;
//            case R.id.map:
//                i = new Intent(MainActivity.this, MapsActivity.class);
//                startActivity(i);
//                break;
//            case R.id.nav_settings:
//                i = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(i);
//                break;
//            case R.id.nav_log_out:
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "User signed out", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//                finish();
//                startActivity(intent);
//                break;
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
                    if(mUser.getUid().equals(snapshot.getKey())) {
                        String admin = snapshot.child("admin").getValue().toString();

                        if(admin.equals("true")) {
                            isAdmin = true;
                        }
                        else
                            isAdmin = false;

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