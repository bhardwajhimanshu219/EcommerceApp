package com.example.flipkart.viewholder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.Interface.Itemclicklistener;
import com.example.flipkart.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Cartviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtprductname,txtproductprice,txtproductquantity;
    private Itemclicklistener itemclicklistener;

    public Cartviewholder(@NonNull View itemView)
    {
        super(itemView);

        txtprductname = itemView.findViewById(R.id.product_name_layout);
        txtproductprice = itemView.findViewById(R.id.product_price_layout);
        txtproductquantity= itemView.findViewById(R.id.product_quantity);

    }

    @Override
    public void onClick(View view)
    {
        itemclicklistener.onClick(view , getAdapterPosition() , false);

    }
    public void setItemclicklistener(Itemclicklistener itemclicklistener)
    {
        this.itemclicklistener=itemclicklistener;
    }
}
