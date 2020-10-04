package com.example.myapplication;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditLocationDetailsFragment extends Fragment {

    DatabaseReference dbRef;
    View view;
    String locNam;
    Button btnUpdate;
    EditText upNoofDays,upAccommondation,upRoadCondition,upRoute,upnote,upweather,uppermission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_location_details, container, false);
        btnUpdate =  view.findViewById(R.id.button12);
        upNoofDays = view.findViewById(R.id.NoofDays);
        upAccommondation = view.findViewById(R.id.Accommondation);
        upRoadCondition = view.findViewById(R.id.RoadCondition);
        uppermission = view.findViewById(R.id.permission);
        upRoute = view.findViewById(R.id.Route);
        upnote = view.findViewById(R.id.note);
        upweather = view.findViewById(R.id.weather);

        if(this.getArguments() != null) {
            upnote.setText(this.getArguments().getString("note"));
            upRoute.setText(this.getArguments().getString("route"));
            upNoofDays.setText(this.getArguments().getString("NOofDays"));
            upRoadCondition.setText(this.getArguments().getString("roadCondition"));
            upweather.setText(this.getArguments().getString("Weather"));
            uppermission.setText(this.getArguments().getString("permission"));
            upAccommondation.setText(this.getArguments().getString("accommondation"));
         locNam = this.getArguments().getString("locationName");
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String  ref = "location/"+locNam;
                dbRef = FirebaseDatabase.getInstance().getReference().child(ref);
                if(!upNoofDays.getText().toString().trim().equals(""))
                    dbRef.child("NOofDays").setValue(upNoofDays.getText().toString().trim());
                if(!upAccommondation.getText().toString().trim().equals(""))
                    dbRef.child("accommondation").setValue(upAccommondation.getText().toString().trim());
                if(!upRoadCondition.getText().toString().trim().equals(""))
                    dbRef.child("roadCondition").setValue(upRoadCondition.getText().toString().trim());
                if(!upRoute.getText().toString().trim().equals(""))
                    dbRef.child("route").setValue(upRoute.getText().toString().trim());
                if(!upnote.getText().toString().trim().equals(""))
                    dbRef.child("note").setValue(upnote.getText().toString().trim());
                if(!upweather.getText().toString().trim().equals(""))
                    dbRef.child("Weather").setValue(upweather.getText().toString().trim());
                if(!uppermission.getText().toString().trim().equals(""))
                    dbRef.child("permission").setValue(uppermission.getText().toString().trim());
                Toast.makeText(getContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
