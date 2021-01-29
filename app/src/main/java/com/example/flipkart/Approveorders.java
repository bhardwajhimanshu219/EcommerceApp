package com.example.flipkart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.Model.products;
import com.example.flipkart.viewholder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Approveorders extends AppCompatActivity {
    private RecyclerView recylerview;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedproductref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approveorders);

        unverifiedproductref = FirebaseDatabase.getInstance().getReference().child("Products");



        recylerview = (RecyclerView) findViewById(R.id.orders_list_approve);
        recylerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recylerview.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(unverifiedproductref.orderByChild("productstate").equalTo("Not Approved"), products.class).build();

        FirebaseRecyclerAdapter<products, productviewholder> adapter = new FirebaseRecyclerAdapter<products, productviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull final products model) {

                holder.productname.setText(model.getName());
                holder.productdesc.setText(model.getDescription());
                holder.productprice.setText("price=" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String productid = model.getPid();
                        CharSequence options[] = new CharSequence[]
                                {
                                        "yes",
                                        "NO"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(Approveorders.this);
                        builder.setTitle("Do you want to approve this order ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {

                                if (position == 0) {
                                    changeproductstate(productid);

                                }
                                if (position == 1) {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

                @NonNull
                @Override
                public productviewholder onCreateViewHolder (@NonNull ViewGroup parent,int viewType)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                    productviewholder holder = new productviewholder(view);
                    return holder;
                }
            };

        recylerview.setAdapter(adapter);
        adapter.startListening();

    }



private void changeproductstate(String productid) {

        unverifiedproductref.child(productid).child("productstate").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(Approveorders.this,"That item has been approved ,and now the product is available for the sale",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
