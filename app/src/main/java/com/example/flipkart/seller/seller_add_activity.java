package com.example.flipkart.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.R;

public class seller_add_activity extends AppCompatActivity {
    private ImageView tshirt,sportstshirt,femaledress,sweaters;
    private ImageView sunglasses,purses,hats,shoes;
    private ImageView headphones,laptops,watches,phones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_add_activity);

        tshirt=(ImageView) findViewById(R.id.t_shirts);
        sportstshirt=(ImageView) findViewById(R.id.t_sports);
        femaledress=(ImageView) findViewById(R.id.t_female);
        sweaters=(ImageView) findViewById(R.id.t_sweater);
        sunglasses=(ImageView) findViewById(R.id.t_glasses);
        purses=(ImageView) findViewById(R.id.t_bags);
        hats=(ImageView) findViewById(R.id.t_hats);
        shoes=(ImageView) findViewById(R.id.t_shoes);
        headphones=(ImageView) findViewById(R.id.t_headphones);
        laptops=(ImageView) findViewById(R.id.t_laptops);
        watches=(ImageView) findViewById(R.id.t_watches);
        phones=(ImageView) findViewById(R.id.t_mobiles);
        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
               intent.putExtra("category","tshirts");
               startActivity(intent);
            }
        });
        sportstshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","sportstshirts");
                startActivity(intent);
            }
        });
        femaledress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","femaledress");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","sweaters");
                startActivity(intent);
            }
        });
        sunglasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);
            }
        });
        purses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","bags");
                startActivity(intent);
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller_add_activity.this, Seller_add_category.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });


    }
}
