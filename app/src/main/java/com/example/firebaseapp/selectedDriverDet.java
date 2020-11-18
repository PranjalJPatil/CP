package com.example.firebaseapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.FirestoreGrpc;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class selectedDriverDet  extends AppCompatActivity {
    TextView darea,dcity,ddest,ddate,dtime,dprice,dname,dphone,demail;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    Button CB,SJ;
    String id;
    StorageReference     mStorageReference;
    ImageView profileimage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecteddriverdet);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");
        final DocumentReference documentReference=fStore.collection("journey").document(id);
        final DocumentReference documentReference2=fStore.collection("users").document(id);
        profileimage = findViewById(R.id.imageView2);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        demail=findViewById(R.id.email);
        dphone=findViewById(R.id.phoneno);
        dname=findViewById(R.id.name);
        darea=findViewById(R.id.area);
        dcity=findViewById(R.id.city);
        ddest=findViewById(R.id.dest);
        ddate =findViewById(R.id.date);
        dtime=findViewById(R.id.time);
        dprice=findViewById(R.id.price);
        CB=findViewById(R.id.confirmBooking);

        StorageReference fileref = mStorageReference.child("users/"+id+"/profile.jpg");
        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimage);
            }
        });
        documentReference2.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dphone.setText(documentSnapshot.getString("phoneno"));
                demail.setText(documentSnapshot.getString("email"));
            }
        });
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
                }
                else
                    Log.d("TAG","error!!");
            }
        });


        CB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getval();

            }
        });


    }

    public void getval2(final String JC){
        final DocumentReference documentReference2=fStore.collection("users").document(fauth.getCurrentUser().getUid());
        documentReference2.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = new HashMap<>();
                data.put("JourneyCode", JC);
                documentReference2.set(data, SetOptions.merge());
            }
        });
    }
    public void getval(){
        final DocumentReference documentReference2=fStore.collection("journey").document(id);
        documentReference2.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String seats= (String) documentSnapshot.getString("seatsLeft");
                int i=Integer.parseInt(seats);
                i--;
                String countStr = String.valueOf(i);
                documentReference2.update("seatsLeft",countStr);
                String curruser=fauth.getCurrentUser().getUid();

                int min = 1000;
                int max = 9999;
                int b = (int)(Math.random()*(max-min+1)+min);

                Map<String, Object> data = new HashMap<>();
                data.put(fauth.getCurrentUser().getUid(), Integer.toString(b));

                getval2((String) documentSnapshot.get("JourneyCode"));

                fStore.collection("journey").document(id).set(data, SetOptions.merge());
                documentReference2.update("passengers", FieldValue.arrayUnion(curruser));
            }
        });
    }

}