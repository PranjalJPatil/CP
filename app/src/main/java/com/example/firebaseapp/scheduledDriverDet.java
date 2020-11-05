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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Map;

public class scheduledDriverDet extends AppCompatActivity {
    TextView darea,dcity,ddest,ddate,dtime,dprice,dname;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    Button CB;
    String id;
    List<String> group;
     CollectionReference colref ;
    PassengerAdapter mPassengerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_driver_det);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");
        final DocumentReference documentReference=fStore.collection("journey").document(fauth.getCurrentUser().getUid());
        colref = fStore.collection("users");
        //dname=findViewById(R.id.name);
        darea=findViewById(R.id.area);
        dcity=findViewById(R.id.city);
        ddest=findViewById(R.id.dest);
        ddate =findViewById(R.id.date);
        dtime=findViewById(R.id.time);
        dprice=findViewById(R.id.price);
        CB=findViewById(R.id.cancelBooking);

        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
          //          dname.setText(documentSnapshot.getString("name"));
                    darea.setText(documentSnapshot.getString("area"));
                    dcity.setText(documentSnapshot.getString("city"));
                    ddest.setText(documentSnapshot.getString("destination"));
                    ddate.setText(documentSnapshot.getString("date"));
                    dtime.setText(documentSnapshot.getString("time"));
                    dprice.setText(documentSnapshot.getString("price"));
                    group = (List<String>) documentSnapshot.get("passengers");
                }
                else
                    Log.d("TAG","error!!");
            }
        });
        setUpRecyclerView();

        CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getval();
            }
        });

    }
    private void setUpRecyclerView() {
        Query query = colref;
        FirestoreRecyclerOptions<PassengerDetails> options = new FirestoreRecyclerOptions.Builder<PassengerDetails>()
                .setQuery(query, PassengerDetails.class)
                .build();
        mPassengerAdapter = new PassengerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mPassengerAdapter);

    }
    public void getval(){
        fStore.collection("journey").document(fauth.getCurrentUser().getUid()).delete();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mPassengerAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mPassengerAdapter.stopListening();
    }
}