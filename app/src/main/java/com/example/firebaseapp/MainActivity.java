package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView  fullname,emaila,phone,ln,an,add;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button Logout,createJ,passengerJ,upload,sJ,sjp;
    ImageView profileimage;
    StorageReference mStorageReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone=findViewById(R.id.profilePhone);
        fullname=findViewById(R.id.profileName);
        emaila=findViewById(R.id.profileEmail);
        ln=findViewById(R.id.license);
        an=findViewById(R.id.aadharCard);
        add=findViewById(R.id.address);
        profileimage = findViewById(R.id.profile);
        upload = findViewById(R.id.upload);

        sJ=findViewById(R.id.SJourney);
        sjp=findViewById(R.id.SJp);
        Logout=findViewById(R.id.button);
        createJ=findViewById(R.id.journeyDet);
        passengerJ=findViewById(R.id.passengerDet);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();

        StorageReference fileref = mStorageReference.child("users/"+userId+"/profile.jpg");
        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimage);
            }
        });


        final DocumentReference documentReference=fStore.collection("users").document(userId);

        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {
                    phone.setText(documentSnapshot.getString("phoneno"));
                    fullname.setText(documentSnapshot.getString("fName"));
                    emaila.setText(documentSnapshot.getString("email"));
                    ln.setText(documentSnapshot.getString("LicenseNO"));
                    an.setText(documentSnapshot.getString("AadharCardNo"));
                    add.setText(documentSnapshot.getString("Address"));
                }
                else
                    Log.d("TAG","error!!");
            }
        });

        Logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        createJ.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),journey_details.class));
            }
        });
        passengerJ.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),passenger_search.class));
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });
        sJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),scheduledDriver.class));
            }
        });
        sjp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),scheduledPassenger.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageuri = data.getData();
//                profileimage.setImageURI(imageuri);

                uploadimagetofirebase(imageuri);
            }
        }
    }

    private void uploadimagetofirebase(Uri imageuri) {
        final StorageReference fileref = mStorageReference.child("users/"+userId+"/profile.jpg");
        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileimage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Image could not be uploaded",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent main_page = new Intent(MainActivity.this, Login.class);
        main_page.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main_page);

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }*/
}