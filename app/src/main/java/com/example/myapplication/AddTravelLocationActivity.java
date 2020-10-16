package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddTravelLocationActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    Button btnAddLoc;
    DatabaseReference dbRef;
    List<String> locations;
    Query db;
    Location location;
    String locNam;
    EditText txtnLocdays,txtnLocAcc,txtnLocRoad,txtnLocPermi,txtnLocRoute,txtnLocNotes,txtnLocWeather,txtLocType;
    AutoCompleteTextView txtLocName;
    View view;
//    String locNam;
    Button btnUpdate, btnDelete;
//    EditText upNoofDays, upAccommondation, upRoadCondition, upRoute, upnote, upweather, uppermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__travel_location);
        textInputLayout = findViewById(R.id.text_input_layout);
        locations = new ArrayList<>();
        btnDelete = findViewById(R.id.buttonLocDelete);
        btnAddLoc = findViewById(R.id.buttonLocADD);
        txtLocName = findViewById(R.id.txtnLocName);
        txtLocType = findViewById(R.id.txtnLocType);
        locations = new ArrayList<>();
//        view = inflater.inflate(R.layout.activity_add__travel_location, container, false);
        btnUpdate = findViewById(R.id.buttonLocUpdate);
        txtnLocdays = findViewById(R.id.txtnLocdays);
        txtnLocAcc = findViewById(R.id.txtnLocAcc);
        txtnLocRoad = findViewById(R.id.txtnLocRoad);
        txtnLocPermi = findViewById(R.id.txtnLocPermi);
        txtnLocRoute = findViewById(R.id.txtnLocRoute);
        txtnLocNotes = findViewById(R.id.txtnLocNotes);
        txtnLocWeather = findViewById(R.id.txtnLocWeather);
        locNam=String.valueOf(txtLocName);
//        btnAddLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dbRef = FirebaseDatabase.getInstance().getReference().child("locations/");
////                if (TextUtils.isEmpty(txtName.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
////                }
//                Intent intent = new Intent(getApplicationContext(),AddTravelLocationActivity.class);
//                startActivity(intent);
//            }
//        });

        btnAddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ref = "location/" + txtLocName.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child(ref);

                if (TextUtils.isEmpty(txtLocName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtLocType.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Empty type", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtnLocdays.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty no of days", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocAcc.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Accommodation", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocRoad.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty RoadCondition", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocPermi.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Permission", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocRoute.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Route", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocNotes.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Note", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtnLocWeather.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty weather", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.child("locationName").setValue(txtLocName.getText().toString().trim());
                    dbRef.child("type").setValue(txtLocType.getText().toString().trim());
                    dbRef.child("NOofDays").setValue(txtnLocdays.getText().toString().trim());
                    dbRef.child("accommondation").setValue(txtnLocAcc.getText().toString().trim());
                    dbRef.child("roadCondition").setValue(txtnLocRoad.getText().toString().trim());
                    dbRef.child("permission").setValue(txtnLocPermi.getText().toString().trim());
                    dbRef.child("route").setValue(txtnLocRoute.getText().toString().trim());
                    dbRef.child("note").setValue(txtnLocNotes.getText().toString().trim());
                    dbRef.child("Weather").setValue(txtnLocWeather.getText().toString().trim());

//                    Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(view, "Successfully inserted", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1000);
                    snackbar.show();
                    clearControls();
                }

            }
        });


//        if (this.getArguments() != null) {
//            upnote.setText(this.getArguments().getString("note"));
//            upRoute.setText(this.getArguments().getString("route"));
//            upNoofDays.setText(this.getArguments().getString("NOofDays"));
//            upRoadCondition.setText(this.getArguments().getString("roadCondition"));
//            upweather.setText(this.getArguments().getString("Weather"));
//            uppermission.setText(this.getArguments().getString("permission"));
//            upAccommondation.setText(this.getArguments().getString("accommondation"));
//            locNam = this.getArguments().getString("locationName");
//        }

//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String ref = "location/" + txtLocName;
//                dbRef = FirebaseDatabase.getInstance().getReference().child(ref);
//                if (!upNoofDays.getText().toString().trim().equals(""))
//                    dbRef.child("NOofDays").setValue(upNoofDays.getText().toString().trim());
//                if (!upAccommondation.getText().toString().trim().equals(""))
//                    dbRef.child("accommondation").setValue(upAccommondation.getText().toString().trim());
//                if (!upRoadCondition.getText().toString().trim().equals(""))
//                    dbRef.child("roadCondition").setValue(upRoadCondition.getText().toString().trim());
//                if (!upRoute.getText().toString().trim().equals(""))
//                    dbRef.child("route").setValue(upRoute.getText().toString().trim());
//                if (!upnote.getText().toString().trim().equals(""))
//                    dbRef.child("note").setValue(upnote.getText().toString().trim());
//                if (!upweather.getText().toString().trim().equals(""))
//                    dbRef.child("Weather").setValue(upweather.getText().toString().trim());
//                if (!uppermission.getText().toString().trim().equals(""))
//                    dbRef.child("permission").setValue(uppermission.getText().toString().trim());
//                Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return view;

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("location/".concat(txtLocName.getText().toString()));
                dbRef.removeValue();
                Snackbar snackbar = Snackbar.make(view, "Deleted Successfully", Snackbar.LENGTH_LONG);
                snackbar.setDuration(1000);
                snackbar.show();
