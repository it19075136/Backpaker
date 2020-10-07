package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView htlName,roomCou,cont,mail,des,loc;
    CheckBox acRoom,nonACRoom;
    Button btnhtlAdd,btnView,btnDel,btnUpd;
    DatabaseReference dbRef;
    ImageView htlImage;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;
    Hotel htl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        htlName = findViewById(R.id.htlName);
        roomCou = findViewById(R.id.roomCount);
        cont = findViewById(R.id.conNum);
        mail = findViewById(R.id.email);
        des = findViewById(R.id.htlDisc);
        acRoom = findViewById(R.id.rmAC);
        nonACRoom = findViewById(R.id.rmNonAC);
        loc = findViewById(R.id.loc);
        htlImage = findViewById(R.id.htlImg);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnhtlAdd = findViewById(R.id.addHotelbtn);
        btnView = findViewById(R.id.viewhtlbtn);
        btnUpd = findViewById(R.id.btnhtlUp);
        btnDel = findViewById(R.id.btnhtlDel);


        htl = new Hotel();
        Query key  = FirebaseDatabase.getInstance().getReference().child("Hotel").orderByKey().limitToLast(1);
        final String[] latestKey = new String[1];
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

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id = htl.getId().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child("Hotel/".concat(id));
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            htlName.setText(dataSnapshot.child("htlName").getValue().toString());
                            cont.setText(dataSnapshot.child("phone").getValue().toString());
                            mail.setText(dataSnapshot.child("email").getValue().toString());
                            roomCou.setText(dataSnapshot.child("roomCount").getValue().toString());
                            loc.setText(dataSnapshot.child("loca").getValue().toString());
                            des.setText(dataSnapshot.child("des").getValue().toString());

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

        btnhtlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Hotel");
                    if (TextUtils.isEmpty(htlName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Hotel Name", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(roomCou.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Room Count", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(cont.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Contact Number", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(mail.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Email Address", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(loc.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Location", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(des.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty Discription", Toast.LENGTH_SHORT).show();
                    else {

                        htl.setHtlName(htlName.getText().toString().trim());
                        htl.setRoomCount(Integer.parseInt(roomCou.getText().toString().trim()));
                        htl.setPhone(Integer.parseInt(cont.getText().toString().trim()));
                        htl.setEmail(mail.getText().toString().trim());
                        htl.setLoca(loc.getText().toString().trim());
                        if (acRoom.isChecked()) {
                            htl.setRoomtype("AC Room");
                        } else if (nonACRoom.isChecked()) {
                            htl.setRoomtype("Non-A/C Room");
                        }
                        else{
                            htl.setRoomtype("Both AC and NON-AC");
                        }
                        htl.setDes(des.getText().toString().trim());
                        htl.setId(GetHotelId(latestKey[0]));
                        dbRef.child(Integer.toString(GetHotelId(latestKey[0])).trim()).setValue(htl);
                        //dbRef.child("htl1").setValue(htl);
                        Toast.makeText(getApplicationContext(), "Welcome to Backpacker Hotel Group", Toast.LENGTH_SHORT).show();
                        clearControl();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Number Format Error", Toast.LENGTH_SHORT).show();
                }
                Query key  = FirebaseDatabase.getInstance().getReference().child("Hotel").orderByKey().limitToLast(1);
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

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Hotel/".concat(htl.getId().toString()));
                dbRef.removeValue();
                Toast.makeText(getApplicationContext(), "Hotel Canceled... ", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Hotel/".concat(htl.getId().toString()));
                dbRef.child("htlName").setValue(htlName.getText().toString().trim());
                dbRef.child("phone").setValue(cont.getText().toString().trim());
                dbRef.child("email").setValue(mail.getText().toString().trim());
                dbRef.child("roomCount").setValue(roomCou.getText().toString().trim());
                dbRef.child("loca").setValue(loc.getText().toString().trim());
                dbRef.child("des").setValue(des.getText().toString().trim());
                if (acRoom.isChecked()) {
                    htl.setRoomtype("AC Room");
                } else if (nonACRoom.isChecked()) {
                    htl.setRoomtype("Non-A/C Room");
                }
                else{
                    htl.setRoomtype("Both AC and NON-AC");
                }
                dbRef.child("roomType").setValue(acRoom.getText().toString());
                Toast.makeText(getApplicationContext(), "Hotel Updated...", Toast.LENGTH_SHORT).show();
                clearControl();
            }
        });


        htlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        }

        private int GetHotelId(String lkey){
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

    private void clearControl(){
        htlName.setText("");
        roomCou.setText("");
        cont.setText("");
        mail.setText("");
        loc.setText("");
        acRoom.setChecked(false);
        nonACRoom.setChecked(false);
        des.setText("");
    }

    private void choosePicture() {
        Intent intent =   new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            htlImage.setImageURI(imageUri);
            uploadpicture();
        }
    }

    private void uploadpicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Fail to upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double ProgressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Presentage: " + (int) ProgressPercent + "%");
                    }
                });
        }

    }
