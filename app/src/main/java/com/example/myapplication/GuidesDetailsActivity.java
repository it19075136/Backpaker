package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuidesDetailsActivity extends AppCompatActivity {
    TextView name ,NIC,email,phoneNO ,gender;
    ImageView image;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_details);
        name=findViewById(R.id.griddata);
//        NIC=findViewById(R.id.griddata_NIC);
//        email=findViewById(R.id.griddata_email);
//        phoneNO=findViewById(R.id.griddata_phoneNO);
//        gender=findViewById(R.id.griddata_gender);
        image=findViewById(R.id.imageView);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        NIC.setText(intent.getStringExtra("NIC"));
        email.setText(intent.getStringExtra("email"));
        phoneNO.setText(intent.getStringExtra("phoneNO"));
        gender.setText(intent.getStringExtra("gender"));
        image.setImageResource(intent.getIntExtra("image",0));

    }
}
