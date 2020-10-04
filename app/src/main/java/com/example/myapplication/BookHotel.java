package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BookHotel extends AppCompatActivity {

    EditText gusName, gusCon, gusEmail, numRms, numGus, numNights;
    RadioGroup radiGrp;
    RadioButton rdiBtn;
    Button bookBtn, showBtn, updBtn, delBtn;
    DatabaseReference dbRef;
    Booking booking;
    String[] latestKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hotel);
        gusName = findViewById(R.id.guestName);
        gusCon = findViewById(R.id.gusCon);
        gusEmail = findViewById(R.id.gusEmail);
        numRms = findViewById(R.id.numRooms);
        numGus = findViewById(R.id.numGuset);
        numNights = findViewById(R.id.numNight);
        radiGrp = findViewById(R.id.rdgrp);
        bookBtn = findViewById(R.id.btnSave);
        showBtn = findViewById(R.id.btnShow);
        updBtn = findViewById(R.id.btnUpd);
        delBtn = findViewById(R.id.btnDel);

        booking = new Booking();

        Query key  = FirebaseDatabase.getInstance().getReference().child("Booking").orderByKey().limitToLast(1);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        latestKey = new String[1];
        latestKey[0] = "0";

        key.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childDataSnapshot: snapshot.getChildren()){
                    latestKey[0] = childDataSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = booking.getGusId().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child("Booking/".concat(id));
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            gusName.setText(dataSnapshot.child("gusName").getValue().toString());
                            gusCon.setText(dataSnapshot.child("gusContact").getValue().toString());
                            gusEmail.setText(dataSnapshot.child("gusEmail").getValue().toString());
                            String roomType  = dataSnapshot.child("reqRoomType").getValue().toString();
                            if(roomType.equalsIgnoreCase("A/C")) {
                                rdiBtn.setChecked(true);
                            }
                            numRms.setText(dataSnapshot.child("roomCou").getValue().toString());
                            numGus.setText(dataSnapshot.child("gusNumCou").getValue().toString());
                            numNights.setText(dataSnapshot.child("nightCou").getValue().toString());

                        }else{
                            Toast.makeText(getApplicationContext(), "Cannot Find Details...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(TextUtils.isEmpty(gusName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Name Field", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(gusCon.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Contact Number", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(gusEmail.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Email Address", Toast.LENGTH_SHORT).show();
                    else if(radiGrp.getCheckedRadioButtonId() == -1)
                        Toast.makeText(getApplicationContext(), "Empty Room Type", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(numRms.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Room Count", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(numGus.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Member Count", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(numNights.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Day Count", Toast.LENGTH_SHORT).show();

                    else{

                        booking.setGusName(gusName.getText().toString().trim());
                        booking.setGusContact(Integer.parseInt(gusCon.getText().toString().trim()));
                        booking.setGusEmail(gusEmail.getText().toString().trim());
                        rdiBtn = (RadioButton)findViewById(radiGrp.getCheckedRadioButtonId());
                        //if(radioId == 1)
                            booking.setReqRoomType(rdiBtn.getText().toString());
                        //else
                          //  booking.setReqRoomType("NON-AC Room");

                        booking.setGusNumCou(Integer.parseInt(numGus.getText().toString().trim()));
                        booking.setNightCou(Integer.parseInt(numNights.getText().toString().trim()));
                        booking.setRoomCou(Integer.parseInt(numRms.getText().toString().trim()));

                        booking.setGusId(GetguestId(latestKey[0]));
                        dbRef.child(Integer.toString(GetguestId(latestKey[0])).trim()).setValue(booking);
                        //dbRef.child("htl1").setValue(htl);
                        Toast.makeText(getApplicationContext(), "Your Hotel Booked..", Toast.LENGTH_SHORT).show();
                        clearControl();

                    }
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Number Format Error", Toast.LENGTH_SHORT).show();
                }

                Query key  = FirebaseDatabase.getInstance().getReference().child("Booking").orderByKey().limitToLast(1);
                key.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot childDataSnapshot: snapshot.getChildren()){
                            latestKey[0] = childDataSnapshot.getKey();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        delBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Booking/".concat(booking.getGusId().toString()));
                dbRef.removeValue();
                Toast.makeText(getApplicationContext(), "Booking Canceled... ", Toast.LENGTH_SHORT).show();
            }
        }));

        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Booking/".concat(booking.getGusId().toString()));
                dbRef.child("gusName").setValue(gusName.getText().toString().trim());
                dbRef.child("gusContact").setValue(gusCon.getText().toString().trim());
                dbRef.child("gusEmail").setValue(gusEmail.getText().toString().trim());
                dbRef.child("gusNumCou").setValue(numGus.getText().toString().trim());
                dbRef.child("roomCou").setValue(numRms.getText().toString().trim());
                dbRef.child("nightCou").setValue(numNights.getText().toString().trim());
                rdiBtn = (RadioButton)findViewById(radiGrp.getCheckedRadioButtonId());
                dbRef.child("reqRoomType").setValue(rdiBtn.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Booking Updated...", Toast.LENGTH_SHORT).show();
                clearControl();
            }
        });
    }

    private void clearControl() {
        gusName.setText("");
        gusCon.setText("");
        gusEmail.setText("");
        radiGrp.clearCheck();
        numGus.setText("");
        numRms.setText("");
        numNights.setText("");
    }

    private int GetguestId(String lkey) {
        return Integer.parseInt(lkey) + 1;
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
}