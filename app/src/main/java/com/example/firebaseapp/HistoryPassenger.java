package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryPassenger extends AppCompatActivity {
    DriverAdapter mDriverAdapter;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    FirebaseFirestore fStore=FirebaseFirestore.getInstance();
    private CollectionReference colref = fStore.collection("journeyCodeHis");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_passenger);
        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = colref.whereArrayContains("passenger",fAuth.getCurrentUser().getUid());
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
                Intent intent = new Intent(HistoryPassenger.this, HistoryPassengerDet.class);
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