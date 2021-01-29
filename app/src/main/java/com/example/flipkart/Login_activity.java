package com.example.flipkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.Model.Users;
import com.example.flipkart.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login_activity extends AppCompatActivity {
    private Button Loginbutton;
    private EditText phone,pass;
    private ProgressDialog loadingbar;
    private String parentdbname = "Users";
    private TextView adminlink,notadminlink;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Loginbutton=(Button) findViewById(R.id.login_btn);
        phone = (EditText) findViewById(R.id.login_input);
        pass =(EditText) findViewById(R.id.login_password_input);
        adminlink =(TextView) findViewById(R.id.admin_textView);
        notadminlink=(TextView) findViewById(R.id.not_admin_textView);
        loadingbar=new ProgressDialog(this);
        checkBox = (CheckBox) findViewById(R.id.login_checkbox) ;
        Paper.init(this);
        Loginbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Loginuser();
            }
        });
        adminlink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Loginbutton.setText("Admin login");
                adminlink.setVisibility(view.INVISIBLE);
                notadminlink.setVisibility((view.VISIBLE));
                parentdbname="Admins";
            }
        });
        notadminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Loginbutton.setText("Login");
                adminlink.setVisibility(view.VISIBLE);
                notadminlink.setVisibility(view.INVISIBLE);
                parentdbname="Users";
            }
        });
    }
    public void Loginuser()
    {
        String password=pass.getText().toString();
        String phn=phone.getText().toString();
        if(TextUtils.isEmpty(phn))
        {
            Toast.makeText(this,"pls write ur phonenumber",Toast.LENGTH_LONG);
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"pls write ur password",Toast.LENGTH_LONG);
        }
        else
        {
            loadingbar.setTitle("pls wait");
            loadingbar.setMessage("we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(password,phn);
        }
    }
    public void AllowAccessToAccount(final String password, final String phn)
    {
        if(checkBox.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phn);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentdbname).child(phn).exists()) {
                    Users usersdata = dataSnapshot.child(parentdbname).child(phn).getValue(Users.class);
                    if (usersdata.getPhone().equals(phn)) {
                        if (usersdata.getPassword().equals(password)) {
                            if (parentdbname.equals("Admins")) {
                                Toast.makeText(Login_activity.this, "welcome admin", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(Login_activity.this, AdminHome.class);
                                startActivity(intent);
                            } else if (parentdbname.equals("Users")) {
                                Toast.makeText(Login_activity.this, "welcome user", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(Login_activity.this,Homeactivity.class);
                                Prevalent.currentonlineuser = usersdata;
                                startActivity(intent);
                            }
                        }else {

                            loadingbar.dismiss();
                            Toast.makeText(Login_activity.this, "incorrect password", Toast.LENGTH_LONG).show();
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
