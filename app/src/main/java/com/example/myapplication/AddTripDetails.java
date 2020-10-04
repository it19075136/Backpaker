package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
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
                "Saloon-auto",
                "Saloon-manual",
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

        Query last = FirebaseDatabase.getInstance().getReference().child("Trips").orderByKey().limitToLast(1);
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
                dataRef = FirebaseDatabase.getInstance().getReference().child("Trips");
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
                        trip.setVehicleType(editVehicleTypeDrop.getText().toString().trim());
                        trip.setLocation(inLocation.getText().toString().trim());
                        trip.setDestination(inDestination.getText().toString().trim());
                        trip.setFuelType(editFuelTypeDrop.getText().toString().trim());
                        trip.setDrivetrain(editDrivetrainDrop.getText().toString().trim());
                        trip.setId(GetTripId(latestKey[0]));
                        SetFuelCost(trip.getDistance(),trip.getVehicleType(),trip.getDrivetrain(),trip.getFuelType());
                        dataRef.child(Integer.toString(GetTripId(latestKey[0])).trim()).setValue(trip);
                        Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        ClearControls();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Id", Toast.LENGTH_SHORT).show();
                }
                Query last = FirebaseDatabase.getInstance().getReference().child("Trips").orderByKey().limitToLast(1);
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
        switch (fuelType){
            case "Diesel":
                if(vehicleType.equals("SUV-auto") && (drivetrain.equals("4WD") || drivetrain.equals("AWD"))){
                    trip.setFuelCost(Double.parseDouble(df.format(distance/5.0*128.0 )));
                }
                break;
            case "Petrol":
                break;
            case "Hybrid":
                break;
            default:
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

}