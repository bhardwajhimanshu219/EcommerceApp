package com.example.flipkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flipkart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class shipment_details extends AppCompatActivity {

    private EditText nameedittext,addressedittext,phoneedittext,cityedittext;
    private Button confirmation;
    String totalbill="" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_details);
        totalbill=getIntent().getStringExtra("Total price");
        Toast.makeText(shipment_details.this,"total amount" + totalbill,Toast.LENGTH_SHORT).show();
        nameedittext=(EditText) findViewById(R.id.shipmnet_name);
        phoneedittext=(EditText) findViewById(R.id.shipmnet_phone);
        addressedittext=(EditText) findViewById(R.id.shipmnet_address);
        cityedittext=(EditText) findViewById(R.id.shipmnet_city);
        confirmation=(Button) findViewById(R.id.shipment_button);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

    }

    private void Check() {
        if(TextUtils.isEmpty(nameedittext.getText().toString()))
        {
            Toast.makeText(shipment_details.this,"mandatory to fill in",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneedittext.getText().toString()))
        {
            Toast.makeText(shipment_details.this,"mandatory to fill in",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressedittext.getText().toString()))
        {
            Toast.makeText(shipment_details.this,"mandatory to fill in",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityedittext.getText().toString()))
        {
            Toast.makeText(shipment_details.this,"mandatory to fill in",Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmationorder();
        }
    }

    private void confirmationorder() {
        final String currentdate ,currentime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
        currentdate = date.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        currentime = time.format(calendar.getTime());
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference()
                .child("orders")
                .child(Prevalent.currentonlineuser.getPhone());
        final HashMap<String,Object> shipment = new HashMap<>();
        shipment.put("totalamount",totalbill);
        shipment.put("name", nameedittext.getText().toString());
        shipment.put("phone",phoneedittext.getText().toString());
        shipment.put("date",currentdate);
        shipment.put("time",currentime);
        shipment.put("address", addressedittext.getText().toString());
        shipment.put("city",cityedittext.getText().toString());
        shipment.put("state","not shipped");
        dataref.updateChildren(shipment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentonlineuser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(shipment_details.this,"your order has been confirmed",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(shipment_details.this,Homeactivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }


}
