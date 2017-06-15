package com.example.sh.cravebit;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

/**
 * Created by Shabbir Hussain on 6/14/2017.
 */

public class DescriptionDialogue extends DialogFragment implements View.OnClickListener {

    View view;
    TextView txt;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.description_layout,null);
        String desc = getArguments().getString("desc");
        String uri = getArguments().getString("uri");

        txt = (TextView)view.findViewById(R.id.descText);
        imageView = (ImageView)view.findViewById(R.id.descImage);
        imageView.setOnClickListener(this);

        Picasso.with(view.getContext())
                .load(uri)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.a1)
                .fit()
                .into(imageView);
        txt.setText(desc);
        Typeface custom_font = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/product.ttf");
        txt.setTypeface(custom_font);


        return view;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
