package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class EditTripFragment extends Fragment {

    DatabaseReference dbRef;
    View view;
    Button btnUpdate;
    AutoCompleteTextView upVehicleType,upFuelType,upDrivetrain;
    Trip trip = new Trip();
    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_trip, container, false);
        btnUpdate =  view.findViewById(R.id.btnUpdate);
        upVehicleType = view.findViewById(R.id.VehicleTypeDrop);
        upDrivetrain = view.findViewById(R.id.DrivetrainDrop);
        upFuelType = view.findViewById(R.id.FuelTypeDrop);

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
                getContext(), R.layout.type_dropdown,
                vehType
        );

        ArrayAdapter<String> FuelTypeAdapter = new ArrayAdapter<>(
                getContext(), R.layout.type_dropdown,
                fuelType
        );

        ArrayAdapter<String> DrivetrainAdapter = new ArrayAdapter<>(
                getContext(), R.layout.type_dropdown,
                drivetrain
        );

        upVehicleType.setAdapter(VehTypeAdapter);
        upFuelType.setAdapter(FuelTypeAdapter);
        upDrivetrain.setAdapter(DrivetrainAdapter);

        if (this.getArguments() != null){
            upDrivetrain.setText(this.getArguments().getString("drivetrain"));
            upVehicleType.setText(this.getArguments().getString("vType"));
            upFuelType.setText(this.getArguments().getString("fType"));
        }
        final String id = this.getArguments().getString("id");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChildRef = "Trips/"+id;
                dbRef = FirebaseDatabase.getInstance().getReference().child(ChildRef);
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnap:snapshot.getChildren()) {
                            trip = snapshot.getValue(Trip.class);
                        }
                        if(!upVehicleType.getText().toString().trim().equals(""))
                            dbRef.child("vehicleType").setValue(upVehicleType.getText().toString().trim());
                        if(!upFuelType.getText().toString().trim().equals(""))
                            dbRef.child("fuelType").setValue(upFuelType.getText().toString().trim());
                        if(!upDrivetrain.getText().toString().trim().equals(""))
                            dbRef.child("drivetrain").setValue(upDrivetrain.getText().toString().trim());
                        SetFuelCost(trip.getDistance(),upVehicleType.getText().toString(),upDrivetrain.getText().toString(),upFuelType.getText().toString());
                        dbRef.child("fuelCost").setValue(trip.getFuelCost());
                        Toast.makeText(getContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
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
}