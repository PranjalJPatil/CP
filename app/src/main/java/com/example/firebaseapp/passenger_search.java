package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class passenger_search extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colref = db.collection("journey");
    EditText usource,udest;
    Button uload;
    DriverAdapter mDriverAdapter;
    String s,d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_search);
        usource=findViewById(R.id.source);
        udest=findViewById(R.id.dest);
        uload=findViewById(R.id.load);
        setUpRecyclerView();
        uload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s=usource.getText().toString();
                d=udest.getText().toString();
                Query query = colref.whereEqualTo("city",s.toString()).whereEqualTo("destination",d.toString());
                FirestoreRecyclerOptions<driverdetails> options = new FirestoreRecyclerOptions.Builder<driverdetails>()
                        .setQuery(query, driverdetails.class)
                        .build();
                mDriverAdapter.updateOptions(options);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = colref;
        FirestoreRecyclerOptions<driverdetails> options = new FirestoreRecyclerOptions.Builder<driverdetails>()
                .setQuery(query, driverdetails.class)
                .build();
        mDriverAdapter = new DriverAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mDriverAdapter);

        mDriverAdapter.setOnItemClickListener(new DriverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                driverdetails DD= documentSnapshot.toObject(driverdetails.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(passenger_search.this, selectedDriverDet.class);
                intent.putExtra("key", id);
                startActivity(intent);
            }});
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDriverAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mDriverAdapter.stopListening();
    }
}