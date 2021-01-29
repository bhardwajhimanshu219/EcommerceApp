package com.example.flipkart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.Model.cart;
import com.example.flipkart.Prevalent.Prevalent;
import com.example.flipkart.viewholder.Cartviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cartactivity extends AppCompatActivity {
    private RecyclerView recycleview;
    private RecyclerView.LayoutManager layoutmanager;

    private Button nextbutton;
    private TextView textview , txtmsg ;
    private int totalamnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartactivity);

        recycleview = (RecyclerView) findViewById(R.id.cart_list);
        recycleview.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(layoutmanager);
        txtmsg=(TextView) findViewById(R.id.msg);
        nextbutton=(Button) findViewById(R.id.next_button);
        textview=(TextView) findViewById(R.id.total_price);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
textview.setText("Total price" + String.valueOf(totalamnt));

                Intent intent = new Intent(cartactivity.this,shipment_details.class);
                intent.putExtra("Total price",String.valueOf(totalamnt));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart()
    {
        textview.setText("Total price" + String.valueOf(totalamnt));
        Checkorderstate();
        super.onStart();
        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<cart> options=
                new FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(cartlistref.child("User View")
                .child(Prevalent.currentonlineuser.getPhone()).child("Products"), cart.class)
                .build();
      FirebaseRecyclerAdapter<cart,Cartviewholder> adapter
              = new FirebaseRecyclerAdapter<cart,Cartviewholder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull Cartviewholder holder, int position, @NonNull final cart model)
          {

              holder.txtproductquantity.setText("Quantity = " +model.getQuantity());
              holder.txtprductname.setText("NAME = "+model.getPname());
              holder.txtproductprice.setText("PRICE = "+model.getPrice()+ "rupees");
              int itemprice=((Integer.valueOf(model.getPrice())))*Integer.valueOf(model.getQuantity());
              totalamnt = totalamnt + itemprice;
              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      CharSequence options[] = new CharSequence[]
                              {
                                      "Edit",
                                      "Remove"
                              };
                      AlertDialog.Builder builder = new AlertDialog.Builder(cartactivity.this);
                      builder.setTitle("cart options:");

                      builder.setItems(options, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int i) {
                              if(i == 0)
                              {
                                  Intent intent = new Intent(cartactivity.this,product_details.class);
                                   intent.putExtra("pid",model.getPid());
                                   startActivity(intent);
                              }
                              if (i == 1)
                              {
                                  cartlistref.child("User View").child(Prevalent.currentonlineuser.getPhone())
                                  .child("Products")
                                  .child(model.getPid()).removeValue()
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful())
                                           {
                                               Toast.makeText(cartactivity.this,"item removed successfully",Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(cartactivity.this,Homeactivity.class);
                                               startActivity(intent);

                                           }
                                       }
                                   });
                              }
                          }
                      });
                      builder.show();
                  }
              });

          }

          @NonNull
          @Override
          public Cartviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent,false);
              Cartviewholder holder = new Cartviewholder(view);
              return  holder;
          }
      };
      recycleview.setAdapter(adapter);
      adapter.startListening();
    }
    private void Checkorderstate()
    {
        DatabaseReference orderref;
        orderref = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentonlineuser.getPhone());
        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                            String shippingstate = dataSnapshot.child("state").getValue().toString();
                            String userName = dataSnapshot.child("name").getValue().toString();

                        if(shippingstate.equals("shipped"))
                        {
                            textview.setText("Dear" +userName +" your order is shipped successfully");
                            recycleview.setVisibility(View.GONE);
                            txtmsg.setText("Your order has been received soon it will be delivered Till then keep shopping");
                            txtmsg.setVisibility(View.VISIBLE);
                            nextbutton.setVisibility(View.GONE);
                        }
                        else if(shippingstate.equals("not shipped"))
                        {
                            textview.setText("order is  not shipped yet");
                            recycleview.setVisibility(View.GONE);
                            txtmsg.setVisibility(View.VISIBLE);
                            nextbutton.setVisibility(View.GONE);
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
