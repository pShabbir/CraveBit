package com.example.sh.cravebit;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Shabbir Hussain on 4/21/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>  {

    private List<MyPost> postList;
    int i;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,readmore,price,rate;
        public RatingBar ratingBar;
        public ImageView imageView;


        public MyViewHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.title);
//            readmore = (TextView)view.findViewById(R.id.description);
//            ratingBar = (RatingBar)view.findViewById(R.id.rate);
            imageView = (ImageView)view.findViewById(R.id.image);
            price = (TextView)view.findViewById(R.id.price);
            rate = (TextView)view.findViewById(R.id.rate);

            int col[] = {getColor("#fff000"),getColor("#3f51b5"),getColor("#3f51b5"),getColor("#42a5f5"),getColor("#cddc39"),getColor("#8bc34a"),
                    getColor("#ffa726"),getColor("#ff5722"),getColor("#ffab00"),getColor("#9e9d24"),getColor("#388e3c"),getColor("#00695c"),
                    getColor("#ff6e40"),getColor("#ef5350"),getColor("#f48fb1"),getColor("#9c27b0"),getColor("#b39ddb"),getColor("#ff4081"),
                    getColor("#69f0ae"),getColor("#37474f"),getColor("#ffca28"),getColor("#880e4f"),getColor("#aa00ff"),getColor("#90caf9"),
                    getColor("#ffab91")
            };
            Random rand = new Random();
            int  n = rand.nextInt(col.length) + 1;
            CardView cardView=(CardView)view.findViewById(R.id.card_view);
            cardView.setCardBackgroundColor(col[n-1]);

            TextView cc=(TextView)view.findViewById(R.id.cc);
            TextView cc2=(TextView)view.findViewById(R.id.cc2);

            Typeface custom_font = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/pacifico.ttf");
            title.setTypeface(custom_font);
//            readmore.setTypeface(custom_font);
            price.setTypeface(custom_font);
            rate.setTypeface(custom_font);
            custom_font = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/product.ttf");
            cc.setTypeface(custom_font);
            cc.setTypeface(custom_font);
//
//            bt1 = (Button)view.findViewById(R.id.likes);
           // imagelikes=(ImageView)view.findViewById(R.id.likes);
        }
    }
    public int getColor(String s){
        return Color.parseColor(s);
    }


    public PostAdapter(List<MyPost> post){
        postList = post;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_post,parent,false);



        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        i=position;
        MyPost post = postList.get(position);
        holder.title.setText(post.getTitle());
//        holder.readmore.setText(post.getDescription());
        holder.price.setText(String.valueOf(post.getPrice()));
//        holder.ratingBar.setNumStars(post.getRating());
        holder.rate.setText(String.valueOf(post.getRating()));

//        GlideApp.with(holder.imageView.getContext())
//                .load(post.getPostImageUrl())
//                .centerCrop()
//                .placeholder(R.mipmap.placeholder)
//                .into(holder.imageView);


        Picasso.with(holder.imageView.getContext())
                .load(post.getPostImageUrl())
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.a1)
                .fit()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }




}
