package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddTravelLocationActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    Button btnAddLoc;
    DatabaseReference dbRef;
    String locNam;
    EditText txtLocName;
    EditText txtLocType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__travel_location);
        textInputLayout = findViewById(R.id.text_input_layout);

        btnAddLoc= findViewById(R.id.buttonLocADD);
        txtLocName = findViewById(R.id.txtnLocName);
        txtLocType = findViewById(R.id.txtnLocType);
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
                String  ref = "location/"+txtLocName.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child(ref);




                if (TextUtils.isEmpty(txtLocName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(txtLocType.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty type", Toast.LENGTH_SHORT).show();
                }
//                else if (TextUtils.isEmpty(txtEmail.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "Empty Email", Toast.LENGTH_SHORT).show();
//                }else if (TextUtils.isEmpty(txtPhone.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "Empty Phone number", Toast.LENGTH_SHORT).show();
//                }
                else{
                    dbRef.child("locationName").setValue(txtLocName.getText().toString().trim());
                    dbRef.child("type").setValue(txtLocType.getText().toString().trim());
//                    Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(view,"Successfully inserted",Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1000);
                    snackbar.show();
                    clearControls();
                }

            }
        });

    }
    private void clearControls(){
        txtLocName.setText("");
        txtLocType.setText("");
    }
}
