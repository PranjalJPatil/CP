package com.example.firebaseapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    ImageView mImageView;
    TextView fname,email,profileimage,phoneno,addr,license,aadhar;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    StorageReference mStorageReference;
    ImageButton imagebutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        mImageView = findViewById(R.id.profilepicture);
        fname = findViewById(R.id.fullname);
        email = findViewById(R.id.emailid);
        mImageView = findViewById(R.id.profilepicture);
        profileimage = findViewById(R.id.upload);
        phoneno = findViewById(R.id.phone);
        addr = findViewById(R.id.address);
        imagebutton = findViewById(R.id.back);
        license = findViewById(R.id.lisenceno);
        aadhar = findViewById(R.id.aadhar);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        StorageReference fileref = mStorageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mImageView);
            }
        });


        final DocumentReference documentReference=fStore.collection("users").document(fauth.getCurrentUser().getUid());

        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    fname.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    phoneno.setText(documentSnapshot.getString("phoneno"));
                    addr.setText(documentSnapshot.getString("Address"));
                    license.setText(documentSnapshot.getString("LicenseNO"));
                    aadhar.setText(documentSnapshot.getString("AadharCardNo"));
                }
                else
                    Log.d("TAG","error!!");
            }
        });

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageuri = data.getData();
                mImageView.setImageURI(imageuri);

                uploadimagetofirebase(imageuri);
            }
        }
    }

    private void uploadimagetofirebase(Uri imageuri) {
        final StorageReference fileref = mStorageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfilePage.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(mImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this,"Image could not be uploaded",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
