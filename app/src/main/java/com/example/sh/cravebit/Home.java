package com.example.sh.cravebit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf");

        TextView txt = (TextView)findViewById(R.id.upperText);
        txt.setTypeface(typeface);

        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(50);
        rotatingTextWrapper.setTypeface(typeface2);


        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Click Here", "To Share","Your Food");
        rotatable.setSize(50);
        rotatable.setAnimationDuration(500);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new BounceInterpolator());

        rotatingTextWrapper.setContent("?", rotatable);



        TextView tx2t = (TextView)findViewById(R.id.lowerText);
        tx2t.setTypeface(typeface);

         rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher2);
        rotatingTextWrapper.setSize(50);
        rotatingTextWrapper.setTypeface(typeface2);


         rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Click Here", "To Share","Your Joint");
        rotatable.setSize(50);
        rotatable.setAnimationDuration(500);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new BounceInterpolator());

        rotatingTextWrapper.setContent("?", rotatable);
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void placeList(View view) {
        Intent i=new Intent(this,PlaceList.class);
        startActivity(i);
        finish();
    }

    public void foodList(View view) {
        Intent i=new Intent(this,FoodList.class);
        startActivity(i);
        finish();
    }
}
