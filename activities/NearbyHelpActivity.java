package com.example.saferoute.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.saferoute.R;
import com.example.saferoute.adapter.PlacesAdapter;
import com.example.saferoute.model.SafetyPlace;

import org.json.JSONObject;

import java.util.ArrayList;

public class NearbyHelpActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<SafetyPlace> places;

    PlacesAdapter adapter;

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_help);

        listView = findViewById(R.id.listView);

        places = new ArrayList<>();

        adapter = new PlacesAdapter(this, places); //convert list into UI

        listView.setAdapter(adapter);

        location = getIntent().getStringExtra("location");

        if(location != null){

            location = location.replace(" ","+");

            getPoliceStations(location);

            getHospitals(location);
        }
    }

    // POLICE API

    private void getPoliceStations(String location){

        String url =
                "https://nominatim.openstreetmap.org/search?q=police+station+"
                        + location
                        + "&format=jsonv2&limit=10";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try{

                        for(int i=0;i<response.length();i++){

                            JSONObject object = response.getJSONObject(i);

                            String name = object.getString("display_name");

                            String lat = object.getString("lat");

                            String lon = object.getString("lon");

                            places.add(
                                    new SafetyPlace(name, "Police Station", lat, lon)
                            );
                        }

                        adapter.notifyDataSetChanged();

                    }
                    catch (Exception e){

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },

                error -> Toast.makeText(this, "Police API Error", Toast.LENGTH_SHORT).show()){

            @Override
            public java.util.Map<String,String> getHeaders(){

                java.util.Map<String,String> headers =
                        new java.util.HashMap<>();

                headers.put("User-Agent","SafeRoute");

                return headers;
            }
        };

        queue.add(request);
    }

    // HOSPITAL API

    private void getHospitals(String location){

        String url =
                "https://nominatim.openstreetmap.org/search?q=hospital+"
                        + location
                        + "&format=jsonv2&limit=10";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try{

                        for(int i=0;i<response.length();i++){

                            JSONObject object = response.getJSONObject(i);

                            String name = object.getString("display_name");

                            String lat = object.getString("lat");//latitute

                            String lon = object.getString("lon");//longititude

                            places.add(
                                    new SafetyPlace(name, "Hospital", lat, lon)
                            );
                        }

                        adapter.notifyDataSetChanged();

                    }catch (Exception e){

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },

                error -> Toast.makeText(this, "Hospital API Error", Toast.LENGTH_SHORT).show()
        ){

            @Override
            public java.util.Map<String,String> getHeaders(){

                java.util.Map<String,String> headers =
                        new java.util.HashMap<>();

                headers.put("User-Agent","SafeRoute");

                return headers;
            }
        };

        queue.add(request);
    }
}