package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityDil extends AppCompatActivity {

    EditText txtlocation,txtmail,txtDate;
    Spinner spinnerlamps,spinnercups,spinnerhammer,spinnertables,spinnergas,spinnertentsize,spinnertentnumber;
    Button btnsave,addcampinggear;

    String message;


    DatabaseReference databaseReference;
    ListView listViewGear;
    List<campingGear> gearList;
    DrawerLayout drawerLayout;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindil);

//        btnLogOut = findViewById(R.id.buttonlogout);
//
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivityDil.this,logIn.class);
//                startActivity(intent);
//            }
//        });

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
        addcampinggear = (Button) findViewById(R.id.buttonaddcampinggear);


        listViewGear = (ListView) findViewById(R.id.listViewGear);

            gearList = new ArrayList<>();




//        btnsave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    AddGear();
//
//            }
//        });

        listViewGear.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               campingGear cgear = gearList.get(i);

               showUpdateGear(cgear.getCampId(),cgear.getLocationName(),cgear.getEmail(),cgear.getLamps(),cgear.getCupsPlates(),cgear.getMalletHammer(),cgear.getTables(),cgear.getGas(),cgear.getDate(),cgear.getTentSize(),cgear.getTentNumber());
                return true;
            }
        });

        addcampinggear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityDil.this,addcampinggearmain.class);
                startActivity(intent);
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                gearList.clear();
                for(DataSnapshot gearSnapshot: dataSnapshot.getChildren()){
                        campingGear gear = gearSnapshot.getValue(campingGear.class);
                        gearList.add(gear);
                }

                gearList adapter = new gearList(MainActivityDil.this,gearList);
                listViewGear.setAdapter(adapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateGear(final String campid, String location, String Email, String Date, String lamp, String cups, String hammer, String tables, String gas, String tentSize, String tentNumber)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater =  getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextlocation = (EditText) dialogView.findViewById(R.id.uptxtlocationName);
        final EditText editTextemail = (EditText) dialogView.findViewById(R.id.uptxtMail);
        final Spinner spinnerTextlamps = (Spinner) dialogView.findViewById(R.id.upspinnerLamps);
        final Spinner spinnercups = (Spinner) dialogView.findViewById(R.id.upspinnerCups);
        final Spinner spinnerhammer = (Spinner) dialogView.findViewById(R.id.upspinnerHammer);
        final Spinner spinnertables = (Spinner) dialogView.findViewById(R.id.upspinnerTables);
        final Spinner spinnergas = (Spinner) dialogView.findViewById(R.id.upspinnerGas);
        final EditText editTextdate = (EditText) dialogView.findViewById(R.id.uptxtDate);
        final Spinner spinnertentsize = (Spinner) dialogView.findViewById(R.id.upspinnertentSize);
        final Spinner spinnertentnumber = (Spinner) dialogView.findViewById(R.id.upspinnertentNumber);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Updating Location : " + location);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = editTextlocation.getText().toString().trim();
                String email = editTextemail.getText().toString().trim();
                String date = editTextdate.getText().toString().trim();
                String lamps = spinnerTextlamps.getSelectedItem().toString();
                String cups = spinnercups.getSelectedItem().toString();
                String hammer = spinnerhammer.getSelectedItem().toString();
                String tables = spinnertables.getSelectedItem().toString();
                String gas = spinnergas.getSelectedItem().toString();
                String tentsize = spinnertentsize.getSelectedItem().toString();
                String tentnumber = spinnertentnumber.getSelectedItem().toString();

                if (!TextUtils.isEmpty(location)) {
                    updateGear(campid, location, email, date, lamps, cups, hammer, tables, gas, tentsize, tentnumber);

                    alertDialog.dismiss();

                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    deleteGear(campid);
                alertDialog.dismiss();



            }
        });

    }

    private void deleteGear(String campid)
    {
        DatabaseReference drGear = FirebaseDatabase.getInstance().getReference("gear").child(campid);
        drGear.removeValue();

        Toast.makeText(this,"Camping gear is deleted",Toast.LENGTH_LONG).show();

    }

    private boolean updateGear(String id,String location,String Email,String Date,String lamp,String cups,String hammer,String tables,String gas,String tentSize,String tentNumber)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("gear").child(id);

        campingGear gears = new campingGear(id,location,Email,Date,lamp,cups,hammer,tables,gas,tentSize,tentNumber);

        databaseReference.setValue(gears);
        Toast.makeText(this, "Camping Gear Updated Successfully", Toast.LENGTH_LONG).show();

        return true;
    }

//    private void AddGear(){
//        String location = txtlocation.getText().toString().trim();
//        String Email = txtmail.getText().toString().trim();
//        String Date = txtDate.getText().toString().trim();
//
//        String lamp = spinnerlamps.getSelectedItem().toString();
//        String  cups = spinnercups.getSelectedItem().toString();
//        String hammer = spinnerhammer.getSelectedItem().toString();
//        String tables = spinnertables.getSelectedItem().toString();
//        String gas = spinnergas.getSelectedItem().toString();
//        String tentSize = spinnertentsize.getSelectedItem().toString();
//        String tentNumber = spinnertentnumber.getSelectedItem().toString();
//
//        message = "Camping gear items for location "+ location+", We would like you send an invoice for the following items to this Email address   "+
//                Email+",   lamps = "+spinnerlamps.getSelectedItem().toString()+
//                ",    cups    &    plates   =   "+spinnercups.getSelectedItem().toString()+",    Mallet & Hammer    ="+spinnerhammer.getSelectedItem().toString()+
//                ",    Tables =   "+spinnertables.getSelectedItem().toString()+",    Mobile gas Device =   "+spinnergas.getSelectedItem().toString()+
//                ",    Tent Size =   "+spinnertentsize.getSelectedItem().toString()+",    How many tents =   "+spinnertentnumber.getSelectedItem().toString()+
//                ". Thank You.";
//
//
//
//        if(!TextUtils.isEmpty(location)){
//            String id = databaseReference.push().getKey();
//            campingGear cgear = new campingGear(id,location,Email,Date,lamp,cups,hammer,tables,gas,tentSize,tentNumber);
//
//            databaseReference.child(id).setValue(cgear);
//            Toast.makeText(this, "Camp Gear Added & select an email app to send the mail", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
//                    "mailto","email@email.com", null));
//            intent.putExtra(Intent.EXTRA_SUBJECT, txtlocation.getText().toString());
//            intent.putExtra(Intent.EXTRA_TEXT, message);
//            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
//
//        }else{
//            Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show();
//        }
//
//
//
//
//    }

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
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(activity);
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