package com.example.sh.cravebit;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatePost extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 100;

    EditText title,dec,price;
    RatingBar ratingBar;
    Bitmap selectedImage;
    ImageView imageView;
    ProgressBar progressBar;
    private FirebaseStorage mStorage;
    private FirebaseDatabase database;
    String postId,userID,author,postImageUrl,description,stitle;
    int sprice;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mStorage = FirebaseStorage.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        database = FirebaseDatabase.getInstance();
        CheckUserPermsions();

        if(flag){
            Toast.makeText(this,"Try again some internal failure",Toast.LENGTH_LONG).show();
            finish();
        }

        progressBar.setVisibility(View.GONE);

        title = (EditText)findViewById(R.id.title);
        dec  = (EditText)findViewById(R.id.description);
        price = (EditText)findViewById(R.id.price);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

       imageView.setVisibility(View.VISIBLE);
       title.setVisibility(View.VISIBLE);
       dec.setVisibility(View.VISIBLE);
       price.setVisibility(View.VISIBLE);
       ratingBar.setVisibility(View.VISIBLE);




    }

    public void submit(View view){
        stitle = title.getText().toString();
        description = dec.getText().toString();
        sprice = Integer.parseInt(price.getText().toString());
        int rating = ratingBar.getNumStars();

        MyPost  myPost = new MyPost(postId,author,sprice,postImageUrl,description,stitle,userID,rating);
        DatabaseReference databaseReference = database.getReference("posts").child(postId);
        databaseReference.setValue(myPost);

    }

    public void pickImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    //Checking user permission
    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        pickImage();// init the contact list

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,"your message" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    // End here

    public void uploadImage(Uri uri){
        StorageReference storageRef = mStorage.getReference();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        author = sharedPreferences.getString("name","unkonwn");
        userID = sharedPreferences.getString("uid","qwertyuiop123456");
        postId= database.getReference("users").push().getKey();

        StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               // downloadUrl = taskSnapshot.getDownloadUrl();
                postImageUrl = taskSnapshot.getDownloadUrl().toString();
//                myFlag = true;
                Toast.makeText(getApplicationContext(),"This is done"+postImageUrl,Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"This has failed",Toast.LENGTH_LONG).show();
                flag = false;
            }
        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                 final Uri imageUri = data.getData();
                 final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                 selectedImage = BitmapFactory.decodeStream(imageStream);
                 imageView=(ImageView)findViewById(R.id.postImage);
                 imageView.setImageBitmap(selectedImage);

                 //Call for uploading image on FireBase
                 uploadImage(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(CreatePost.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(CreatePost.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}

class MyPost {
    public String postId;
    public String author;
    public int    price;
    public String postImageUrl;
    public String description;
    public String title;
    public String userID;
    public int rating;

    public MyPost(String postId, String author, int price, String postImageUrl, String description, String title, String userID, int rating) {
        this.postId = postId;
        this.author = author;
        this.price = price;
        this.postImageUrl = postImageUrl;
        this.description = description;
        this.title = title;
        this.userID = userID;
        this.rating = rating;
    }
}
