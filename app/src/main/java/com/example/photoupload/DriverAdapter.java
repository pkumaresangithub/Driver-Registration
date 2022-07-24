package com.example.photoupload;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DriverAdapter {
    EditText mobileno,fname,mname,lname,address, state,city,pin,imageurl;
    public ImageView imageView;


    public DriverAdapter(View itemView)
    {
//        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.imageview);
//        fname = (TextView) itemView.findViewById(R.id.firstname);
//        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
//        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }
}
