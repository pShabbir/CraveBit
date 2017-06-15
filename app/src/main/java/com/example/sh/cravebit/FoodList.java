package com.example.sh.cravebit;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodList extends AppCompatActivity {

    private List<MyPost> arr;
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                MyPost myPost = arr.get(position);
                Toast.makeText(getApplicationContext(),myPost.getDescription(),Toast.LENGTH_LONG).show();

                FragmentManager fragmentManager = getFragmentManager();
                DescriptionDialogue descriptionDialogue = new DescriptionDialogue();
                Bundle f=new Bundle();
                f.putString("desc",myPost.getDescription());
                f.putString("uri",myPost.getPostImageUrl());
                descriptionDialogue.setArguments(f);
                descriptionDialogue.show(fragmentManager,"This is PopTime");
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    public void createPost(View view) {
        Intent i=new Intent(this,CreatePost.class);
        startActivity(i);
        finish();
    }
    private void collectPhoneNumbers(Map<String,Object> users) {


        arr=new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){



            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String author = (String) singleUser.get("author");
            String description = (String) singleUser.get("description");
            String postId = (String) singleUser.get("postId");
            String postImageUrl = (String) singleUser.get("postImageUrl");
            int price =  (int)(long) singleUser.get("price");
            int rating = (int)(long) singleUser.get("rating");
            String title = (String) singleUser.get("title");
            String userID = (String) singleUser.get("userID");

            arr.add(new MyPost(postId,author,price,postImageUrl,description,title,userID,rating));

        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//       / StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(arr);
        recyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }
}
