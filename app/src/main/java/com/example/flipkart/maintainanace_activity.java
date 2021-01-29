package com.example.flipkart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.seller.seller_add_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class maintainanace_activity extends AppCompatActivity {
    private Button mainatainbuton , deleteproduct;
    private EditText name,price,decs;
    private ImageView productimage;
    private String productid ="";
    private DatabaseReference productref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainanace_activity);
        productid = getIntent().getStringExtra("pid");
        productref = FirebaseDatabase.getInstance().getReference().child("Products").child(productid);


        mainatainbuton = (Button) findViewById(R.id.maintain_changes_button);
        name = (EditText) findViewById(R.id.product_maintain_name);
        price = (EditText) findViewById(R.id.product_maintain_price);
        decs = (EditText) findViewById(R.id.product_maintain_desc);
        productimage = (ImageView) findViewById(R.id.product_maintain_image);
        deleteproduct = (Button) findViewById(R.id.delete_product_button);
        
        
        displayspecificinfo();
        mainatainbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychanges();
            }
        });
        deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletethisproduct();

            }
        });

    }

    private void deletethisproduct() {

        productref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(maintainanace_activity.this,"Deleted successfully" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(maintainanace_activity.this, seller_add_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applychanges()
    {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDesc = decs.getText().toString();
        if(pName.equals(""))
        {
            Toast.makeText(this,"Write your product name",Toast.LENGTH_SHORT).show();
        }
        else if(pPrice.equals(""))
        {
            Toast.makeText(this,"Write your product price",Toast.LENGTH_SHORT).show();
        }
        else if(pDesc.equals(""))
        {
            Toast.makeText(this,"Write your product description",Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> prductmap=new HashMap<>();
            prductmap.put("pid",productid);
            prductmap.put("description",pDesc);
            prductmap.put("price",pPrice);
            prductmap.put("name",pName);
            productref.updateChildren(prductmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(maintainanace_activity.this,"updated successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(maintainanace_activity.this , AdminHome.class);
                        startActivity(intent);
                    }
                }
            });

        }

    }

    private void displayspecificinfo() {

        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String pName  = dataSnapshot.child("name").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDesc  = dataSnapshot.child("description").getValue().toString();
                    String pImage  = dataSnapshot.child("image").getValue().toString();
                    name.setText(pName);
                    price.setText(pPrice);
                    decs.setText(pDesc);
                    Picasso.get().load(pImage).into(productimage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
