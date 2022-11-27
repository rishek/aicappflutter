package com.example.rishek.risheksapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity4 extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    boolean mLocationPermissionGranted;
    FusedLocationProviderClient mFusedLocationProviderClient;
    MarkerOptions markerOpts;
    Marker clickedMarker;
    Location currentLocation;
    LocationRequest request;
    LocationCallback callback;
    Location mLastKnownLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(clickedMarker==null){
                    Toast.makeText(MainActivity4.this, "PLEASE SELECT A DESTINATION ON MAP", Toast.LENGTH_LONG).show();
                    }
                    else {
                    float[] results = new float[1];
                    Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                            clickedMarker.getPosition().latitude, clickedMarker.getPosition().longitude, results);
                    double fare;
                    fare = 150 + (results[0] / 1000) * 25;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    Toast.makeText(MainActivity4.this, "The Distance is: " + df.format(results[0] / 1000) + "km", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity4.this, "The Fare is: RS " + df.format(fare), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("info", "I'm here");
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            Log.d("info", "Granted");
            getDeviceLocation();
        } else {
            Log.d("info", "Not Granted");
            getLocationPermission();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clickedMarker == null) {
                } else {
                    clickedMarker.remove();
                }
                if (markerOpts == null) {
                    markerOpts = new MarkerOptions();
                }
                markerOpts.position(latLng);
                clickedMarker = mMap.addMarker(markerOpts);

            }
        });


    }

    private void getLocationPermission() {
        Log.d("info", "Taking permission");

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("info", "permission granted");
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Log.i("info", "getDeviceLocation: ");
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.i("info", "gotlocation");
                            mLastKnownLocation = (Location) task.getResult();
                            currentLocation = mLastKnownLocation;
                            if (mLastKnownLocation==null){
                                request = new LocationRequest();
                                request.setInterval(120000); // two minute interval
                                request.setFastestInterval(120000);
                                request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                                callback =new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        List<Location> locationList = locationResult.getLocations();
                                        if (locationList.size() > 0) {
                                            //The last location in the list is the newest
                                            Location location = locationList.get(locationList.size() - 1);
                                            addMarker(location);
                                            Log.i("info", "requesting location");
                                        }
                                    }
                                } ;
                                mFusedLocationProviderClient.requestLocationUpdates(request,callback,Looper.myLooper());
                                return;
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 12));
                            addMarker(mLastKnownLocation);
                        } else {
                            Log.i("info", "notgotlocation");
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 8));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    public void addMarker(Location location) {
        mMap.clear();
        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.title("my position");
        mMap.addMarker(mp);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));

    }


}
