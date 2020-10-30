package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;
import java.util.Map;

import static android.util.Log.d;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,mLicenseno,mAadharcard,mAddress;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName=findViewById(R.id.FullName);
        mEmail=findViewById(R.id.Email);
        mPassword=findViewById(R.id.Password);
        mPhone=findViewById(R.id.Phone);
        mRegisterBtn=findViewById(R.id.BtnRegister);
        mLoginBtn=findViewById(R.id.Logintxt);
        mLicenseno=findViewById(R.id.LicenseNo);
        mAadharcard=findViewById(R.id.AadharCard);
        mAddress=findViewById(R.id.Address);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                final String fullname=mFullName.getText().toString();
                final String phone=mPhone.getText().toString();
                final String licenseno=mLicenseno.getText().toString();
                final String aadharcard=mAadharcard.getText().toString();
                final String address=mAddress.getText().toString();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("password is required");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("password must be 6 characters long");
                    return;
                }
                if(TextUtils.isEmpty(licenseno)){
                    mPassword.setError("password is required");
                    return;
                }
                if(TextUtils.isEmpty(aadharcard)){
                    mPassword.setError("password is required");
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    mPassword.setError("password is required");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"USER HAS BEEN CREATED",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("fName",fullname);
                            user.put("email", email);
                            user.put("phoneno",phone);
                            user.put("LicenseNO",licenseno);
                            user.put("AadharCardNo",aadharcard);
                            user.put("Address",address);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                 @Override
                                                                                 public void onSuccess(Void aVoid) {
                                                                                     Log.d("TAG","On success profile is created"+ userID );
                                                                                 }
                                                                             }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","onFailure"+e.toString());
                                }
                            });
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this,"Error!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}