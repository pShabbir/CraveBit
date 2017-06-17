package com.example.sh.cravebit;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class BottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        final RelativeLayout r=(RelativeLayout)findViewById(R.id.v);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.home:
                               // selectedFragment = FoodList.newInstance();
                                //r.setBackground(getResources().getDrawable(R.mipmap.storybg));
                                break;
//                            case R.id.create:
//                                selectedFragment = CreatePost.newInstance();
//                                r.setBackground(getResources().getDrawable(R.mipmap.createbg));

                                break;
//                            case R.id.profile:
//                                selectedFragment = ProfilePage.newInstance();
//                                r.setBackground(getResources().getDrawable(R.mipmap.profilebg));
//                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

       // transaction.replace(R.id.frame_layout, StoryPage.newInstance());
        transaction.commit();


    }
}
