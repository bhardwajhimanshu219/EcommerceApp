package com.example.flipkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.Model.Users;
import com.example.flipkart.Prevalent.Prevalent;
import com.example.flipkart.seller.selleractivity;
import com.example.flipkart.seller.sellerpage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinnow,loginbutton;
    private ProgressDialog loadingbar;
    private TextView sellerbegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinnow = (Button) findViewById(R.id.main_join_now);
        loginbutton = (Button) findViewById(R.id.main_login);
        sellerbegin = (TextView) findViewById(R.id.seller_entry);
        loadingbar = new ProgressDialog(this);

        Paper.init(this);
        sellerbegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, selleractivity.class);
                startActivity(intent);

            }
        });
        loginbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent=new Intent(MainActivity.this,Login_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                );

        joinnow.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent=new Intent(MainActivity.this,Register_app.class);
                                           startActivity(intent);
                                       }
                                   }


        );
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPhoneKey != "" && UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingbar.setTitle("already logged in");
                loadingbar.setMessage("we are checking credentials");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if(firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, sellerpage.class);
            startActivity(intent);

        }
    }

    private void AllowAccess(final String phn, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phn).exists())
                {
                    Users usersdata = dataSnapshot.child("Users").child(phn).getValue(Users.class);
                    if (usersdata.getPhone().equals(phn)) {
                        if (usersdata.getPassword().equals(password)) {
                           Toast.makeText(MainActivity.this,"Logeed in successfully",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent=new Intent(MainActivity.this,Login_activity.class);
                            startActivity(intent);
                        }
                        else
                            {

                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
