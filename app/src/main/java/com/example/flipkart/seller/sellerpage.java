package com.example.flipkart.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.MainActivity;
import com.example.flipkart.Model.products;
import com.example.flipkart.R;
import com.example.flipkart.viewholder.selleritemviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class sellerpage extends AppCompatActivity {
    private TextView mTextmessage;
    private RecyclerView recylerview;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedproductref;


    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
               case R.id.navigation_home:

                    return true;


                case R.id.navigation_add:
                    Intent intentcate = new Intent(sellerpage.this, seller_add_activity.class);

                    startActivity(intentcate);


                    return true ;


                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(sellerpage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
        }
        return  false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerpage);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextmessage = (TextView) findViewById(R.id.seller_text);
        navView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        unverifiedproductref = FirebaseDatabase.getInstance().getReference().child("Products");

        recylerview = (RecyclerView) findViewById(R.id.orders_list_seller);
        recylerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recylerview.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(unverifiedproductref.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), products.class).build();

        FirebaseRecyclerAdapter<products, selleritemviewholder> adapter = new FirebaseRecyclerAdapter<products, selleritemviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull selleritemviewholder holder, int position, @NonNull final products model) {

                holder.productname.setText(model.getName());
                holder.productdesc.setText(model.getDescription());
                holder.productprice.setText("price=" + model.getPrice());
                holder.txtproductstatus.setText("price=" + model.getProductstate());
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(sellerpage.this);
                        builder.setTitle("Do you want to delete this order ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {

                                if (position == 0) {

                                    deletesellerproduct(productid);

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
            public selleritemviewholder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_layout, parent, false);
                selleritemviewholder holder = new selleritemviewholder(view);
                return holder;
            }
        };

        recylerview.setAdapter(adapter);
        adapter.startListening();

    }

    private void deletesellerproduct(String productid) {
        unverifiedproductref.child(productid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(sellerpage.this,"order has been removed",Toast.LENGTH_SHORT).show();

            }
                });
            }
}

