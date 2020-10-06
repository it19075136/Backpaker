package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class HotelDetails extends AppCompatActivity {

    EditText htlName,rmCou,ConNo,Mail,loc,type;
    TextView Des;
    CheckBox acRm,nonAC;
    Button btnUpd, btnDelete;
    DatabaseReference dbRef;
    Button bookNow , callHtl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

        htlName = findViewById(R.id.htlName);
        rmCou = findViewById(R.id.rmCou);
        ConNo= findViewById(R.id.conNum);
        Mail = findViewById(R.id.mail);
        loc = findViewById(R.id.loc);
        type = findViewById(R.id.type);
        Des = findViewById(R.id.txtDes);

        bookNow = findViewById(R.id.btnBook);
        callHtl = findViewById(R.id.btnCon);

        Intent intent = getIntent();

        htlName.setText(intent.getStringExtra("hotelNm"));


        Query db = FirebaseDatabase.getInstance().getReference("Hotel").orderByChild("htlName").equalTo(htlName.getText().toString());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Hotel htl = childSnap.getValue(Hotel.class);
                    rmCou.setText(htl.getRoomCount().toString());
                    ConNo.setText(htl.getPhone().toString());
                    Mail.setText(htl.getEmail());
                    loc.setText(htl.getLoca());
                    type.setText(htl.getRoomtype());
                    Des.setText(htl.getDes());

//                    int r1 = childSnap.child("roomCount").getValue(Integer.class);
//                    rmCou.setText(String.valueOf(r1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //        String hotelChild = "Hotel/"+ String.valueOf(intent.getIntExtra("id",0));
//        dbRef =FirebaseDatabase.getInstance().getReference().child(hotelChild);

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotelDetails.this,BookHotel.class);
                intent.putExtra("htlnm",htlName.getText().toString());
                startActivity(intent);
            }
        });

        
    }
}