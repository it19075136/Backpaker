package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelFinder extends AppCompatActivity {

    AutoCompleteTextView locDrop,htlDrop;
    TextInputLayout locSelector,htlSelector;
    Button findHtl;
    DatabaseReference dbRef;
    List<String> location;
    List<Hotel> hotel;
    List<String> htlName;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_finder);

        location = new ArrayList<>();
        hotel = new ArrayList<>();
        htlName = new ArrayList<>();

        locSelector = findViewById(R.id.locSelect);
        locDrop = findViewById(R.id.locDropdown);
        htlSelector = findViewById(R.id.htlSelector);
        htlDrop = findViewById(R.id.htlDropdown);
        drawerLayout = findViewById(R.id.drawerLayout);

        findHtl = findViewById(R.id.btnFind);

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Hotel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    String spinnerLoc = childSnapshot.child("loca").getValue(String.class);
                    location.add(spinnerLoc);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HotelFinder.this,R.layout.drop_down,location);
        locDrop.setAdapter(arrayAdapter);


        locDrop.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                htlName.clear();
                Query db = FirebaseDatabase.getInstance().getReference("Hotel").orderByChild("loca").equalTo(locDrop.getText().toString());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnap : dataSnapshot.getChildren()){
                            String hotelName = childSnap.child("htlName").getValue(String.class);
                            Hotel hotelObj =  childSnap.getValue(Hotel.class);
                            hotel.add(hotelObj);
                            htlName.add(hotelName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                ArrayAdapter<Hotel> arrayAdapterhtl = new ArrayAdapter<>(HotelFinder.this,R.layout.drop_down,hotel);
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(HotelFinder.this,R.layout.drop_down,htlName);

                htlDrop.setAdapter(arrayAdapter2);
            }
        }));

        findHtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelFinder.this,HotelDetails.class);
                intent.putExtra("hotelNm",htlDrop.getText().toString());
                startActivity(intent);
            }
        });

        }
    public void onStart(){
        super.onStart();
        Log.i("Lifecycle","OnCreate() invoked");
    }

    public void  onRestart(){
        super.onRestart();
        Log.i("Lifecycle", "OnRestart() invoked");
    }

    public void onResume(){
        super.onResume();
        Log.i("Lifecycle", "OnResume() invoked");
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
