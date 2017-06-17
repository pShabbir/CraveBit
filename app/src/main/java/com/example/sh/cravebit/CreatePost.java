package com.example.sh.cravebit;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatePost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText title,dec,price;
    RatingBar ratingBar;
    Bitmap selectedImage;
    ImageView imageView;
    ProgressBar progressBar;
    private FirebaseStorage mStorage;
    private FirebaseDatabase database;
    String postId,userID,author,postImageUrl,description,stitle;
    int sprice;
    float myrating=0;
    boolean flag;
    Uri imageUri;

    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mStorage = FirebaseStorage.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        database = FirebaseDatabase.getInstance();
        imageView=(ImageView)findViewById(R.id.postImage);
        title = (EditText)findViewById(R.id.title);
        dec  = (EditText)findViewById(R.id.description);
        price = (EditText)findViewById(R.id.price);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        submit = (Button)findViewById(R.id.submit);

       // pickImage();

        progressBar.setVisibility(View.GONE);


       imageView.setVisibility(View.VISIBLE);
       title.setVisibility(View.VISIBLE);
       dec.setVisibility(View.VISIBLE);
       price.setVisibility(View.VISIBLE);
       ratingBar.setVisibility(View.VISIBLE);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating =  rating;
            }
        });




    }

    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
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

    public void submit(View view){
        stitle = title.getText().toString();
        description = dec.getText().toString();
        sprice = Integer.parseInt(price.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        new Demo().execute();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public void pick(View view){
        CheckUserPermsions();

    }


    public void pickImage(){
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    class Demo extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //
//            InputStream imageStream = null;
//            try {
//                imageStream = getContentResolver().openInputStream(imageUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            selectedImage = BitmapFactory.decodeStream(imageStream);
            //
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            Log.d("Shabbir",bitmap.getHeight()+"");
            byte[] data = baos.toByteArray();

            StorageReference storageRef = mStorage.getReference();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            author = sharedPreferences.getString("name","unkonwn");
            userID = sharedPreferences.getString("uid","qwertyuiop123456");
            postId= database.getReference("users").push().getKey();


            StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

            UploadTask uploadTask = riversRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    postImageUrl =""+ taskSnapshot.getDownloadUrl();
                    MyPost  myPost = new MyPost(postId,author,sprice,postImageUrl,description,stitle,userID,myrating);
                    DatabaseReference databaseReference = database.getReference("posts").child(postId);
                    databaseReference.setValue(myPost);
                    onBackPressed();

                }
            });
            return null;
        }
    }

    class ImageLoading extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            selectedImage = BitmapFactory.decodeStream(imageStream);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageView.setImageBitmap(selectedImage);

        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            imageUri = data.getData();
           // setPic();
//             InputStream imageStream = null;
//            try {
//                imageStream = getContentResolver().openInputStream(imageUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            selectedImage = BitmapFactory.decodeStream(imageStream);
                // imageView.setImageBitmap(selectedImage);

//            Picasso.with(this)
//                    .load(imageUri)
//                    .placeholder(R.mipmap.placeholder)
//                    .error(R.mipmap.a1)
//                    .fit()
//                    .into(imageView);

            new ImageLoading().execute();

//            Log.d("Shabbir",""+selectedImage.getByteCount());
//
//            Toast.makeText(CreatePost.this, ""+selectedImage.getHeight(), Toast.LENGTH_LONG).show();

            // Toast.makeText(CreatePost.this, ""+selectedImage.getByteCount(), Toast.LENGTH_LONG).show();

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
    public Object rating;

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getAuthor() {

        return author;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Object getRating() {
        return rating;
    }



    public MyPost(String postId, String author, int price, String postImageUrl, String description, String title, String userID, Object rating) {
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
