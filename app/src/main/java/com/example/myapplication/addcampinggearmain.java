package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class addcampinggearmain extends AppCompatActivity {

    EditText txtlocation,txtmail,txtDate;
    Spinner spinnerlamps,spinnercups,spinnerhammer,spinnertables,spinnergas,spinnertentsize,spinnertentnumber;
    Button btnsave,gobackgearmain;

    String message;


    DatabaseReference databaseReference;
    ListView listViewGear;
    List<campingGear> gearList;
    DrawerLayout drawerLayout;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcampinggearmain);

        databaseReference = FirebaseDatabase.getInstance().getReference("gear");

        txtlocation = (EditText) findViewById(R.id.txtlocationName);
        txtmail = (EditText) findViewById(R.id.txtMail);
        txtDate = (EditText) findViewById(R.id.txtDate);

        spinnerlamps = (Spinner) findViewById(R.id.spinnerLamps);
        spinnercups = (Spinner) findViewById(R.id.spinnerCups);
        spinnerhammer = (Spinner) findViewById(R.id.spinnerHammer);
        spinnertables = (Spinner) findViewById(R.id.spinnerTables);
        spinnergas = (Spinner) findViewById(R.id.spinnerGas);
        spinnertentsize = (Spinner) findViewById(R.id.spinnertentSize);
        spinnertentnumber = (Spinner) findViewById(R.id.spinnertentNumber);
        drawerLayout = findViewById(R.id.drawerLayout);

        btnsave = (Button) findViewById(R.id.btnUpdate);
        gobackgearmain = (Button) findViewById((R.id.buttongeargoback));


        listViewGear = (ListView) findViewById(R.id.listViewGear);

        gearList = new ArrayList<>();




        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGear();


            }
        });

        gobackgearmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addcampinggearmain.this,MainActivityDil.class);
                startActivity(intent);
            }
        });
    }

    private void AddGear(){
        String location = txtlocation.getText().toString().trim();
        String Email = txtmail.getText().toString().trim();
        String Date = txtDate.getText().toString().trim();

        String lamp = spinnerlamps.getSelectedItem().toString();
        String  cups = spinnercups.getSelectedItem().toString();
        String hammer = spinnerhammer.getSelectedItem().toString();
        String tables = spinnertables.getSelectedItem().toString();
        String gas = spinnergas.getSelectedItem().toString();
        String tentSize = spinnertentsize.getSelectedItem().toString();
        String tentNumber = spinnertentnumber.getSelectedItem().toString();

        message = "Camping gear items for location "+ location+", We would like you send an invoice for the following items to this Email address   "+
                Email+",   lamps = "+spinnerlamps.getSelectedItem().toString()+
                ",    cups    &    plates   =   "+spinnercups.getSelectedItem().toString()+",    Mallet & Hammer    ="+spinnerhammer.getSelectedItem().toString()+
                ",    Tables =   "+spinnertables.getSelectedItem().toString()+",    Mobile gas Device =   "+spinnergas.getSelectedItem().toString()+
                ",    Tent Size =   "+spinnertentsize.getSelectedItem().toString()+",    How many tents =   "+spinnertentnumber.getSelectedItem().toString()+
                ". Thank You.";



        if(!TextUtils.isEmpty(location)){
            String id = databaseReference.push().getKey();
            campingGear cgear = new campingGear(id,location,Email,Date,lamp,cups,hammer,tables,gas,tentSize,tentNumber);

            databaseReference.child(id).setValue(cgear);
            Toast.makeText(this, "Camp Gear Added & select an email app to send the mail", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                    "mailto","email@email.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, txtlocation.getText().toString());
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        }else{
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show();
        }




    }
}