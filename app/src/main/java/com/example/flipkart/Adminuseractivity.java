package com.example.flipkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flipkart.Model.cart;
import com.example.flipkart.viewholder.Cartviewholder;
import com.example.flipkart.viewholder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Adminuseractivity extends AppCompatActivity {
    private RecyclerView productlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productref;
    private String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminuseractivity);
        userid = getIntent().getStringExtra("uid");

         productlist = (RecyclerView) findViewById(R.id.user_orders_list_final);

        productlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productlist.setLayoutManager(layoutManager);

        productref = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userid).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<cart> options =
                new FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(productref,cart.class)
                .build();
        FirebaseRecyclerAdapter<cart, Cartviewholder> adapter = new FirebaseRecyclerAdapter<cart, Cartviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cartviewholder holder, int position, @NonNull cart model) {
                holder.txtproductquantity.setText("Quantity = " +model.getQuantity());
                holder.txtprductname.setText("NAME = "+model.getPname());
                holder.txtproductprice.setText("PRICE = "+model.getPrice()+ "rupees");

            }

            @NonNull
            @Override
            public Cartviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent,false);
                Cartviewholder holder = new Cartviewholder(view);
                return  holder;
            }
        };
        productlist.setAdapter(adapter);
        adapter.startListening();
    }
    }
