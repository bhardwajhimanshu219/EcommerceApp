package com.example.flipkart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipkart.Model.products;
import com.example.flipkart.Prevalent.Prevalent;
import com.example.flipkart.viewholder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Homeactivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference productReference;
    private RecyclerView recyclerView;
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }
        productReference= FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(Homeactivity.this, cartactivity.class);
                    startActivity(intent);
                }
            }
        });
        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView usernametextview = (TextView)headerView.findViewById(R.id.user_profile);
        CircleImageView userimageview = (CircleImageView) headerView.findViewById(R.id.profile_image);
       if (!type.equals("Admin")) {
           usernametextview.setText(Prevalent.currentonlineuser.getName());
           Picasso.get().load(Prevalent.currentonlineuser.getImage()).placeholder(R.drawable.profile).into(userimageview);
       }
        recyclerView=(RecyclerView) findViewById(R.id.Recycler_menu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {

        super.onStart();
       FirebaseRecyclerOptions<products> options=new FirebaseRecyclerOptions.Builder<products>()
               .setQuery(productReference.orderByChild("productstate").equalTo("Approved"),products.class).build();
       FirebaseRecyclerAdapter<products, productviewholder>adapter=new FirebaseRecyclerAdapter<products, productviewholder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull final products model) {
               holder.productname.setText(model.getName());
               holder.productdesc.setText(model.getDescription());
               holder.productprice.setText("price=" +model.getPrice());
               Picasso.get().load(model.getImage()).into(holder.imageView);

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(type.equals("Admin"))
                       {
                           Intent intent = new Intent(Homeactivity.this, maintainanace_activity.class);
                           intent.putExtra("pid", model.getPid());
                           startActivity(intent);

                       }
                       else {
                           Intent intent = new Intent(Homeactivity.this, product_details.class);
                           intent.putExtra("pid", model.getPid());
                           startActivity(intent);
                       }
                   }
               });


           }

           @NonNull
           @Override
           public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent,false);
                productviewholder holder=new productviewholder(view);
                return holder;
           }
       };
       recyclerView.setAdapter(adapter);
       adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homeactivity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //if(id == R.id.nav_settings)
        //{
            //return true;
        //}
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptybody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.nav_cart)
        {
            if(!type.equals("Admin"))
            {
                Intent intent = new Intent(Homeactivity.this, cartactivity.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.nav_search)
        {
            if(!type.equals("Admin"))
            {
                Intent intent = new Intent(Homeactivity.this, Search__activity.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.nav_category)
        {
        }
        else if(id == R.id.nav_settings)
        {
            if(!type.equals("Admin"))
            {
                Intent intent = new Intent(Homeactivity.this, settings.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.nav_logout)
        {
            if(!type.equals("Admin"))
            {
                Paper.book().destroy();

                Intent intent = new Intent(Homeactivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
