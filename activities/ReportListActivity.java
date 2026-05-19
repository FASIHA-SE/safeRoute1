package com.example.saferoute.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saferoute.R;
import com.example.saferoute.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ReportListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> reportList;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView =findViewById(R.id.reportListView);

        reportList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://saferoute-a7294-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Reports");

        loadReports();
    }
    private void loadReports() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                reportList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {

                    Report report = data.getValue(Report.class);

                    String text = "📍 " + report.getLocation() + "\n📝 " + report.getDescription();

                    reportList.add(text);
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(ReportListActivity.this, android.R.layout.simple_list_item_1, reportList);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}