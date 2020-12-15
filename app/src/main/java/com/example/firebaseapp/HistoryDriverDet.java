package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryDriverDet extends AppCompatActivity {
    String uid ;
    Map<String,Object> user=new HashMap<>();
    TextView darea,dcity,ddest,ddate,dtime,dprice,dname,p1name,p2name,p3name,p4name,p1phone,p2phone,p3phone,p4phone;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    String id;
    List<String> group;
    String JC="";
    CollectionReference colref ;
    String drivername,area,city,date,time,price,dest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver_det);
        fauth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");

        final DocumentReference documentReference = fStore.collection("journeyCodeHis").document(id);
        colref = fStore.collection("users");
        dname=findViewById(R.id.name);
        darea = findViewById(R.id.area);
        dcity = findViewById(R.id.city);
        ddest = findViewById(R.id.dest);
        ddate = findViewById(R.id.date);
        dtime = findViewById(R.id.time);
        dprice = findViewById(R.id.price);
        p1name=findViewById(R.id.passenger1name);
        p1phone=findViewById(R.id.passenger1phone);
        p2name=findViewById(R.id.passenger2name);
        p2phone=findViewById(R.id.passenger2phone);
        p3name=findViewById(R.id.passenger3name);
        p3phone=findViewById(R.id.passenger3phone);
        p4name=findViewById(R.id.passenger4name);
        p4phone=findViewById(R.id.passenger4phone);

        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dname.setText(documentSnapshot.getString("name"));
                    darea.setText(documentSnapshot.getString("area"));
                    dcity.setText(documentSnapshot.getString("city"));
                    ddest.setText(documentSnapshot.getString("destination"));
                    ddate.setText(documentSnapshot.getString("date"));
                    dtime.setText(documentSnapshot.getString("time"));
                    dprice.setText(documentSnapshot.getString("price"));
                    group = (List<String>) documentSnapshot.get("passenger");

                    for(int i=0;i<group.size();i++){
                        uid = group.get(i);
                        disptext( uid, i);
                    }
                    func();

                } else
                    Log.d("TAG", "error!!");
            }
        });
    }
    public void disptext(String uid,int i){
        Log.d("T-2", uid+" "+i);
        if(i==0){
            Log.d("T-3", uid+" "+i);
          DocumentReference documentReferenc=fStore.collection("users").document(uid);
          documentReferenc.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  String n=documentSnapshot.getString("fName"),p= documentSnapshot.getString("phoneno");
               p1name.setText(n);
               p1phone.setText(p);
              }
          });
        }
       else if(i==1){
            Log.d("T-3", uid+" "+i);
            DocumentReference documentReferenc=fStore.collection("users").document(uid);
            documentReferenc.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String n=documentSnapshot.getString("fName"),p= documentSnapshot.getString("phoneno");
                    p2name.setText(n);
                    p2phone.setText(p);
                }
            });
        }
        else if(i==2){
            Log.d("T-3", uid+" "+i);
             DocumentReference documentReferenc=fStore.collection("users").document(uid);
            documentReferenc.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    p3name.setText(documentSnapshot.getString("fName"));
                    p3phone.setText(documentSnapshot.getString("phoneno"));
                }
            });
        }
        else if(i==3){
            Log.d("T-4", uid+" "+i);
             DocumentReference documentReferenc=fStore.collection("users").document(uid);
            documentReferenc.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    p4name.setText(documentSnapshot.getString("fName"));
                    p4phone.setText(documentSnapshot.getString("phoneno"));
                }
            });
        }
    }
    public void func(){
        final DocumentReference documentReference2=fStore.collection("journeyCodeHis").document(id);
        documentReference2.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                drivername=documentSnapshot.get("name").toString();
                area=documentSnapshot.getString("area");
                city=documentSnapshot.getString("city");
                dest=documentSnapshot.getString("destination");
                date=documentSnapshot.getString("date");
                time=documentSnapshot.getString("time");
                price=documentSnapshot.getString("price");
                Log.d("tag",drivername+" "+area+" "+city);
            }
        });

    }




}