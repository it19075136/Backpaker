package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
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
    List<String> locations;
    Bundle bundle = new Bundle();
    View view;
    Intent intent;
    Button btnDelete,CampGear,BookHotel,AddTrip;
    DrawerLayout drawerLayout;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

//        sw = findViewById(R.id.switch1);
//        btnDelete = findViewById(R.id.btnDel);
        CampGear = findViewById(R.id.btnGear);
        BookHotel = findViewById(R.id.btnHotel);
        AddTrip = findViewById(R.id.btnTrip);
        drawerLayout = findViewById(R.id.drawerLayout);

        CampGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGear = new Intent(LocationDetail.this,MainActivityDil.class);
                startActivity(iGear);
            }
        });

        BookHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iHotel = new Intent(LocationDetail.this,HotelFinder.class);
                startActivity(iHotel);
            }
        });

        AddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTrip = new Intent(LocationDetail.this,AddTripDetails.class);
                startActivity(iTrip);
            }
        });
        
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        locations = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();

        final String locationName = intent.getStringExtra("locName");

        Query query = FirebaseDatabase.getInstance().getReference("location").orderByChild("locationName").equalTo(locationName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                     location = childSnap.getValue(Location.class);

                }
                bundle.putString("locationName",location.getLocationName().toString());
                bundle.putString("route",location.getRoute().toString());
                bundle.putString("note",location.getNote().toString());
                bundle.putString("accommondation",location.getAccommondation().toString());
                bundle.putString("NOofDays",location.getNOofDays().toString());
                bundle.putString("roadCondition",location.getRoadCondition().toString());
                bundle.putString("Weather",location.getWeather().toString());
                bundle.putString("permission",location.getPermission().toString());
                ChangeFragment(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dbRef = FirebaseDatabase.getInstance().getReference().child("location/".concat(locationName));
//                dbRef.removeValue();
//                Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT);
//                bundle.putString("checkDel","yes");
//            }
//        });

//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                switchCheck = b;
//            }
//        });
        ChangeFragment(view);
    }

    public void ChangeFragment(View view) {
        Fragment frag;

//        if (switchCheck) {
//            frag = new EditLocationDetailsFragment();
//            FragmentManager fm = getSupportFragmentManager();
//            frag.setArguments(bundle);
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.fragment, frag);
//            ft.commit();
//        }

            frag = new ViewLocationDetailsFragment();
            frag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, frag);
            ft.commit();

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

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
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


