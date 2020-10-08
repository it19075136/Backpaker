package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddTripDetails extends AppCompatActivity {

    Button save, reset, showTrips;
    Trip trip;
    EditText inLocation, inDestination;
    DatabaseReference dataRef;
    AutoCompleteTextView editVehicleTypeDrop, editFuelTypeDrop, editDrivetrainDrop;
    TextInputLayout editVehicleType, editFuelType, editDrivetrain;
    private static DecimalFormat df = new DecimalFormat("0.00");
    FusedLocationProviderClient fusedLocationProviderClient; //fusedLocation object
    String sType;
    double lat1 = 0, long1 = 0,lat2 = 0, long2 = 0;
    int flag = 0;
    List<Trip> trip_list;
    boolean exist = false;
    DrawerLayout drawerLayout;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_details);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        editVehicleType = findViewById(R.id.editVehicleType);
        editVehicleTypeDrop = findViewById(R.id.editVehicleTypeDrop);

        editFuelType = findViewById(R.id.upFuelTypeWrap);
        editFuelTypeDrop = findViewById(R.id.editFuelTypeDrop);

        editDrivetrain = findViewById(R.id.upDrivetrain);
        editDrivetrainDrop = findViewById(R.id.DrivetrainDrop);

        inLocation = findViewById(R.id.upLocation);
        editVehicleTypeDrop = findViewById(R.id.editVehicleTypeDrop);
        inDestination = findViewById(R.id.upDestination);

        Uid = FirebaseAuth.getInstance().getUid();

        drawerLayout = findViewById(R.id.drawerLayout);
        //Initialize places
        Places.initialize(getApplicationContext(), "AIzaSyBA4NJujf77g_GZs6gOYm2Ic6tiK-xncKQ");

        //Set source field non focusable
        inLocation.setFocusable(false);
        inLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define type
                sType = "source";
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(AddTripDetails.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });

        inDestination.setFocusable(false);
        inDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define type
                sType = "Destination";
                //Initialize place fields
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                //Create Intent
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(AddTripDetails.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });

        String[] drivetrain = new String[]{
                "4WD",
                "2WD",
                "AWD"
        };

        String[] fuelType = new String[]{
                "Diesel",
                "Petrol",
                "Hybrid"
        };

        String[] vehType = new String[]{
                "SUV-auto",
                "SUV-manual",
                "Sedan-auto",
                "Sedan-manual",
                "Hatchback-auto",
                "Hatchback-manual",
                "Bike"
        };

        ArrayAdapter<String> VehTypeAdapter = new ArrayAdapter<>(
                AddTripDetails.this, R.layout.type_dropdown,
                vehType
        );

        ArrayAdapter<String> FuelTypeAdapter = new ArrayAdapter<>(
                AddTripDetails.this, R.layout.type_dropdown,
                fuelType
        );

        ArrayAdapter<String> DrivetrainAdapter = new ArrayAdapter<>(
                AddTripDetails.this, R.layout.type_dropdown,
                drivetrain
        );

        editVehicleTypeDrop.setAdapter(VehTypeAdapter);
        editFuelTypeDrop.setAdapter(FuelTypeAdapter);
        editDrivetrainDrop.setAdapter(DrivetrainAdapter);

        save = findViewById(R.id.btnUpdate);
        reset = findViewById(R.id.btnReset);
        showTrips = findViewById(R.id.btnShowTrips);
        editVehicleTypeDrop = findViewById(R.id.editVehicleTypeDrop);
        editFuelTypeDrop = findViewById(R.id.editFuelTypeDrop);
        editDrivetrainDrop = findViewById(R.id.DrivetrainDrop);

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        trip = new Trip();
        trip_list = new ArrayList<>();

        dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.child("Trips/".concat(Uid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    Trip trip1 = childSnapshot.getValue(Trip.class);
                    trip_list.add(trip1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query last = FirebaseDatabase.getInstance().getReference().child("Trips/".concat(Uid)).orderByKey().limitToLast(1);
        final String[] latestKey = new String[1];
        latestKey[0] = "0";
        last.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    latestKey[0] = childDataSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        showTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(getApplicationContext(), ViewMyTrips.class);
                startActivity(I);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataRef = FirebaseDatabase.getInstance().getReference();
                dataRef.child("Trips/".concat(Uid)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                            Trip trip1 = childSnapshot.getValue(Trip.class);
                            trip_list.add(trip1);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dataRef = FirebaseDatabase.getInstance().getReference();
                try {
                    if (TextUtils.isEmpty(inLocation.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Location must be entered", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(inDestination.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Destination must be entered", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(editVehicleTypeDrop.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Vehicle Type must be entered", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(editFuelTypeDrop.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Fuel Type must be entered", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(editDrivetrainDrop.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Drivetrain must be entered", Toast.LENGTH_SHORT).show();
                    else {
                        dataRef = dataRef.child("Trips");
                        trip.setVehicleType(editVehicleTypeDrop.getText().toString().trim());
                        trip.setLocation(inLocation.getText().toString().trim());
                        trip.setDestination(inDestination.getText().toString().trim());
                        trip.setFuelType(editFuelTypeDrop.getText().toString().trim());
                        trip.setDrivetrain(editDrivetrainDrop.getText().toString().trim());
                        trip.setId(GetTripId(latestKey[0]));
                        trip.setUid(Uid);
                        SetFuelCost(trip.getDistance(),trip.getVehicleType(),trip.getDrivetrain(),trip.getFuelType());
                        exist = false;
                        for (int j=0; j < trip_list.size(); j++){
                            if(inDestination.getText().toString().trim().equals(trip_list.get(j).getDestination().trim())
                                    && inLocation.getText().toString().trim().equals(trip_list.get(j).getLocation().trim()))
                                exist = true;
                        }
                        if (!exist) {
                            dataRef.child(Uid).child(Integer.toString(GetTripId(latestKey[0])).trim()).setValue(trip);
                            Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                            ClearControls();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Trip already exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Id", Toast.LENGTH_SHORT).show();
                }
                    Query last = FirebaseDatabase.getInstance().getReference().child("Trips/".concat(Uid)).orderByKey().limitToLast(1);
                    last.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                                latestKey[0] = childDataSnapshot.getKey();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearControls();
            }
        });

    }

    private void SetFuelCost(Double distance,String vehicleType,String drivetrain,String fuelType){
        switch (vehicleType){
            case "SUV-auto":
                if((drivetrain.equals("4WD") || drivetrain.equals("AWD"))) {
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 5.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 5.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 15.0 * 138.0)));
                }
                else{
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 7.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 7.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 17.0 * 138.0)));
                }
                break;
            case "SUV-manual":
                if((drivetrain.equals("4WD") || drivetrain.equals("AWD"))) {
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 7.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 7.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 17.0 * 138.0)));
                }
                else{
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 9.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 9.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 19.0 * 138.0)));
                }
                break;
            case "Sedan-auto":
                if((drivetrain.equals("4WD") || drivetrain.equals("AWD"))) {
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 10.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 10.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 17.0 * 138.0)));
                }
                else{
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 12.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 12.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 19.0 * 138.0)));
                }
                break;
            case "Sedan-manual":
            case "Hatchback-auto":
                if((drivetrain.equals("4WD") || drivetrain.equals("AWD"))) {
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 12.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 12.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 19.0 * 138.0)));
                }
                else{
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 14.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 14.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 21.0 * 138.0)));
                }
                break;
            case "Hatchback-manual":
                if((drivetrain.equals("4WD") || drivetrain.equals("AWD"))) {
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 14.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 14.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 21.0 * 138.0)));
                }
                else{
                    if (fuelType.equals("Diesel"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 16.0 * 128.0)));
                    else if (fuelType.equals("Petrol"))
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 16.0 * 138.0)));
                    else
                        trip.setFuelCost(Double.parseDouble(df.format(distance / 23.0 * 138.0)));
                }
                break;
            case "Bike":
                trip.setFuelCost(Double.parseDouble(df.format(distance / 30.0 * 138.0)));
                break;
            default:
                trip.setFuelCost(0.0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check Condition
        if (requestCode == 100 || requestCode == RESULT_OK){
            //When Success
            //Initialize Place
            Place place = null;
            if(data != null) {
                place = Autocomplete.getPlaceFromIntent(data);
            }
            //Check condition
            if(sType.equals("source")){
                //When type is source
                //Increase flag value
                flag++;
                //Set an address on Edit Text
                if(place != null) {
                    inLocation.setText(place.getAddress());
                }
                else
                    getLocation();
                //Get latitude and Longitude
                assert place != null;
                String sSource = String.valueOf(place.getLatLng());
                sSource = sSource.replaceAll("lat/lng:","");
                sSource = sSource.replace("(","");
                sSource = sSource.replace(")","");
                String[] split = sSource.split(",");
                lat1 = Double.parseDouble(split[0]);
                long1 = Double.parseDouble(split[1]);
            }
            else{
                //when type is destination
                //Increase flag value
                flag++;
                //Set an address on Edit Text
                assert place != null;
                inDestination.setText(place.getAddress());
                //Get latitude and longitude
                String sDestination = String.valueOf(place.getLatLng());
                sDestination = sDestination.replaceAll("lat/lng:","");
                sDestination = sDestination.replace("(","");
                sDestination = sDestination.replace(")","");
                String[] split = sDestination.split(",");
                lat2 = Double.parseDouble(split[0]);
                long2 = Double.parseDouble(split[1]);
            }

            //Check condition
            if ((flag > 1 && !(inLocation.getText().toString().matches("") || inDestination.getText().toString().matches(""))) || flag > 2){
                //When flag is greater than and equal to 2
                //Calculate distance
                distance(lat1,long1,lat2,long2);
            }
            else if (requestCode == AutocompleteActivity.RESULT_ERROR){
                //when error
                //Initialize Status
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void distance(double lat1, double long1, double lat2, double long2) {
        //Calculate longitude difference
        double longDiff = long1 - long2;
        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        //convert distance radian to degree
        distance = rad2deg(distance);
        //Distance in miles
        distance = distance * 60 * 1.1515;
        //Distance  in kilometres
        distance = distance * 1.609344;
        //Set distance
        Toast.makeText(getApplicationContext(),"Distance : ".concat(Double.toString(distance)),Toast.LENGTH_SHORT).show();
        if(distance/30.0 >= 1) {
            String hrs = String.format(Locale.getDefault(),"%2d hrs",(int)(distance/30));
            String mins = String.format(Locale.getDefault()," %2d mins", (int)(distance%30));
            trip.setTravelTime(hrs+mins);
        }
        else{
            trip.setTravelTime(String.format(Locale.getDefault(),"%2d mins", (int)(distance%30)));
        }
        trip.setDistance(distance);
    }

    //convert radian to degree
    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    //convert degree to radian
    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize GeoCoder
                        Geocoder geocoder = new Geocoder(AddTripDetails.this, Locale.getDefault());
                        //Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //Set current location Address
                        inLocation.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        }
        else {
            //when permission denied
            ActivityCompat.requestPermissions(AddTripDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void ClearControls(){
        inLocation.setText("");
        inDestination.setText("");
        editVehicleTypeDrop.setText("");
        editFuelTypeDrop.setText("");
        editDrivetrainDrop.setText("");
    }

    private int GetTripId(String lkey){
        return Integer.parseInt(lkey)+1;
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    private static void redirectActivity(Activity activity, Class aClass){
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