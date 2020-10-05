package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMyTrips extends AppCompatActivity implements ViewTripFragment.onDeleteEventListener {

    DatabaseReference dbRef;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw;
    Boolean switchCheck = false;
    List<String> trips;
    Bundle bundle = new Bundle();
    Trip trip;
    Query db;
    TextInputLayout destinationSelector;
    AutoCompleteTextView destDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_trips);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        trips = new ArrayList<>();

        destinationSelector = findViewById(R.id.destinationSelector);
        destDrop = findViewById(R.id.destinationDrop);

        dbRef =  FirebaseDatabase.getInstance().getReference();
        dbRef.child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trips.clear();
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    Trip ntrip = childSnapshot.getValue(Trip.class);
                    trips.add(ntrip.Location.concat("-".concat(ntrip.getDestination())));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> DestinationAdapter = new ArrayAdapter<>(
                ViewMyTrips.this, R.layout.type_dropdown,
                trips
        );

        destDrop.setAdapter(DestinationAdapter);

        findViewById(R.id.navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        sw = findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchCheck = b;
            }
        });

        destDrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                String SelectedTrip = destDrop.getText().toString();
                String[] Selected = SelectedTrip.split("-", 2);
                Toast.makeText(getApplicationContext(),Selected[1],Toast.LENGTH_SHORT).show();
                db = FirebaseDatabase.getInstance().getReference("Trips").orderByChild("destination").equalTo(Selected[1]);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnap : snapshot.getChildren()) {
                            trip = childSnap.getValue(Trip.class);
                        }
                        bundle.putString("id", trip.getId().toString());
                        bundle.putString("distance", trip.getDistance().toString());
                        bundle.putString("destination", trip.getDestination());
                        bundle.putString("location", trip.getLocation());
                        bundle.putString("vType", trip.getVehicleType());
                        bundle.putString("fType", trip.getFuelType());
                        bundle.putString("drivetrain", trip.getDrivetrain());
                        bundle.putString("travelTime",trip.getTravelTime());
                        bundle.putString("fuelCost",trip.getFuelCost().toString());
                        ChangeFragment(view);
                    }
                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                             }
                });
            }
        });
    }


    public void ChangeFragment(View view){

        Fragment frag;
        frag = new ViewTripFragment();

        if(switchCheck){
            frag = new EditTripFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        frag.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, frag);
        ft.commit();

    }

    @Override
    public void DeleteEvent(String s) {
        trips.remove(s);
        ArrayAdapter<String> DestinationAdapter = new ArrayAdapter<>(
                ViewMyTrips.this, R.layout.type_dropdown,
                trips
        );

        destDrop.setAdapter(DestinationAdapter);
        destDrop.showDropDown();
        bundle.clear();
        Fragment frag;
        frag = new ViewTripFragment();
        FragmentManager fm = getSupportFragmentManager();
        frag.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, frag);
        ft.commit();
    }
}
