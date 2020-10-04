package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationDetail extends AppCompatActivity {

    DatabaseReference dbRef;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw;
    Boolean switchCheck = false;
    List<String> trips;
    Bundle bundle = new Bundle();
    View view;
    Intent intent;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        sw = findViewById(R.id.switch1);
        btnDelete = findViewById(R.id.btnDel);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        trips = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();

        final String locationName = intent.getStringExtra("locName");

        Query query = FirebaseDatabase.getInstance().getReference("location").orderByChild("locationName").equalTo(locationName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Location location = childSnap.getValue(Location.class);
                    bundle.putString("locationName",location.getLocationName());
                    bundle.putString("route",location.getRoute());
                    bundle.putString("note",location.getNote());
                    bundle.putString("accommondation",location.getAccommondation());
                    bundle.putString("NOofDays",location.getNOofDays());
                    bundle.putString("roadCondition",location.getRoadCondition());
                    bundle.putString("Weather",location.getWeather());
                    bundle.putString("permission",location.getPermission());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("location/".concat(locationName));
                dbRef.removeValue();
                Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT);
                bundle.putString("checkDel","yes");
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchCheck = b;
            }
        });
        ChangeFragment(view);
    }

    public void ChangeFragment(View view) {
        Fragment frag;

        if (switchCheck) {
            frag = new EditLocationDetailsFragment();
            FragmentManager fm = getSupportFragmentManager();
            frag.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, frag);
            ft.commit();
        } else {
            frag = new ViewLocationDetailsFragment();
            frag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, frag);
            ft.commit();
        }
    }
}


