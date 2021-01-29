package com.example.flipkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flipkart.Model.Adminorders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;

public class checkorders extends AppCompatActivity {

    private RecyclerView orderlist;
    private DatabaseReference orderref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorders);
        orderref= FirebaseDatabase.getInstance().getReference().child("orders");
        orderlist = (RecyclerView) findViewById(R.id.orders_list_final);
        orderlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Adminorders> options =
                new FirebaseRecyclerOptions.Builder<Adminorders>()
                .setQuery(orderref,Adminorders.class)
                .build();

        FirebaseRecyclerAdapter<Adminorders , AdminViewHolder> adapter =
                new FirebaseRecyclerAdapter<Adminorders, AdminViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewHolder holder, final int position, @NonNull final Adminorders model) {

                        holder.username.setText("Name: " + model.getName());
                        holder.usershippingaddress.setText("Address " + model.getAddress() + " "+model.getCity());
                        holder.userdatetime.setText("order at " + model.getDate() + " " +model.getTime());
                        holder.usertotalprice.setText("Total price " + model.getTotalamount());
                        holder.userphonenumber.setText("phonenumber" + model.getPhone());

                       holder.showorders.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String uID= getRef(position).getKey();
                               Intent intent = new Intent(checkorders.this , Adminuseractivity.class);
                               intent.putExtra("uid",uID);
                               startActivity(intent);

                           }
                       });
                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               CharSequence options [] = new CharSequence[]
                                       {
                                               "Yes",
                                               "NO"
                                       };
                               AlertDialog.Builder builder = new AlertDialog.Builder(checkorders.this);
                               builder.setTitle("Have you shipped this order products");
                               builder.setItems(options, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int i) {
                                       if( i == 0)
                                       {
                                                   String uID = getRef(position).getKey();
                                                   RemoveOrder(uID);
                                       }
                                       else
                                       {
                                           finish();
                                       }
                                   }
                               });
                               builder.show();
                           }
                       });
                    }

                    @NonNull
                    @Override
                    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_layout,parent,false);
                        return new AdminViewHolder(view);
                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();

    }

    private void RemoveOrder(String uID) {
        orderref.child(uID).removeValue();
    }

    public static class  AdminViewHolder extends RecyclerView.ViewHolder
    {

        public TextView username, userphonenumber, usertotalprice,userdatetime,usershippingaddress;
        public Button showorders;

        public AdminViewHolder(@NonNull View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.order_username);
            userphonenumber = itemView.findViewById(R.id.user_phonenumber);
            usertotalprice = itemView.findViewById(R.id.order_price_layout);
            userdatetime=itemView.findViewById(R.id.order_datetime_layout);
            usershippingaddress=itemView.findViewById(R.id.order_shipping_address);
            showorders =itemView.findViewById(R.id.click_order);
        }
    }

}
