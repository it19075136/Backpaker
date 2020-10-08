package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
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
    DrawerLayout drawerLayout;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_trips);

        drawerLayout = findViewById(R.id.drawerLayout);

        trips = new ArrayList<>();
        uId = FirebaseAuth.getInstance().getUid();

        destinationSelector = findViewById(R.id.destinationSelector);
        destDrop = findViewById(R.id.destinationDrop);

        dbRef =  FirebaseDatabase.getInstance().getReference();
        dbRef.child("Trips/".concat(uId)).addValueEventListener(new ValueEventListener() {
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
                db = FirebaseDatabase.getInstance().getReference("Trips/".concat(uId)).orderByChild("destination").equalTo(Selected[1]);
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
    public void DeleteEvent(String d,String l) {
        trips.remove(l.concat("-".concat(d)));
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

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private static void redirectActivity(Activity activity,Class aClass){
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    public void ClickHotels(View view){
        redirectActivity(this,HotelFinder.class);
    }

    public void ClickTrips(View view){
        redirectActivity(this,ViewMyTrips.class);
    }

    public void ClickGear(View view){
        redirectActivity(this,MainActivityDil.class);
    }

    public void ClickLocs(View view){
        redirectActivity(this,PickTravelModeActivity.class);
    }

    public void ClickLogout(View view){
        Logout(this);
    }

    private void Logout(final Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                activity.finishAffinity();
                Intent intent = new Intent(activity,loggedIn.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

}
