package com.example.flipkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipkart.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class settings extends AppCompatActivity
{
    private CircleImageView profileimage;
    private EditText phonenumber,fullname,address;
    private TextView changeprofileimage,close,update;

    private Uri imageuri;
    private String myUrl = "";
    private StorageTask uploadtask;
    private StorageReference storageprofileimages;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageprofileimages= FirebaseStorage.getInstance().getReference().child("profile pictures");

        profileimage = (CircleImageView) findViewById(R.id.settings_profile_image);
        phonenumber =(EditText) findViewById(R.id.settings_phonenumber);
        fullname = (EditText) findViewById(R.id.settings_fullname);
        address =(EditText) findViewById(R.id.settings_address);
        changeprofileimage=(TextView) findViewById(R.id.changeprofile_settings);
        close=(TextView) findViewById(R.id.close_settings);
        update=(TextView)findViewById(R.id.update_settings);

        userinfodisplay(profileimage, phonenumber, fullname, address);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    userinfosaved();
                }
                else
                {
                    updateonlyuserinfo();
                }
            }
        });
        changeprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checker = "clicked";
                CropImage.activity(imageuri)
                        .setAspectRatio(1,1)
                        .start(settings.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            profileimage.setImageURI(imageuri);

        }
        else
        {
            Toast.makeText(this,"error try again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(settings.this, settings.class));
            finish();

        }
    }
    private void updateonlyuserinfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> usermap= new HashMap<>();
        usermap.put("name",fullname.getText().toString());
        usermap.put("address",address.getText().toString());
        usermap.put("phone",phonenumber.getText().toString());
        ref.child(Prevalent.currentonlineuser.getPhone()).updateChildren(usermap);
        startActivity(new Intent(settings.this,Homeactivity.class));
        Toast.makeText(settings.this,"profile updated successfully",Toast.LENGTH_SHORT).show();
        finish();
    }
    private void userinfosaved() {

        if(TextUtils.isEmpty(fullname.getText().toString()))
        {
            Toast.makeText(this,"name is mandatory.",Toast.LENGTH_SHORT).show();
        }
         else if(TextUtils.isEmpty(address.getText().toString()))
        {
            Toast.makeText(this,"address is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonenumber.getText().toString()))
        {
            Toast.makeText(this,"phonenumber is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadimage();
        }
    }

    private void  uploadimage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("update profile");
        progressDialog.setMessage("pls wait while we are updating information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageuri != null)
        {
            final StorageReference fileref = storageprofileimages.child(Prevalent.currentonlineuser.getPhone() +".jpg");
            uploadtask = fileref.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                   if(!task.isSuccessful())
                   {
                       throw task.getException();
                   }

                    return fileref.getDownloadUrl();

                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadurl = task.getResult();
                        myUrl = downloadurl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> usermap= new HashMap<>();
                        usermap.put("name",fullname.getText().toString());
                        usermap.put("address",address.getText().toString());
                        usermap.put("phone",phonenumber.getText().toString());
                        usermap.put("image",myUrl);
                        ref.child(Prevalent.currentonlineuser.getPhone()).updateChildren(usermap);
                        progressDialog.dismiss();
                        startActivity(new Intent(settings.this,Homeactivity.class));
                        Toast.makeText(settings.this,"profile updated successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(settings.this,"error",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(settings.this,"image is not selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void userinfodisplay(final CircleImageView profileimage,final EditText phonenumber,final EditText fullname, final EditText address)
    {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineuser.getPhone());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String addr=dataSnapshot.child("address").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        Picasso.get().load(image).into(profileimage);
                        fullname.setText(name);
                        phonenumber.setText(phone);
                        address.setText(addr);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
