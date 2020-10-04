package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class GuideForm extends AppCompatActivity {

    Button AddGuides;
    EditText txtGuideName,txtGuideNic,txtGuideEmail,txtGuidePhone,txtGuideGrnder;
    DatabaseReference dbRef;
    Guide guide;
//    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_form);

        txtGuideName = findViewById(R.id.text_input_name);
        txtGuideNic = findViewById(R.id.text_input_nic);
        txtGuideEmail = findViewById(R.id.text_input_email);
//        txtGuideGrnder = findViewById(R.id.text_input_radio);
        txtGuidePhone = findViewById(R.id.text_input_phone);

        AddGuides = findViewById(R.id.btnaddguides);
        AddGuides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Guide");
                if (TextUtils.isEmpty(txtGuideName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(txtGuideNic.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty NIC", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtGuideEmail.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Email", Toast.LENGTH_SHORT).show();
                }
//                else if (TextUtils.isEmpty(txtGuideGrnder.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "Empty  gender", Toast.LENGTH_SHORT).show();
//                }
                else{
                    guide.setName(txtGuideName.getText().toString().trim());
                    guide.setEmail(txtGuideEmail.getText().toString().trim());
                    guide.setNIC(txtGuideNic.getText().toString().trim());
//                    guide.setGender(txtGuideGrnder.getText().toString().trim());
//                    guide.setPhoneNO(Integer.parseInt(txtGuidePhone.getText().toString().trim()));
                    dbRef.child(UUID.randomUUID().toString()).setValue(guide);
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
        txtGuideName.setText("");
        txtGuideEmail.setText("");
        txtGuideNic.setText("");
        txtGuideGrnder.setText("");
        txtGuidePhone.setText("");
    }

}