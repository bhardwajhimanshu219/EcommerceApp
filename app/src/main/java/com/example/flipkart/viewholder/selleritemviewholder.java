package com.example.flipkart.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.Interface.Itemclicklistener;
import com.example.flipkart.R;

public class selleritemviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView productname,productdesc,productprice,txtproductstatus;
        public ImageView imageView;
        public Itemclicklistener listener;

        public selleritemviewholder(@NonNull View itemView)
        {
            super(itemView);
            imageView =(ImageView) itemView.findViewById(R.id.seller_product_image);
            productname =(TextView) itemView.findViewById(R.id.seller_product_name);
            productdesc =(TextView) itemView.findViewById(R.id.seller_product_desc);
            productprice =(TextView) itemView.findViewById(R.id.seller_product_price);
            txtproductstatus =(TextView) itemView.findViewById(R.id.seller_product_status);

        }
        public void setItemclickListener(Itemclicklistener listener)
        {
            this.listener=listener;
        }
        @Override
        public void onClick(View v) {

            listener.onClick(v,getAdapterPosition(),false);
        }
    }


