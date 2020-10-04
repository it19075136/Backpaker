package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;
//import 'package:flutter/material.dart';
public class AddGuidesDetails extends AppCompatActivity {

    Guide guide;
    FloatingActionButton addguide;
    GridView gridView;
    Query query = FirebaseDatabase.getInstance().getReference().child("Guide").orderByChild("name");
//    dbRef = FirebaseDatabase.getInstance().getReference().child("Guide/locationName");
//    String[] fruitNames = {"peacock ","Madulsima","Kalabokka"};
String[] guideNames = {"pasindu ","sandun","thisara"};
String[] guideNIC = {"981 ","991","971"};
String[] guideemail = {"pasindu@ ","sandun@","thisara@"};
String[] guidephoneNO = {"071 ","076","078"};
String[] guidegender = {"male ","male","female"};
    int[] guideImages = {R.drawable.man,R.drawable.man,R.drawable.man};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__guides_details);
//        addguide=findViewById(R.id.add_guides_form);
        gridView = findViewById(R.id.gridview);
        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),GuidesDetailsActivity.class);
                intent.putExtra("name",guideNames[i]);
                intent.putExtra("NIC",guideNIC[i]);
                intent.putExtra("email",guideemail[i]);
                intent.putExtra("phoneNO",guidephoneNO[i]);
                intent.putExtra("gender",guidegender[i]);
                intent.putExtra("image",guideImages[i]);
                startActivity(intent);
            }
        });

//        addguide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dbRef = FirebaseDatabase.getInstance().getReference().child("Permission");
////                if (TextUtils.isEmpty(txtName.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
////                }
////                else if (TextUtils.isEmpty(txtNic.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty NIC", Toast.LENGTH_SHORT).show();
////                }else if (TextUtils.isEmpty(txtEmail.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Email", Toast.LENGTH_SHORT).show();
////                }else if (TextUtils.isEmpty(txtPhone.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Phone number", Toast.LENGTH_SHORT).show();
////                }
////                else{
////                    pr.setName(txtName.getText().toString().trim());
////                    pr.setEmail(txtEmail.getText().toString().trim());
////                    pr.setNIC(txtNic.getText().toString().trim());
////                    pr.setPhoneNum(Integer.parseInt(txtPhone.getText().toString().trim()));
////                    dbRef.child(UUID.randomUUID().toString()).setValue(pr);
//////                    Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
////                    Snackbar snackbar = Snackbar.make(view,"Successfully inserted",Snackbar.LENGTH_LONG);
////                    snackbar.setDuration(1000);
////                    snackbar.show();
////                    clearControls();
////                }
//
////            @Override
////            public void onClick(View view) {
//////                dbRef = FirebaseDatabase.getInstance().getReference().child("locations/");
//////                if (TextUtils.isEmpty(txtName.getText().toString())){
//////                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
//////                }
//////                Intent intent = new Intent();
////                Intent intent= new Intent(getActivity(),AddTravelLocationActivity.class);
////                startActivity(intent);
////            }
////        });
//                Intent intent= new Intent(getApplicationContext(),GuideForm.class);
//                startActivity(intent);
//            }
//        });



//        btn = (Button)findViewById(R.id.button2);
//        tex = findViewById(R.id.);
//        gridView.setOnClickListener(new AdapterView.OnItemClickListener(){
//           @Override
//           public void onItemClick(AdapterView<?> adapterView,View,int i,long l){
//               Intent intent = new Intent(getApplicationContext(),GuidesDetailsActivity.class);
//                Intent.putExtra("name",fruitNames[i]);
//                Intent.putExtra("image",fruitImages[i]);
//                startActivity(intent);
//
//           }
//        });

    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return guideImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
             View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
//             TextView name= view1.findViewById(R.id.guide);
//             TextView NIC= view1.findViewById(R.id.guide);
//             TextView email= view1.findViewById(R.id.guide);
//             TextView phoneNO= view1.findViewById(R.id.guide);
//             TextView gender= view1.findViewById(R.id.guide);
            ImageView image =view1.findViewById(R.id.image);
//            name.setText(guideNames[i]);
//            NIC.setText(guideNIC[i]);
//            email.setText(guideemail[i]);
//            phoneNO.setText(guidephoneNO[i]);
//            gender.setText(guidegender[i]);
            image.setImageResource(guideImages[i]);
            return view1;


        }
    }
}
