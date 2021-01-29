package com.example.flipkart.seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sellerlogin extends AppCompatActivity {
    private EditText emailinput,passwordinput;
    private ImageView loginimage;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerlogin);
        mAuth = FirebaseAuth.getInstance();

        loadingbar=new ProgressDialog(this);

        emailinput=(EditText) findViewById(R.id.seller_email_login);
        passwordinput=(EditText) findViewById(R.id.seller_password_login);
        loginimage=(ImageView) findViewById(R.id.seller_member);
        login = (Button) findViewById(R.id.seller_submit_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });

    }

    private void loginuser() {

        final String email = emailinput.getText().toString();
        final String password = passwordinput.getText().toString();
        if(!email.equals("") && !password.equals(""))
        {

            loadingbar.setTitle("pls wait");
            loadingbar.setMessage("we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(sellerlogin.this, "you are Login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(sellerlogin.this, sellerpage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(sellerlogin.this,"pls complete full details",Toast.LENGTH_SHORT).show();
        }
    }
}
