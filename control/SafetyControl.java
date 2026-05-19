package com.example.saferoute.control;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.saferoute.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class SafetyControl {

    private Context context;
    private RequestQueue queue;
    private DatabaseReference reportRef;

    public SafetyControl(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);

        this.reportRef = FirebaseDatabase
                .getInstance("https://saferoute-a7294-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Reports");
    }

    // 🔵 COMMUNITY REPORTS (FIREBASE)
    public void loadCommunityReports(String location, TextView communityReports) {

        reportRef.get().addOnSuccessListener(snapshot -> {

            StringBuilder builder = new StringBuilder();

            for (DataSnapshot data : snapshot.getChildren()) {

                Report report = data.getValue(Report.class);

                if (report != null &&
                        report.getLocation().equalsIgnoreCase(location.replace("+", " "))) {

                    builder.append("⚠️ ")
                            .append(report.getDescription())
                            .append("\n\n");
                }
            }

            if (builder.length() == 0) {
                communityReports.setText("No community reports found.");
            } else {
                communityReports.setText(builder.toString());
            }

        }).addOnFailureListener(e ->
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    // 🔵 POLICE API
    public void getPoliceStations(String location,
                                  TextView policeCount,
                                  Runnable onComplete) {

        String url = "https://nominatim.openstreetmap.org/search?q=police+station+"
                + location + "&format=jsonv2&limit=10";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    int count = response.length();
                    policeCount.setText("Police Stations: " + count);

                    onComplete.run(); // next step call
                },
                error -> Toast.makeText(context, "Police API Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("User-Agent", "SafeRoute");
                return headers;
            }
        };

        queue.add(request);
    }

    // 🔵 HOSPITAL API
    public void getHospitals(String location,
                             TextView hospitalCount,
                             Runnable onComplete) {

        String url = "https://nominatim.openstreetmap.org/search?q=hospital+"
                + location + "&format=jsonv2&limit=10";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    int count = response.length();
                    hospitalCount.setText("Hospitals: " + count);

                    onComplete.run();
                },
                error -> Toast.makeText(context, "Hospital API Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("User-Agent", "SafeRoute");
                return headers;
            }
        };

        queue.add(request);
    }

    // 🔵 SAFETY SCORE LOGIC
    public int calculateScore(int police, int hospital) {

        int policeScore = Math.min(police, 5) * 20;
        int hospitalScore = Math.min(hospital, 5) * 15;

        return policeScore + hospitalScore;
    }
}