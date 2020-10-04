package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTripFragment extends Fragment{

    View view;
    TextView viewDistance,viewLocation,viewDrivetrain,FuelType,VehicleType,viewDestination,viewTravel,viewFuelCost;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_trip, container, false);
        viewDistance = view.findViewById(R.id.viewDistance);
        viewLocation = view.findViewById(R.id.viewLocation2);
        viewDrivetrain = view.findViewById(R.id.viewDrivetrain2);
        FuelType = view.findViewById(R.id.viewFuelType2);
        VehicleType = view.findViewById(R.id.viewVehicleType2);
        viewDestination = view.findViewById(R.id.viewDestEdit2);
        viewTravel = view.findViewById(R.id.viewTravelTime);
        viewFuelCost = view.findViewById(R.id.viewFuelCost);

        if (this.getArguments() != null){
            viewDistance.setText(this.getArguments().getString("distance"));
            viewLocation.setText(this.getArguments().getString("location"));
            viewDrivetrain.setText(this.getArguments().getString("drivetrain"));
            VehicleType.setText(this.getArguments().getString("vType"));
            FuelType.setText(this.getArguments().getString("fType"));
            viewDestination.setText(this.getArguments().getString("destination"));
            viewTravel.setText(this.getArguments().getString("travelTime"));
            viewFuelCost.setText(this.getArguments().getString("fuelCost")  );
            id = this.getArguments().getString("id");
//            Toast.makeText(getContext(),id,Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}