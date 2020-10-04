package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class PermissionRequestActivity extends AppCompatActivity {

    EditText txtName,txtNic,txtEmail,txtPhone;
    Button btnSend;
    DatabaseReference dbRef;
    PermissionRequest pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        txtName=findViewById(R.id.editName);
        txtNic =findViewById(R.id.editNIC);
        txtEmail=findViewById(R.id.editEmail);
        txtPhone=findViewById(R.id.editPhoNum);
        btnSend=findViewById(R.id.btnPermissionSend);

        pr=new PermissionRequest();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Permission");
                if (TextUtils.isEmpty(txtName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(txtNic.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty NIC", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtEmail.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txtPhone.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Empty Phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    pr.setName(txtName.getText().toString().trim());
                    pr.setEmail(txtEmail.getText().toString().trim());
                    pr.setNIC(txtNic.getText().toString().trim());
                    pr.setPhoneNum(Integer.parseInt(txtPhone.getText().toString().trim()));
                    dbRef.child(UUID.randomUUID().toString()).setValue(pr);
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
        txtName.setText("");
        txtEmail.setText("");
        txtNic.setText("");
        txtPhone.setText("");
    }
}