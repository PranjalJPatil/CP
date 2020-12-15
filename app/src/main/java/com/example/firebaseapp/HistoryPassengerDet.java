package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPassengerDet extends AppCompatActivity {
    List<String> group;
    TextView darea,dcity,ddest,ddate,dtime,dprice,dname;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    StorageReference mStorageReference;
    ImageView profileimage;
    Button CB;
    String id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_passenger_det);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");
        Log.d("jc",id);
        final DocumentReference documentReference=fStore.collection("journeyCodeHis").document(id);
        profileimage = findViewById(R.id.imageView2);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dname=findViewById(R.id.name);
        darea=findViewById(R.id.area);
        dcity=findViewById(R.id.city);
        ddest=findViewById(R.id.dest);
        ddate =findViewById(R.id.date);
        dtime=findViewById(R.id.time);
        dprice=findViewById(R.id.price);


        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    dname.setText(documentSnapshot.getString("name"));
                    darea.setText(documentSnapshot.getString("area"));
                    dcity.setText(documentSnapshot.getString("city"));
                    ddest.setText(documentSnapshot.getString("destination"));
                    ddate.setText(documentSnapshot.getString("date"));
                    dtime.setText(documentSnapshot.getString("time"));
                    dprice.setText(documentSnapshot.getString("price"));
                    group = (List<String>) documentSnapshot.get("passenger");
                    String uid=group.get(0);
                    StorageReference fileref = mStorageReference.child("users/"+documentSnapshot.getString("did")+"/profile.jpg");
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileimage);
                        }
                    });
                    Log.d("tag",uid);
                }
                else
                    Log.d("TAG","error!!");
            }
        });



    }

}