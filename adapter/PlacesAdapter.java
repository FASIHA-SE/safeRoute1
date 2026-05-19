package com.example.saferoute.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saferoute.R;
import com.example.saferoute.model.SafetyPlace;

import java.util.ArrayList;

public class PlacesAdapter extends ArrayAdapter<SafetyPlace> {

    Context context; //current screen info
    ArrayList<SafetyPlace> places;

    public PlacesAdapter(Context context, ArrayList<SafetyPlace> places) {
        super(context, 0, places);

        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.place_item,parent,false);
        }

        TextView placeName = convertView.findViewById(R.id.placeName);

        TextView placeType = convertView.findViewById(R.id.placeType);

        SafetyPlace place = places.get(position);

        placeName.setText(place.getName());

        placeType.setText(place.getType());

        convertView.setOnClickListener(v -> {

            String uri = "google.navigation:q=" + place.getLat() + "," + place.getLon();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); //req to open google map

            intent.setPackage("com.google.android.apps.maps"); //force to open only google map

            context.startActivity(intent);
        });

        return convertView;
    }
}