//            bundle.putString("checkDel","yes");
                clearControls();
            }
        });

        dbRef =  FirebaseDatabase.getInstance().getReference();
        dbRef.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locations.clear();
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    Location nlocation = childSnapshot.getValue(Location.class);
//                    locations.add(nlocation.getLocationName());
                    locations.add(nlocation.getLocationName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> DestinationAdapter = new ArrayAdapter<>(
                AddTravelLocationActivity.this, R.layout.type_dropdown,
                locations
        );

        txtLocName.setAdapter(DestinationAdapter);
        txtLocName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                db = FirebaseDatabase.getInstance().getReference("location").orderByChild("locationName").equalTo(txtLocName.getText().toString());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnap : snapshot.getChildren()) {
                            location = childSnap.getValue(Location.class);
                        }
//                        bundle.putString("id", location.getId().toString());
//                        bundle.putString("distance", location.getDistance().toString());
//                        bundle.putString("destination", location.getDestination());
//                        bundle.putString("location", location.getLocation());
//                        bundle.putString("vType", location.getVehicleType());
//                        bundle.putString("fType", location.getFuelType());
//                        bundle.putString("drivetrain", location.getDrivetrain());
//                        bundle.putString("travelTime",location.getTravelTime());
//                        bundle.putString("fuelCost",location.getFuelCost().toString());
                        txtLocName.setText(location.getLocationName());
                        txtLocType.setText(location.getType());
                        txtnLocdays.setText(location.getNOofDays());
                        txtnLocWeather.setText(location.getWeather());
                        txtnLocNotes.setText(location.getNote());
                        txtnLocRoute.setText(location.getNote());
                        txtnLocPermi.setText(location.getPermission());
                        txtnLocRoad.setText(location.getRoadCondition());
                        txtnLocAcc.setText(location.getAccommondation());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
//        view = inflater.inflate(R.layout.fragment_edit_location_details, container, false);
//        btnUpdate =  view.findViewById(R.id.buttonLocUpdate);
//        upNoofDays = view.findViewById(R.id.NoofDays);
//        upAccommondation = view.findViewById(R.id.Accommondation);
//        upRoadCondition = view.findViewById(R.id.RoadCondition);
//        uppermission = view.findViewById(R.id.permission);
//        upRoute = view.findViewById(R.id.Route);
//        upnote = view.findViewById(R.id.note);
//        upweather = view.findViewById(R.id.weather);

//        if(this.getArguments() != null) {
//            txtnLocNotes.setText(this.getArguments().getString("note"));
//            txtnLocRoute.setText(this.getArguments().getString("route"));
//            txtnLocdays.setText(this.getArguments().getString("NOofDays"));
//            txtnLocRoad.setText(this.getArguments().getString("roadCondition"));
//            txtnLocWeather.setText(this.getArguments().getString("Weather"));
//            txtnLocPermi.setText(this.getArguments().getString("permission"));
//            txtnLocAcc.setText(this.getArguments().getString("accommondation"));
//            locNam = this.getArguments().getString("locationName");
//        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  ref = "location/"+txtLocName.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child(ref);
                if(!txtnLocdays.getText().toString().trim().equals(""))
                    dbRef.child("NOofDays").setValue(txtnLocdays.getText().toString().trim());
                if(!txtnLocAcc.getText().toString().trim().equals(""))
                    dbRef.child("accommondation").setValue(txtnLocAcc.getText().toString().trim());
                if(!txtnLocRoad.getText().toString().trim().equals(""))
                    dbRef.child("roadCondition").setValue(txtnLocRoad.getText().toString().trim());
                if(!txtnLocRoute.getText().toString().trim().equals(""))
                    dbRef.child("route").setValue(txtnLocRoute.getText().toString().trim());
                if(!txtnLocNotes.getText().toString().trim().equals(""))
                    dbRef.child("note").setValue(txtnLocNotes.getText().toString().trim());
                if(!txtnLocWeather.getText().toString().trim().equals(""))
                    dbRef.child("Weather").setValue(txtnLocWeather.getText().toString().trim());
                if(!txtnLocPermi.getText().toString().trim().equals(""))
                    dbRef.child("permission").setValue(txtnLocPermi.getText().toString().trim());
                if(!txtLocType.getText().toString().trim().equals(""))
                    dbRef.child("type").setValue(txtLocType.getText().toString().trim());
                Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                clearControls();
            }
        });
//        return view;

    }

    private void clearControls() {
        txtLocName.setText(" ");
        txtLocType.setText("");
        txtnLocdays.setText("");
        txtnLocWeather.setText("");
        txtnLocNotes.setText("");
        txtnLocRoute.setText("");
        txtnLocPermi.setText("");
        txtnLocRoad.setText("");
        txtnLocAcc.setText("");
    }
}
