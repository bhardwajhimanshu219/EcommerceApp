package com.example.flipkart.seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Seller_add_category extends AppCompatActivity {

    private String categoryname, pname, pdescription, pprice, savecurrentdate, savecurrenttime;
    private ImageView producticon;
    private EditText name,description,price;
    private Button enter;
    private  static final int GalleryPick =1;
    private Uri ImageUri;
    private String randomkey,downloadimageurl;
    private ProgressDialog loadingbar;
    private StorageReference Productimageref;
    private DatabaseReference Productref, sellersref;

    private String sName, sAddress, sPhone, sEmail, sId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_category);

        categoryname = getIntent().getExtras().get("category").toString();
        Productimageref= FirebaseStorage.getInstance().getReference().child("Productimages");
        Productref = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersref = FirebaseDatabase.getInstance().getReference().child("Sellers");
        Toast.makeText(Seller_add_category.this,categoryname,Toast.LENGTH_LONG).show();
        producticon=(ImageView) findViewById(R.id.addproductimage);
        name=(EditText) findViewById(R.id.productname);
        description=(EditText) findViewById(R.id.description);
        price=(EditText) findViewById(R.id.price);
        enter=(Button)findViewById(R.id.submit_button);
        loadingbar=new ProgressDialog(this);
        producticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opengallery();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validateproductdata();
            }
        });
        sellersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            sName = dataSnapshot.child("name").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sId = dataSnapshot.child("sid").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void Opengallery()
    {
       Intent galleryIntent = new Intent();
       galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
       galleryIntent.setType("image/*");
       startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            producticon.setImageURI(ImageUri);
        }

    }
    private void Validateproductdata()
    {
        pdescription = description.getText().toString();
        pprice = price.getText().toString();
        pname = name.getText().toString();
        if(ImageUri == null)
        {
            Toast.makeText(this,"prduct image is mandatory",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pdescription))
        {
            Toast.makeText(this," pls write product description... ",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pprice))
        {
            Toast.makeText(this," pls write product price... ",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pname))
        {
            Toast.makeText(this," pls write product name... ",Toast.LENGTH_LONG).show();
        }
        else
        {
            storeproductinformation();
        }
    }
    private void storeproductinformation()
    {
        loadingbar.setTitle("Adding new product");
        loadingbar.setMessage("we are adding product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        savecurrentdate =currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime =currentTime.format(calendar.getTime());
        randomkey = savecurrentdate + savecurrenttime;
        final StorageReference filfepath = Productimageref.child(ImageUri.getLastPathSegment() + randomkey + ".jpg");
        final UploadTask uploadTask = filfepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(Seller_add_category.this,"Error" + message,Toast.LENGTH_LONG).show();
                    loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Seller_add_category.this,"product image uploaded succcessfully",Toast.LENGTH_LONG).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                                throw task.getException();

                        }
                        downloadimageurl = filfepath.getDownloadUrl().toString();
                        return filfepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadimageurl = task.getResult().toString();
                            Toast.makeText(Seller_add_category.this,"got the image url successfully",Toast.LENGTH_LONG).show();
                             Saveproducttodatabase();
                        }
                    }
                });
            }
        });
    }
        private void Saveproducttodatabase()
        {
            HashMap<String, Object> prductmap=new HashMap<>();
            prductmap.put("pid",randomkey);
            prductmap.put("date",savecurrentdate);
            prductmap.put("time",savecurrenttime);
            prductmap.put("description",pdescription);
            prductmap.put("image",downloadimageurl);
            prductmap.put("category",categoryname);
            prductmap.put("price",pprice);
            prductmap.put("name",pname);

            prductmap.put("sellername",sName);
            prductmap.put("selleraddress",sAddress);
            prductmap.put("sellerphone",sPhone);
            prductmap.put("sid",sId);
            prductmap.put("selleremail",sEmail);
            prductmap.put("productstate","Not Approved");

            Productref.child(randomkey).updateChildren(prductmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent=new Intent(Seller_add_category.this , sellerpage.class);
                        startActivity(intent);
                        loadingbar.dismiss();
                        Toast.makeText(Seller_add_category.this,"product is added successfully",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String message=task.getException().toString();
                        Toast.makeText(Seller_add_category.this,"Error"+message,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
}
