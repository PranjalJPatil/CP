package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class scheduledDriver extends AppCompatActivity {

    Intent intent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colref = db.collection("journey");
    EditText usource,udest;
    Button uload;
    String para;
    DriverAdapter mDriverAdapter;
    FirebaseAuth fauth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_driver);
        usource=findViewById(R.id.source);
        udest=findViewById(R.id.dest);
        uload=findViewById(R.id.load);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = colref.whereEqualTo("UserId",fauth.getCurrentUser().getUid());
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
                 intent = new Intent(scheduledDriver.this, scheduledDriverDet.class);
                intent.putExtra("key", id);
                intent.putExtra("JoCo",getval());
                startActivity(intent);
            }});
    }
       String getval(){

    DocumentReference documentReference=db.collection("journey").document(fauth.getCurrentUser().getUid());
    documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            para=(String)documentSnapshot.get("JourneyCode");
            Log.d("tag",para);
        }
    });
    return para;
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