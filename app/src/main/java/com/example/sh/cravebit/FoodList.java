package com.example.sh.cravebit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FoodList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
    }

    public void createPost(View view) {
        Intent i=new Intent(this,CreatePost.class);
        startActivity(i);
    }
}
