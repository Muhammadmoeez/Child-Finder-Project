package com.example.childfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ParentDashboard extends AppCompatActivity implements OnMapReadyCallback {

    String myCurrentID,tempParentID, myCurrentParentID;
    Location currentLocation;
    private Query query_d;
    Double  driverLong;
    Double  driverLat;
    int i;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private  static final int REQUEST_CODE = 101;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        toolbar = (Toolbar) findViewById(R.id.parentDashboardToolBar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        tempParentID = intent.getStringExtra("ParentID");
        Toast.makeText(this, "temp"+ tempParentID, Toast.LENGTH_SHORT).show();
        myCurrentParentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.parentDashboardLocationPoint);
        supportMapFragment.getMapAsync(ParentDashboard.this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

    }

    private void fetchLastLocation() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    if (tempParentID == null){
                        currentLocation = location;
                        double parentLatitude = currentLocation.getLatitude();
                        double parentLongitude = currentLocation.getLongitude();

                        HashMap parentMapLoc = new HashMap();
                        parentMapLoc.put("UserLatitude",parentLatitude);
                        parentMapLoc.put("UserLongitude",parentLongitude);

                        FirebaseDatabase.getInstance().getReference().child("Parent")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(parentMapLoc).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Parent")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.exists())
                                                    {
                                                        String parentName = dataSnapshot.child("UserName").getValue().toString();
                                                        double parentLat = (double) dataSnapshot.child("UserLatitude").getValue();
                                                        double parentLong = (double) dataSnapshot.child("UserLongitude").getValue();

                                                        LatLng parentNewLoc = new LatLng(parentLat,parentLong);

                                                        MarkerOptions optionsMarker = new MarkerOptions();
                                                        optionsMarker.title(parentName);
                                                        optionsMarker.snippet("Current Location");
                                                        optionsMarker.position(parentNewLoc);
                                                        optionsMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                                        mMap.addMarker(optionsMarker);
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parentNewLoc,15F));

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }


                            }
                        });


                        comparison();
                    }

                    else {
                        currentLocation = location;
                        double parentLatitude = currentLocation.getLatitude();
                        double parentLongitude = currentLocation.getLongitude();

                        HashMap parentMapLoc = new HashMap();
                        parentMapLoc.put("UserLatitude",parentLatitude);
                        parentMapLoc.put("UserLongitude",parentLongitude);

                        FirebaseDatabase.getInstance().getReference().child("Parent")
                                .child(tempParentID)
                                .updateChildren(parentMapLoc).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Parent")
                                            .child(tempParentID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.exists())
                                                    {
                                                        String parentName = dataSnapshot.child("UserName").getValue().toString();
                                                        double parentLat = (double) dataSnapshot.child("UserLatitude").getValue();
                                                        double parentLong = (double) dataSnapshot.child("UserLongitude").getValue();

                                                        LatLng parentNewLoc = new LatLng(parentLat,parentLong);

                                                        MarkerOptions optionsMarker = new MarkerOptions();
                                                        optionsMarker.title(parentName);
                                                        optionsMarker.snippet("Current Location");
                                                        optionsMarker.position(parentNewLoc);
                                                        optionsMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                                        mMap.addMarker(optionsMarker);
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parentNewLoc,15F));

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }
                        });


                        comparison();
                    }

                }
            }
        });
    }


    private void comparison() {

        if (tempParentID == null){

            DatabaseReference parentRefForFetchUserID = FirebaseDatabase.getInstance().getReference()
                    .child("Parent")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            parentRefForFetchUserID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {


                        myCurrentID = dataSnapshot.child("UserID").getValue().toString();
                        Toast.makeText(ParentDashboard.this, "nullllll "+myCurrentID, Toast.LENGTH_SHORT).show();
                        DriverLoc();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        else {

            DatabaseReference parentRefForFetchUserID = FirebaseDatabase.getInstance().getReference()
                    .child("Parent")
                    .child(tempParentID);

            parentRefForFetchUserID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {


                        myCurrentID = dataSnapshot.child("UserID").getValue().toString();
                        Toast.makeText(ParentDashboard.this, ""+myCurrentID, Toast.LENGTH_SHORT).show();
                        DriverLoc();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DriverLoc();

        }



    }

    private void DriverLoc()
    {
        DatabaseReference childReference = FirebaseDatabase.getInstance().getReference()
                .child("Child");

        childReference.orderByChild("ParentID").equalTo(myCurrentID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                double childLat = (double) dataSnapshot.child("ChildLatitude").getValue();
                double childLong = (double) dataSnapshot.child("ChildLongitude").getValue();
                String childName = dataSnapshot.child("ChildName").getValue().toString();

                LatLng childLoc = new LatLng(childLat,childLong);

                MarkerOptions markerChildOptions = new MarkerOptions();
                markerChildOptions.title(childName);
                markerChildOptions.snippet("Your Child is here");
                markerChildOptions.position(childLoc);
                markerChildOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mMap.addMarker(markerChildOptions);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    private Marker mDriverMarker;


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

    }















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parent_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id== R.id.addChild)
        {
            Intent intent = new Intent(this,AddMyChild.class);
            startActivity(intent);
        }
        else if (id== R.id.allChild)
        {


            Intent intent = new Intent(this,MyAllChild.class);
            intent.putExtra("ParentID",tempParentID);
            startActivity(intent);
        }


        else if (id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent1 = new Intent(this,SignIn.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
}