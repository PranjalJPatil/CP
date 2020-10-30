package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    TextView  fullname,emaila,phone,ln,an,add;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button Logout,createJ,passengerJ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone=findViewById(R.id.profilePhone);
        fullname=findViewById(R.id.profileName);
        emaila=findViewById(R.id.profileEmail);
        ln=findViewById(R.id.license);
        an=findViewById(R.id.aadharCard);
        add=findViewById(R.id.address);

        Logout=findViewById(R.id.button);
        createJ=findViewById(R.id.journeyDet);
        passengerJ=findViewById(R.id.passengerDet);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference=fStore.collection("users").document(userId);

        documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {
                    phone.setText(documentSnapshot.getString("phoneno"));
                    fullname.setText(documentSnapshot.getString("fName"));
                    emaila.setText(documentSnapshot.getString("email"));
                    ln.setText(documentSnapshot.getString("LicenseNO"));
                    an.setText(documentSnapshot.getString("AadharCardNo"));
                    add.setText(documentSnapshot.getString("Address"));
                }
                else
                    Log.d("TAG","error!!");
            }
        });
        Logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        createJ.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),journey_details.class));
            }
        });
        passengerJ.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),passenger_search.class));
            }
        });

    }/*
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent main_page = new Intent(MainActivity.this, Login.class);
        main_page.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main_page);

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }*/
}