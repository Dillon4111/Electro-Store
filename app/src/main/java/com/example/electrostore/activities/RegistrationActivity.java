package com.example.electrostore.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electrostore.R;
import com.example.electrostore.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference db;
    private EditText userNameEdit, emailEdit, passwordEdit, confPasswordEdit, addressEdit;
    private RadioButton radioButton;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        userNameEdit = findViewById(R.id.registerName);
        emailEdit = findViewById(R.id.registerEmail);
        passwordEdit = findViewById(R.id.registerPassword);
        confPasswordEdit = findViewById(R.id.registerConfirmPassword);
        Button registerButton = findViewById(R.id.registerButton);
        addressEdit = findViewById(R.id.addressEditText);
        radioButton = findViewById(R.id.studentRadioButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = userNameEdit.getText().toString();
                final String email = emailEdit.getText().toString().trim();
                final String password = passwordEdit.getText().toString();
                String confPassword = confPasswordEdit.getText().toString();
                final String address = addressEdit.getText().toString();
                boolean student = radioButton.isChecked();

                if (userName.matches("") || email.matches("") ||
                        password.matches("") || confPassword.matches("") ||
                        address.matches("")) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RegistrationActivity.this);

                    dlgAlert.setMessage("Please fill in all fields");
                    dlgAlert.setTitle("Hold up!");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else if (password.length() < 6) {
                    passwordEdit.setError("Password must be 6 characters or more");
                    passwordEdit.requestFocus();
                } else if (!password.equals(confPassword)) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RegistrationActivity.this);

                    dlgAlert.setMessage("Passwords are not the same");
                    dlgAlert.setTitle("Hold up!");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Register", "createUserWithEmail:success");
                                        mUser = mAuth.getCurrentUser();
                                        User user = new User(userName, email, address, student);
                                        String uid = mUser.getUid();
                                        db.child("Users").child(uid).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RegistrationActivity.this, "Registration is successful",
                                                                Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(RegistrationActivity.this, SignInActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegistrationActivity.this, "Write to db failed", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        Log.w("Register", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        TextView signInButton = findViewById(R.id.backToSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Back to sign-in", "Link clicked");
                Intent intent = new Intent(RegistrationActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}