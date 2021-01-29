package com.example.flipkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.flipkart.Model.products;
import com.example.flipkart.viewholder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Search__activity extends AppCompatActivity {
    private Button searchbtn;
    private EditText inputtext;
    private RecyclerView searchlist;
    private String searchinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__activity);
        inputtext=(EditText)findViewById(R.id.search_product);
        searchbtn=(Button)findViewById(R.id.search_btn);
        searchlist=(RecyclerView)findViewById(R.id.search_list);
        searchlist.setLayoutManager(new LinearLayoutManager(Search__activity.this));
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchinput = inputtext.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                .setQuery(reference.orderByChild("name").startAt(searchinput),products.class)
                .build();
        FirebaseRecyclerAdapter<products, productviewholder> adapter =
                new FirebaseRecyclerAdapter<products, productviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull final products model) {
                        holder.productname.setText(model.getName());
                        holder.productdesc.setText(model.getDescription());
                        holder.productprice.setText("price=" +model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(Search__activity.this,product_details.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
                        productviewholder holder=new productviewholder(view);
                        return holder;
                    }
                };
        searchlist.setAdapter(adapter);

    }
}
