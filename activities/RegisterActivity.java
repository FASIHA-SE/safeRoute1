package com.example.saferoute.activities;

import android.content.Intent;
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
import com.example.saferoute.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password,userName;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
        userName = (EditText) findViewById(R.id.userName);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        TextView loginLink = (TextView) findViewById(R.id.loginLink);
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://saferoute-a7294-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");

        //reg btn
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //login link
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


        private void registerUser(){

            String username = userName.getText().toString().trim();
            String userEmail=email.getText().toString().trim();

            String userPassword=password.getText().toString().trim();

            if(username.isEmpty()){

                userName.setError("Enter user-name");

                userName.requestFocus(); //cursor back to username field

                return;
            }

            if(userEmail.isEmpty()){

                email.setError("Enter Email");

                email.requestFocus();

                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){

                email.setError("Enter Valid Email");

                email.requestFocus();

                return;
            }

            if(userPassword.isEmpty()){

                password.setError("Enter Password");

                password.requestFocus();

                return;
            }

            if(userPassword.length()<6){

                password.setError("Password must be 6 characters");

                password.requestFocus();

                return;
            }

            auth.createUserWithEmailAndPassword(userEmail,userPassword)

                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                String userId = auth.getCurrentUser().getUid();

                                User user = new User(username, userEmail, userPassword);

                                reference.child(userId).setValue(user);

                                Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }

                            else{

                                Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });


    }
}