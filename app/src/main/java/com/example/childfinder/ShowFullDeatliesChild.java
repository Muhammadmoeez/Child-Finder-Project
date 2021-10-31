package com.example.childfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ShowFullDeatliesChild extends AppCompatActivity implements OnMapReadyCallback {


    ImageView regArrowBackChild;


    String tempChildName;
    String tempChildEmail;
    String tempChildPassword;
    String tempChildConfirmPassword;
    String tempChildNumber;
    String tempChildRole;
    String tempChildParentID;
    String tempChildID;
    Double tempChildLongitude;
    Double tempChildLatitude;


    TextView fullChildName, fullChildEmail, fullChildNumber;

    //SelectedPointDetailsForMapDeclaration
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;
    GoogleMap map;

    Double  receiveLatitude, receiveLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_deatlies_child);

        regArrowBackChild = (ImageView) findViewById(R.id.arrowBackShowFullChild);
        regArrowBackChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        tempChildName = intent.getStringExtra("ChildName");
        tempChildEmail = intent.getStringExtra("ChildEmail");
        tempChildPassword = intent.getStringExtra("ChildPassword");
        tempChildConfirmPassword = intent.getStringExtra("ChildConfirmPassword");
        tempChildNumber = intent.getStringExtra("ChildNumber");
        tempChildRole = intent.getStringExtra("Role");
        tempChildParentID = intent.getStringExtra("ParentID");
        tempChildID = intent.getStringExtra("ChildID");
        tempChildLongitude = intent.getDoubleExtra("ChildLongitude",0.0);
        tempChildLatitude = intent.getDoubleExtra("ChildLatitude",0.0);






        fullChildName = (TextView) findViewById(R.id.showFullChildName);
        fullChildEmail = (TextView) findViewById(R.id.showFullChildEmail);
        fullChildNumber = (TextView) findViewById(R.id.showFullChildNumber);



        fullChildName.setText(tempChildName);
        fullChildEmail.setText(tempChildEmail);
        fullChildNumber.setText(tempChildNumber);

        //SelectedPointDetailsForMapInitialization
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fetchLastLocation();




    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    /// currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.showFullChildLocationPoint);
                    supportMapFragment.getMapAsync(ShowFullDeatliesChild.this);
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


        LatLng latLng = new LatLng(tempChildLatitude, tempChildLongitude);

        MarkerOptions options = new MarkerOptions();
        options.title(String.valueOf(tempChildName));
        options.snippet(String.valueOf(tempChildEmail));
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(options);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
       // MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.dark);
        //map.setMapStyle(mapStyleOptions);
    }
}