package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class EditTripFragment extends Fragment {

    DatabaseReference dbRef;
    View view;
    Button btnUpdate;
    AutoCompleteTextView upVehicleType,upFuelType,upDrivetrain;
    Trip trip = new Trip();
    private static DecimalFormat df = new DecimalFormat("0.00");
    String uId;

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
                "Sedan-auto",
                "Sedan-manual",
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

        uId = FirebaseAuth.getInstance().getUid();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.getArguments() != null){
            upDrivetrain.setText(this.getArguments().getString("drivetrain"));
            upVehicleType.setText(this.getArguments().getString("vType"));
            upFuelType.setText(this.getArguments().getString("fType"));
        }
        final String id = this.getArguments().getString("id");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChildRef = "Trips/"+uId.concat("/"+id);
                dbRef = FirebaseDatabase.getInstance().getReference().child(ChildRef);
                Query q = FirebaseDatabase.getInstance().getReference().child("Trips/".concat(uId)).orderByChild("id").equalTo(Integer.parseInt(id));
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnap:snapshot.getChildren()) {
                            trip = childSnap.getValue(Trip.class);
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
    }

    private void SetFuelCost(Double distance, String vehicleType, String drivetrain, String fuelType){
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
}