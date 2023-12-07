package com.example.bottomnavigationactivities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    /*Receive Data */
    ImageView receiveImage;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_seedtest) {
                startActivity(new Intent(getApplicationContext(), SeedTestActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String name = preferences.getString("name", "");
                String email = preferences.getString("email", "");
                String username = preferences.getString("username", "");
                String password = preferences.getString("password", "");

                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                profileIntent.putExtra("name", name);
                profileIntent.putExtra("email", email);
                profileIntent.putExtra("username", username);
                profileIntent.putExtra("password", password);
                startActivity(profileIntent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

    }

    private void saveUserDetailsToSharedPreferences(String name, String email, String username, String password) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Update SharedPreferences only if there's a difference
        if (!name.equals(preferences.getString("name", "")) ||
                !email.equals(preferences.getString("email", "")) ||
                !username.equals(preferences.getString("username", "")) ||
                !password.equals(preferences.getString("password", ""))) {

            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
        }
    }
}
