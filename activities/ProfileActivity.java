package com.example.saferoute.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saferoute.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    TextView usernameText, emailText;
    Switch themeSwitch;
    Button logoutBtn;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // UI
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        themeSwitch = findViewById(R.id.themeSwitch);
        logoutBtn = findViewById(R.id.logoutBtn);



        // Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance("https://saferoute-a7294-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        // SharedPref
        sp = getSharedPreferences("LoginData", MODE_PRIVATE);

        // LOAD DATA
        if (user != null) {
            ref.child(user.getUid()).get().addOnSuccessListener(snapshot -> {
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                usernameText.setText(username);
                emailText.setText(email);
            });
        }

        // LOGOUT
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(ProfileActivity.this).setTitle("Logout").setMessage("Do you want to logout?").setPositiveButton("Yes", (dialog, which) ->
                        {

                            auth.signOut();
                            sp.edit().putBoolean("isLoggedIn", false).apply();

                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }).setNegativeButton("No", null).show();
            }
        });





        // DARK MODE (simple save)
        themeSwitch.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {

                sp.edit().putBoolean("darkMode", isChecked).apply();

                if (isChecked) {
                    Toast.makeText(ProfileActivity.this, "Dark Mode ON", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ProfileActivity.this, "Light Mode ON", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}