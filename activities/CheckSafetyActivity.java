package com.example.saferoute.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saferoute.R;
import com.example.saferoute.control.SafetyControl;

public class CheckSafetyActivity extends AppCompatActivity {

    EditText locationInput;
    Button searchBtn, viewNearbyHelpBtn;

    TextView policeCount, hospitalCount, safetyScore,
            resultText, recommendationText, communityReports;

    String currentLocation = "";

    int policeStations = 0;
    int hospitals = 0;

    SafetyControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_safety);

        // UI
        locationInput = findViewById(R.id.locationInput);
        searchBtn = findViewById(R.id.searchBtn);
        viewNearbyHelpBtn = findViewById(R.id.viewNearbyHelpBtn);

        policeCount = findViewById(R.id.policeCount);
        hospitalCount = findViewById(R.id.hospitalCount);
        safetyScore = findViewById(R.id.safetyScore);
        resultText = findViewById(R.id.resultText);
        recommendationText = findViewById(R.id.recommendationText);
        communityReports = findViewById(R.id.communityReports);

        // CONTROL
        control = new SafetyControl(this);

        // SEARCH BUTTON
        searchBtn.setOnClickListener(v -> analyzeSafety());

        // NAVIGATION
        viewNearbyHelpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, NearbyHelpActivity.class);
            intent.putExtra("location", currentLocation);
            startActivity(intent);
        });
    }

    private void analyzeSafety() {

        String location = locationInput.getText().toString().trim();
        currentLocation = location;

        if (location.isEmpty()) {
            locationInput.setError("Enter Location");
            return;
        }

        String formattedLocation = location.replace(" ", "+");

        resultText.setText("Analyzing...");
        safetyScore.setText("Score: 0");

        control.getPoliceStations(formattedLocation, policeCount, () -> {

            control.getHospitals(formattedLocation, hospitalCount, () -> {

                policeStations = Integer.parseInt(
                        policeCount.getText().toString().replaceAll("\\D+","")
                );

                hospitals = Integer.parseInt(
                        hospitalCount.getText().toString().replaceAll("\\D+","")
                );

                int score = control.calculateScore(policeStations, hospitals);

                safetyScore.setText("Score: " + score);

                showResult(score);

                control.loadCommunityReports(formattedLocation, communityReports);
            });
        });
    }

    private void showResult(int score) {

        if (score >= 80) {

            resultText.setText("HIGH COVERAGE");
            resultText.setTextColor(Color.GREEN);
            recommendationText.setText("Emergency help easily available.");

        } else if (score >= 40) {

            resultText.setText("MODERATE COVERAGE");
            resultText.setTextColor(Color.parseColor("#FF9800"));
            recommendationText.setText("Some delay possible.");

        } else {

            resultText.setText("LOW COVERAGE");
            resultText.setTextColor(Color.RED);
            recommendationText.setText("Limited services nearby.");
        }
    }
}