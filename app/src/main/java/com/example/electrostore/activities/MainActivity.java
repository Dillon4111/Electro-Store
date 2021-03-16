package com.example.electrostore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.electrostore.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//            case R.id.nav_my_watchlist:
//                i = new Intent(MainActivity.this, MyWatchlistActivity.class);
//                startActivity(i);
//                break;
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

}