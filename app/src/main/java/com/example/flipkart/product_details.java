package com.example.flipkart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flipkart.Model.products;
import com.example.flipkart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class product_details extends AppCompatActivity {
    private Button addtocart;
    private ImageView productimage;
    private ElegantNumberButton numberButton;
    private TextView productdescription,productprice,productname;
    private String productid ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productid = getIntent().getStringExtra("pid");
        addtocart=(Button) findViewById(R.id.cart_details);
        productimage=(ImageView)findViewById(R.id.product_image_details);
        numberButton=(ElegantNumberButton)findViewById(R.id.product_button);
        productname= (TextView)findViewById(R.id.product_name_details);
        productprice=(TextView)findViewById(R.id.product_price_details);
        productdescription=(TextView)findViewById(R.id.product_desc_details);
        productdetails(productid);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingtocartlist();

            }
        });
    }

    private void addingtocartlist() {
        String currentdate ,currentime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
        currentdate = date.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        currentime = time.format(calendar.getTime());

        final DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartmap = new HashMap<>();
        cartmap.put("pid",productid);
        cartmap.put("pname",productname.getText().toString());
        cartmap.put("price",productprice.getText().toString());
        cartmap.put("date",currentdate);
        cartmap.put("time",currentime);
        cartmap.put("quantity",numberButton.getNumber());
        cartmap.put("discount", "");

        cartlist.child("User View").child(Prevalent.currentonlineuser.getPhone()).child("Products").child(productid)
        .updateChildren(cartmap)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    cartlist.child("Admin View").child(Prevalent.currentonlineuser.getPhone()).child("Products").child(productid)
                            .updateChildren(cartmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(product_details.this,"added successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(product_details.this,Homeactivity.class);
                                        startActivity(intent);
                                    }

                                }
                            });

                }
            }
        });

    }

    private void productdetails(String productid) {
        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    products products= dataSnapshot.getValue(products.class);
                    productname.setText(products.getName());
                    productprice.setText(products.getPrice());
                    productdescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
