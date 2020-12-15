package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import static com.google.firebase.firestore.FieldValue.delete;

public class scheduledPassengerDet extends AppCompatActivity {

    TextView darea,dcity,ddest,ddate,dtime,dprice,dname,dpcode,demail,dphone;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    Button CB,TJ;
    ImageView profileimage;
    String id;
    StorageReference     mStorageReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_passenger_det);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");
        final DocumentReference documentReference=fStore.collection("journey").document(id);
        final DocumentReference documentReference2=fStore.collection("users").document(id);
        dphone=findViewById(R.id.phoneno);
        profileimage = findViewById(R.id.imageView2);
        demail=findViewById(R.id.email);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dname=findViewById(R.id.name);
        dpcode=findViewById(R.id.passengercode);
        darea=findViewById(R.id.area);
        dcity=findViewById(R.id.city);
        ddest=findViewById(R.id.dest);
        ddate =findViewById(R.id.date);
        dtime=findViewById(R.id.time);
        dprice=findViewById(R.id.price);
        CB=findViewById(R.id.cancelBooking);
        TJ=findViewById(R.id.button2);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String b= (String) documentSnapshot.get("b");
                if(b.equals("1")){
                    TJ.setVisibility(View.VISIBLE);
                    CB.setVisibility(View.INVISIBLE);
                }
                else{

                }
            }
        });

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

                    dpcode.setText(documentSnapshot.getString(fauth.getCurrentUser().getUid()));
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
            public void onClick(View v){
                Toast.makeText(scheduledPassengerDet.this,"you have successfully canceled your booking",Toast.LENGTH_SHORT).show();
                getval();
            }
        });
       TJ.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              Intent intent = new Intent(scheduledPassengerDet.this, MapsActivity2.class);
               intent.putExtra("key", id);

               startActivity(intent);
           }
       });
    }

    public void getval(){
        fStore.collection("journey").document(id).update("passengers",
                FieldValue.arrayRemove(fauth.getCurrentUser().getUid()));

        DocumentReference docRef2 = fStore.collection("users").document(fauth.getCurrentUser().getUid());
        Map<String,Object> updates2 = new HashMap<>();
        updates2.put("JourneyCode", FieldValue.delete());
        docRef2.update(updates2);

        DocumentReference docRef = fStore.collection("journey").document(id);
        Map<String,Object> updates = new HashMap<>();
        updates.put(fauth.getCurrentUser().getUid(), FieldValue.delete());
        docRef.update(updates);
    }
}