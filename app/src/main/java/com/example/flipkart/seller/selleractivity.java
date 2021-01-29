package com.example.flipkart.seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class selleractivity extends AppCompatActivity {
    private EditText sellername, selleremail, sellerphone, selleradrress,sellerpassword;
    private Button register, submitbtn;
    private ImageView sellerimage;
    private TextView sellerform;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selleractivity);

        mAuth = FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
        sellerform = (TextView) findViewById(R.id.seller_title);
        sellername = (EditText) findViewById(R.id.seller_name);
        selleremail = (EditText) findViewById(R.id.seller_email);
        sellerphone = (EditText) findViewById(R.id.seller_phone);
        selleradrress = (EditText) findViewById(R.id.seller_address);
        sellerpassword = (EditText) findViewById(R.id.seller_password);
        register=(Button) findViewById(R.id.seller_submit);
        submitbtn=(Button) findViewById(R.id.seller_member);
        sellerimage=(ImageView) findViewById(R.id.seller_login);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selleractivity.this,sellerlogin.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerseller();

         }
        });
            }

    private void registerseller() {

        final String name = sellername.getText().toString();
        final String phone = sellerphone.getText().toString();
        final String email = selleremail.getText().toString();
        final String password = sellerpassword.getText().toString();
        final String address = selleradrress.getText().toString();
        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("") )
        {
            loadingbar.setTitle("pls wait");
            loadingbar.setMessage("we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

                mAuth.createUserWithEmailAndPassword(email , password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    final DatabaseReference rootref;
                                    rootref = FirebaseDatabase.getInstance().getReference();

                                    String sid = mAuth.getCurrentUser().getUid();


                                    HashMap<String,Object> sellermap = new HashMap<>();

                                    sellermap.put("sid",sid);
                                    sellermap.put("phone",phone);
                                    sellermap.put("email",email);
                                    sellermap.put("password",password);
                                    sellermap.put("address",address);
                                    sellermap.put("name",name);

                                    rootref.child("Sellers").child(sid).updateChildren(sellermap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    loadingbar.dismiss();
                                                    Toast.makeText(selleractivity.this, "you are registered successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(selleractivity.this, sellerpage.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });

                                }
                            }
                        });
        }
        else
        {
            Toast.makeText(selleractivity.this,"pls complete full details",Toast.LENGTH_SHORT).show();
        }
    }
}
