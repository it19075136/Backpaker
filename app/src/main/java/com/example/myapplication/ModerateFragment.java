package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public  class  ModerateFragment extends Fragment {
    private FloatingActionButton moderateAddLoca;
    View view;
    GridView gridView;
    DatabaseReference dbRef;
    Location location;
    List<String> locationNames;
//    String[] loc = {
//            "Kandy",
//            "Colombo"
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view  =  inflater.inflate(R.layout.fragment_moderate, container, false);

        locationNames = new ArrayList<>();
        gridView = view.findViewById(R.id.moderatelocview);

        location = new Location();

        Query query = FirebaseDatabase.getInstance().getReference("location").orderByChild("type").equalTo("moderate");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    String locationName = childSnap.child("locationName").getValue(String.class);
                    locationNames.add(locationName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),LocationDetail.class);
                intent.putExtra("locName", locationNames.get(i));
                startActivity(intent);
            }
        });
//        easyAddLoca = view.findViewById(R.id.easy_floBtn);
//        easyAddLoca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dbRef = FirebaseDatabase.getInstance().getReference().child("locations/");
////                if (TextUtils.isEmpty(txtName.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
////                }
////                Intent intent = new Intent();
//                Intent intent= new Intent(getActivity(),AddTravelLocationActivity.class);
//                startActivity(intent);
//            }

//        location.setLocationName("LocationName");
//        location.setRoute("route");
//        location.setNote("note");
//        location.setAccommondation("accommondation");
//        location.setNOofDays("NOofDays");
//        location.setRoadCondition("roadCondition");
//        location.setWeather("Weather");
//        location.setPermission("permission");
//        });
//        clickLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),LocationDetail.class);
////                intent.putExtra("locationName",location.getLocationName());
//                //Is i want to put other attribties her also
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return locationNames.size();
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
            TextView name= view1.findViewById(R.id.name);
            ImageView image =view1.findViewById(R.id.image);
            name.setText(locationNames.get(i));
//            image.setImageResource(locations.get(i).);
            return view1;
        }
    }
}


