package com.example.saferoute.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saferoute.R;
import com.example.saferoute.model.Report;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;

public class ReportUnsafeActivity extends AppCompatActivity {

    EditText locationInput;
    EditText descriptionInput;
    Button submitBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_unsafe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FIND VIEWS
        locationInput = (EditText) findViewById(R.id.location);
        descriptionInput = (EditText) findViewById(R.id.description);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        // FIREBASE REFERENCE
        reference = FirebaseDatabase.getInstance("https://saferoute-a7294-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Reports");

        // BUTTON CLICK
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReport();
            }
        });
    }

        private void submitReport() {

            String location = locationInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            // VALIDATION
            if (location.isEmpty()) {
                locationInput.setError("Please enter location");
                locationInput.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                descriptionInput.setError("Please enter description");
                descriptionInput.requestFocus();
                return;
            }

            // CREATE FIREBASE KEY
            String id = reference.push().getKey();

            // TIME
            long timestamp = System.currentTimeMillis();

            // CREATE OBJECT
            Report report = new Report(location, description, timestamp);

            // SAVE TO FIREBASE
            reference.child(id).setValue(report)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(ReportUnsafeActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();

                            locationInput.setText("");
                            descriptionInput.setText("");
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ReportUnsafeActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

    }
}