package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewLocationDetailsFragment extends Fragment{

    View view;
    TextView ViewNoofDays,RoadCondition,Weather,Permission,Accomodation,notes,route,locationName;
    String SelectedDest;
    Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_location_details, container, false);
//        SelectedDest = this.getArguments().getString("destination").toString();
        ViewNoofDays = view.findViewById(R.id.viewNoofDaysDetails);
        RoadCondition = view.findViewById(R.id.viewRoadConditionDetails);
        Weather = view.findViewById(R.id.viewWeatherDetails);
        Permission = view.findViewById(R.id.viewPermssionDetails);
        notes = view.findViewById(R.id.viewNoteDetails);
        route = view.findViewById(R.id.viewRouteDetails);
        Accomodation = view.findViewById(R.id.viewAccommodationDetails);

//        route.setText(getArguments().toString("route"));
//        ViewNoofDays.setText(getArguments().toString("NOofDays"));
//        RoadCondition.setText(getArguments().toString("roadCondition"));
//        Weather.setText(getArguments().toString("Weather"));
//        Permission.setText(getArguments().toString("permission"));
//        notes.setText(getArguments().toString("note"));
//        Accomodation.setText(getArguments().toString("accommondation"));

        if(this.getArguments() != null) {
            notes.setText(this.getArguments().getString("note"));
            route.setText(this.getArguments().getString("route"));
        ViewNoofDays.setText(this.getArguments().getString("NOofDays"));
        RoadCondition.setText(this.getArguments().getString("roadCondition"));
        Weather.setText(this.getArguments().getString("Weather"));
        Permission.setText(this.getArguments().getString("permission"));
        Accomodation.setText(this.getArguments().getString("accommondation"));
//        if(this.getArguments().getString("checkDel").equals("yes")){
////            clearControls();
//        }
        }
        return view;
    }
//    private void clearControls(){
//        ViewNoofDays.setText("");
//        RoadCondition.setText("");
//        Weather.setText("");
//        Permission.setText("");
//        notes.setText("");
//        Accomodation.setText("");
//        route.setText("");
//    }
}

