package com.example.bottomnavigationactivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView profileName, profileEmail, profileUsername, profilePassword;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        editProfile = findViewById(R.id.editButton);

        showUserData();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_seedtest) {
                startActivity(new Intent(getApplicationContext(), SeedTestActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                return true;
            }
            return false;
        });
    }

    private boolean isUserDataAvailableInSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.contains("name") && preferences.contains("email") &&
                preferences.contains("username") && preferences.contains("password");
    }

    public void showUserData() {
        Intent intent = getIntent();
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        String nameUser = intent.getStringExtra("name");
        String emailUser = intent.getStringExtra("email");
        String usernameUser = intent.getStringExtra("username");
        String passwordUser = intent.getStringExtra("password");

        // Check if intent extras are available and not empty
        if (nameUser != null && !nameUser.isEmpty() &&
                emailUser != null && !emailUser.isEmpty() &&
                usernameUser != null && !usernameUser.isEmpty() &&
                passwordUser != null && !passwordUser.isEmpty()) {

            // Display user information from intent extras
            profileName.setText(nameUser);
            profileEmail.setText(emailUser);
            profileUsername.setText(usernameUser);
            profilePassword.setText(passwordUser);
        } else {
            // Display user information from SharedPreferences as a fallback
            String nameFromPrefs = preferences.getString("name", "");
            String emailFromPrefs = preferences.getString("email", "");
            String usernameFromPrefs = preferences.getString("username", "");
            String passwordFromPrefs = preferences.getString("password", "");

            profileName.setText(nameFromPrefs);
            profileEmail.setText(emailFromPrefs);
            profileUsername.setText(usernameFromPrefs);
            profilePassword.setText(passwordFromPrefs);
        }
    }


    private static final int PROFILE_EDIT_REQUEST_CODE = 123;

    public void passUserData() {
        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String namefromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailfromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernamefromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordfromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                    intent.putExtra("name", namefromDB);
                    intent.putExtra("email", emailfromDB);
                    intent.putExtra("username", usernamefromDB);
                    intent.putExtra("password", passwordfromDB);

                    // Start ProfileEditActivity with startActivityForResult
                    startActivityForResult(intent, PROFILE_EDIT_REQUEST_CODE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_EDIT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve updated data from ProfileEditActivity
            String updatedName = data.getStringExtra("name");
            String updatedEmail = data.getStringExtra("email");
            String updatedUsername = data.getStringExtra("username");
            String updatedPassword = data.getStringExtra("password");

            // Update the UI with the updated data
            profileName.setText(updatedName);
            profileEmail.setText(updatedEmail);
            profileUsername.setText(updatedUsername);
            profilePassword.setText(updatedPassword);

            // Save the updated data to SharedPreferences
            saveUserDetailsToSharedPreferences(updatedName, updatedEmail, updatedUsername, updatedPassword);
        }
    }

// Add the saveUserDetailsToSharedPreferences method

    private void saveUserDetailsToSharedPreferences(String name, String email, String username, String password) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}