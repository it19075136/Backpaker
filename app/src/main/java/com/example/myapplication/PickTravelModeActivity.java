package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PickTravelModeActivity extends AppCompatActivity {

//    Button btnEas,btnModerate,btnHard;
//Button easyAddLoca;
private FloatingActionButton easyAddLoca;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private EasyFragment easyFragment;
    private ModerateFragment moderateFragment;
    private HardFragment hardFragment;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_travel_mode);
//        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        easyFragment =new EasyFragment();
        moderateFragment = new ModerateFragment();
        hardFragment = new HardFragment();

        tabLayout.setupWithViewPager(viewPager);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(easyFragment,"Easy");
        viewPagerAdapter.addFragment(moderateFragment,"Moderate");
        viewPagerAdapter.addFragment(hardFragment,"Hard");

        viewPager.setAdapter(viewPagerAdapter);

//        tabLayout.getTabAt(0).setIcon(R.drawable.common_google_signin_btn_icon_dark);
//        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(12);

//          easyAddLoca = findViewById(R.id.container_layout);

//        btnModerate = findViewById(R.id.btnModerate);
//        btnHard = findViewById(R.id.btnHard);
//
//        easyAddLoca = findViewById(R.id.easy_floBtn);
//        easyAddLoca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dbRef = FirebaseDatabase.getInstance().getReference().child("locations/");
////                if (TextUtils.isEmpty(txtName.getText().toString())){
////                    Toast.makeText(getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
////                }
//                Intent intent = new Intent(PickTravelModeActivity.this,AddTravelLocationActivity.class);
//                startActivity(intent);
//            }
//        });
//        easyAddLoca.setOnClickListener(new View.OnClickListener() {
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


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment>  fragments = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();



        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmentsTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
    }
}