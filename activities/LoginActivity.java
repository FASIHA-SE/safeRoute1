package com.example.saferoute.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saferoute.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.saferoute.R;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        TextView registerLink = (TextView) findViewById(R.id.registerLink);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        // Check User is already logged in

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {

            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); //Close the current activity .. in my case LA
        }
        // LOGIN BUTTON

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });
        // REGISTER LINK

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

        private void loginUser() {

            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty()) {
                email.setError("Enter Email");
                return;
            }

            if (userPassword.isEmpty()) {
                password.setError("Enter Password");
                return;
            }

            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, DashboardActivity.class));
                            finish();

                        } else {
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

    }
}