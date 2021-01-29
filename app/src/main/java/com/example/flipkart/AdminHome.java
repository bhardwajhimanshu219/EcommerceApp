package com.example.flipkart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {
    private Button logoutbtn, checkordersbtn, maintainordr, approveorderbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        maintainordr=(Button)findViewById(R.id.maintain_btn);
        logoutbtn=(Button) findViewById(R.id.log_out_btn);
        checkordersbtn=(Button) findViewById(R.id.checkorder_btn);
        approveorderbtn=(Button) findViewById(R.id.check_approve_order_btn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHome.this,MainActivity.class);

                startActivity(intent);
            }
        });
        checkordersbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this , checkorders.class);
                startActivity(intent);
            }
        });
        maintainordr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,Homeactivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
        approveorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(AdminHome.this,Approveorders.class);
                    startActivity(intent);
            }
        });
    }
}
