package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    private GoogleMap mMap;
    private DatabaseReference reference;
   private LocationManager locationManager;
   private final int MINTIME=1000,MINDISTANCE=1;
    Marker mymarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE) ;
        reference= FirebaseDatabase.getInstance().getReference().child(fauth.getCurrentUser().getUid());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationUpdate();
        ReadChanges();
    }

    private void ReadChanges() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("tag", "entered read chages");
                if (snapshot.exists()) {
                    Log.d("tag", "snapshot does exits");
                    try {

                        Coordinates location = snapshot.getValue(Coordinates.class);
                        Log.d("tag", "after loc");
                        if (location != null){
                            Log.d("tag",location.getLatitude()+" "+ location.getLongitude());
                            mymarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));}
                        else {
                            Log.d("tag","came in else part");
                        }
                    } catch (Exception e) {
                        Log.d("tag","exception");
                    }
                }
                else
                    Log.d("tag", "snapshot does not exits");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationUpdate() {

        if(locationManager!=null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
       if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINTIME,MINDISTANCE,this);
       }
       else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
           locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MINTIME,MINDISTANCE,this);
       }
       else{

       } }
        else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocationUpdate();
            }
            else{

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
       mymarker= mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

       mMap.getUiSettings().setZoomControlsEnabled(true);
       mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
            public void onLocationChanged(@NonNull Location location) {
                 if(location!=null){
                     saveLocation(location);
                 }
                 else{

                 }
            }

    private void saveLocation(Location location) {
        reference.setValue(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}