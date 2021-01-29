package com.example.flipkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register_app extends AppCompatActivity {
    private Button createbutton;
    private EditText Inputname,phonenumber,password;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_app);
        createbutton=(Button) findViewById(R.id.login_button);
        Inputname=(EditText) findViewById(R.id.name_input);
        password=(EditText) findViewById(R.id.password_input);
        phonenumber=(EditText) findViewById(R.id.number_input);
        loadingbar=new ProgressDialog(this);
    createbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           CreateAccount();
        }
    });

    }
    private void CreateAccount()
    {
        String name=Inputname.getText().toString();
        String pass=password.getText().toString();
        String phone=phonenumber.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"pls write ur name",Toast.LENGTH_LONG);
        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"pls write ur password",Toast.LENGTH_LONG);
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"pls write ur phonnumber",Toast.LENGTH_LONG);
        }
        else
        {
            loadingbar.setTitle("pls wait");
            loadingbar.setMessage("we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            ValidatePhonenumber(name,pass,phone);
        }
    }

    private void ValidatePhonenumber(final String name, final String pass, final String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",pass);
                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Register_app.this,"aacnt has been created",Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();
                                Intent intent=new Intent(Register_app.this,Login_activity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Register_app.this,"network issues",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Register_app.this,"this" +phonenumber+ "already exist.",Toast.LENGTH_LONG).show();
                    loadingbar.dismiss();
                    Toast.makeText(Register_app.this,"try again later",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Register_app.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
