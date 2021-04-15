package com.example.electrostore.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.electrostore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signInEmail, signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            return;
        }

        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        Button signInButton = findViewById(R.id.signInButton);
        TextView orSignInText = findViewById(R.id.orRegisterText);

        signInButton.setOnClickListener(v -> {
            String email = signInEmail.getText().toString().trim();
            String password = signInPassword.getText().toString();

            if (email.matches("") || password.matches("")) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SignInActivity.this);

                dlgAlert.setMessage("Please fill in both fields");
                dlgAlert.setTitle("Hold up!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                dlgAlert.setPositiveButton("Ok",
                        (dialog, which) -> {

                        });
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "User signed in",
                                        Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(intent);

                            } else {
                                Log.w("MySignin", "SignInUserWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        orSignInText.setOnClickListener(v -> {
            Log.d("OR REGISTER", "Link clicked");
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}