package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ViewTripFragment extends Fragment{

    View view;
    TextView viewDistance,viewLocation,viewDrivetrain,FuelType,VehicleType,viewDestination,viewTravel,viewFuelCost;
    String id;
    Button btnDelete;
    DatabaseReference dbRef;
    String uId;

    public interface onDeleteEventListener {
        public void DeleteEvent(String d,String l);
    }

    onDeleteEventListener deleteEventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            deleteEventListener = (onDeleteEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement delete event listener");
        }
    }

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
        btnDelete = view.findViewById(R.id.buttonDelete);

        uId = FirebaseAuth.getInstance().getUid();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.getArguments() != null){
            String distFormat = this.getArguments().getString("distance");
            if (distFormat != null)
                distFormat = String.format(Locale.getDefault(),"%2d Kms",(int)(Double.parseDouble(distFormat)/1));
            viewDistance.setText(distFormat);
            viewLocation.setText(this.getArguments().getString("location"));
            viewDrivetrain.setText(this.getArguments().getString("drivetrain"));
            VehicleType.setText(this.getArguments().getString("vType"));
            FuelType.setText(this.getArguments().getString("fType"));
            viewDestination.setText(this.getArguments().getString("destination"));
            viewTravel.setText(this.getArguments().getString("travelTime"));
            viewFuelCost.setText(this.getArguments().getString("fuelCost")  );
            id = this.getArguments().getString("id");
        }
        else {
            viewDistance.setText("");
            viewLocation.setText("");
            viewDrivetrain.setText("");
            VehicleType.setText("");
            FuelType.setText("");
            viewDestination.setText("");
            viewTravel.setText("");
            viewFuelCost.setText("");
        }

        btnDelete.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbRef =FirebaseDatabase.getInstance().getReference();
                        Query tripsQuery = dbRef.child("Trips/".concat(uId)).orderByChild("id").equalTo(Integer.parseInt(id));
                        tripsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    childSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(getContext(), "Trip Deleted...", Toast.LENGTH_SHORT).show();
                        deleteEventListener.DeleteEvent(viewDestination.getText().toString(),viewLocation.getText().toString());
                    }
                })
        );

    }
}