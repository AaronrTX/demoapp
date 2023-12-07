package com.example.bottomnavigationactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupEmail, signupUsername, signupPassword;

    TextView loginRedirectText;
    Button signupButton;

    FirebaseDatabase database;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                final String name = signupName.getText().toString();
                final String email = signupEmail.getText().toString();
                final String username = signupUsername.getText().toString();
                final String password = signupPassword.getText().toString();

                if(name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Error: Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(SignupActivity.this,"Error: Student ID already exists",Toast.LENGTH_LONG).show();
                        }
                        else{
                            HelperClass helperClass = new HelperClass(name, email, username,password);
                            reference.child(username).setValue(helperClass);

                            Toast.makeText(SignupActivity.this,"You have signup successfully!",Toast.LENGTH_LONG).show();
                            HelperClass.saveUserDetailsToSharedPreferences(SignupActivity.this, name, email, username, password);
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}