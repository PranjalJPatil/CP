package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class scheduledDriverDet extends AppCompatActivity {
    Map<String,Object> user=new HashMap<>();
    TextView darea,dcity,ddest,ddate,dtime,dprice,dname;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    String name;
    Button CB,SJ,EJ;
    String id,m_Text="";
    List<String> group=new ArrayList<>();

    String JC="";
     CollectionReference colref ;
    PassengerAdapter mPassengerAdapter;
     String drivername,uid,area,city,date,time,price,dest;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_driver_det);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("key");
        JC=getIntent().getStringExtra("key2");
        Log.d("tag", "DocumentSnapshot data of JC" + JC);
        Log.d("tag",JC);
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
        SJ=findViewById(R.id.startJourney);
        EJ=findViewById(R.id.endJourney);

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
                    func();

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

        SJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EJ.setVisibility(View.VISIBLE);
                SJ.setVisibility(View.INVISIBLE);
            }
        });
        EJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final DocumentReference documentReference=fStore.collection("journeyCodeHis").document(JC);
                user.put("name",drivername);
                user.put("area",area);
                user.put("city", city);
                user.put("date",date);
                user.put("did",fauth.getCurrentUser().getUid());
                user.put("time",time);
                user.put("price",price);
                user.put("destination",dest);

                Log.d("tag",drivername+" "+area+" "+city);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        func2();
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
       ;
    }
    public void func2(){
        DocumentReference documentReference3=fStore.collection("journey").document(fauth.getCurrentUser().getUid());
        documentReference3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                group = (List<String>) documentSnapshot.get("startJourney");

                user.put("passenger",group);
                fStore.collection("journeyCodeHis").document(JC).set(user);
                DocumentReference documentReference1=fStore.collection("journey").document(fauth.getCurrentUser().getUid());
                documentReference1.delete();
            }
        });

    }
    public void func(){
        final DocumentReference documentReference2=fStore.collection("journey").document(fauth.getCurrentUser().getUid());
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
    private void setUpRecyclerView() {
        Query query = colref.whereEqualTo("JourneyCode",JC);
        FirestoreRecyclerOptions<PassengerDetails> options = new FirestoreRecyclerOptions.Builder<PassengerDetails>()
                .setQuery(query, PassengerDetails.class)
                .build();

        mPassengerAdapter = new PassengerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mPassengerAdapter);

        mPassengerAdapter.setOnItemClickListener(new PassengerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                PassengerDetails DD= documentSnapshot.toObject(PassengerDetails.class);
                final String id = documentSnapshot.getId();
                
//                Intent intent = new Intent(scheduledDriverDet.this, selectedDriverDet.class);
//                intent.putExtra("key", id);
//                startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(scheduledDriverDet.this);
                builder.setTitle("Enter Passenger Code");

//              Set up the input
                final EditText input = new EditText(scheduledDriverDet.this);
//              Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                final DocumentReference docref = fStore.collection("journey").document(fauth.getCurrentUser().getUid());
//              Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d("tag",m_Text);
                                String otp = documentSnapshot.getString(id);
                                Log.d("tag",otp);
                                if(m_Text.equals(otp)){
                                    Log.d("tag","Success");
                                    Toast.makeText(scheduledDriverDet.this,"Otp success",Toast.LENGTH_SHORT).show();
                                    docref.update("startJourney",FieldValue.arrayUnion(id));
                                    docref.update("passengers",FieldValue.arrayRemove(id));
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }});
    }
    public void getval()
    {
        fStore.collection("journey").document(fauth.getCurrentUser().getUid()).delete();
    }
}