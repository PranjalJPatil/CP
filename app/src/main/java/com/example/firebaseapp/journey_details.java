package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class journey_details extends AppCompatActivity {
    Map<String,Object> user=new HashMap<>();
            List<String> pb= Arrays.asList(new String[4]);
    Button backToprofile,submit;
    EditText  dprice,darea,dcity,ddest,ddate,dtime;;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        backToprofile=findViewById(R.id.goProfile);
        submit=findViewById(R.id.button);

        darea=findViewById(R.id.area);
        dcity=findViewById(R.id.city);
        ddest=findViewById(R.id.dest);
        ddate=findViewById(R.id.date);
        dtime=findViewById(R.id.time);
        dprice=findViewById(R.id.price);
        String uid2;
        uid2=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference1=fStore.collection("users").document(uid2);

        documentReference1.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name=documentSnapshot.getString("fName");
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String area,city,date,time,dest,price,uid;
                uid=fAuth.getCurrentUser().getUid();
                area=darea.getText().toString();
                city=dcity.getText().toString();
                date=ddate.getText().toString();
                time=dtime.getText().toString();
                dest=ddest.getText().toString();
                price=dprice.getText().toString();


                DocumentReference documentReference=fStore.collection("journey").document(uid);
                user.put("name",name);
                user.put("UserId",uid);
                user.put("area",area);
                user.put("city", city);
                user.put("date",date);
                user.put("time",time);
                user.put("price",price);
                user.put("destination",dest);
                user.put("seatsLeft","4");


                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG","On success profile is created" );
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","onFailure"+e.toString());
                    }
                });
            }
        });

        backToprofile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}