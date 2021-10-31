package com.example.childfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddMyChild extends AppCompatActivity implements OnMapReadyCallback {

    //Location
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String parentCurrentID;

    DatabaseReference parentRefForFetchParentData;

    ImageView regArrowBackChild;

    EditText regChildName, regChildEmail, regChildPassword, regChildConfirmPassword, regChildPhone;
    CheckBox checkBoxChild;
    Button saveChildBtn;

    String parentEmailReLogIn, parentPasswordReLogIn, parentRoleReLogIn, parentCurrentIDReLogIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_child);

        regArrowBackChild = (ImageView) findViewById(R.id.arrowBackAddChild);
        regArrowBackChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        parentCurrentID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        regChildName = (EditText) findViewById(R.id.childName);
        regChildEmail = (EditText) findViewById(R.id.childEmail);
        regChildPassword = (EditText) findViewById(R.id.childPassword);
        regChildConfirmPassword = (EditText) findViewById(R.id.childConfirmPassword);

        checkBoxChild = (CheckBox) findViewById(R.id.childCheckBox);
        checkBoxChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    regChildPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    regChildConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    regChildPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    regChildConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        regChildPhone = (EditText) findViewById(R.id.childPhone);

        saveChildBtn = (Button) findViewById(R.id.childSignUpWithEmail);
        saveChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parentData();
                insertData();


            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddMyChild.this);
        fetchLastLocation();


    }

    private void parentData() {

        parentRefForFetchParentData = FirebaseDatabase.getInstance().getReference()
                .child("Parent")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        parentRefForFetchParentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                parentEmailReLogIn = snapshot.child("UserEmail").getValue().toString();
                parentPasswordReLogIn = snapshot.child("UserPassword").getValue().toString();
                parentRoleReLogIn = snapshot.child("Role").getValue().toString();
                parentCurrentIDReLogIn = snapshot.child("UserID").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


    private void fetchLastLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {


                if (location != null){

                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.childLocationPoint);
                    supportMapFragment.getMapAsync(AddMyChild.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
      //  Toast.makeText(this, "latLng "+latLng, Toast.LENGTH_SHORT).show();

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);

    }

    private void insertData() {

        final String registerChildName = regChildName.getText().toString();
        final String registerChildEmail = regChildEmail.getText().toString();
        final String registerChildPassword = regChildPassword.getText().toString();
        final String registerChildConfirmPassword = regChildConfirmPassword.getText().toString();
        final String registrationChildPhone = regChildPhone.getText().toString();

        if (TextUtils.isEmpty(registerChildName)){
            regChildName.requestFocus();
            regChildName.setError("Enter Name");
        }
        else if (TextUtils.isEmpty(registerChildEmail)){

            regChildEmail.requestFocus();
            regChildEmail.setError("Enter Email");
        }
        else if (TextUtils.isEmpty(registerChildPassword)){

            regChildPassword.requestFocus();
            regChildPassword.setError("Enter Password");
        }
        else if (TextUtils.isEmpty(registerChildConfirmPassword)){

            regChildConfirmPassword.requestFocus();
            regChildConfirmPassword.setError("Re-Enter Password");

        }
        else if (!registerChildPassword.matches(registerChildConfirmPassword)){
            regChildConfirmPassword.requestFocus();
            regChildConfirmPassword.setError("Your Password is not Matched");

        }
        else if (TextUtils.isEmpty(registrationChildPhone)){
            regChildPhone.requestFocus();
            regChildPhone.setError("Enter Phone Number");

        }
        else if (!registrationChildPhone.matches("\\+[0-9]{10,13}$")){

            regChildPhone.requestFocus();
            regChildPhone.setError("+923329882570");
        }

        else{

            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(registerChildEmail, registerChildPassword)
                    .addOnCompleteListener(AddMyChild.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                HashMap insertNewChildData = new HashMap();
                                insertNewChildData.put("ChildName",registerChildName);
                                insertNewChildData.put("ChildEmail",registerChildEmail);
                                insertNewChildData.put("ChildPassword",registerChildPassword);
                                insertNewChildData.put("ChildConfirmPassword",registerChildConfirmPassword);
                                insertNewChildData.put("ChildNumber",registrationChildPhone);
                                insertNewChildData.put("Role","Child");
                                insertNewChildData.put("ParentID",parentCurrentID);
                                insertNewChildData.put("ChildID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                insertNewChildData.put("ChildLongitude",currentLocation.getLongitude());
                                insertNewChildData.put("ChildLatitude",currentLocation.getLatitude());


                                FirebaseDatabase.getInstance().getReference().child("Child")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(insertNewChildData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull  Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    Toast.makeText(AddMyChild.this, "Child Registration", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                    Intent intent = new Intent(AddMyChild.this, ParentDashboard.class);
                                                    intent.putExtra("ParentID",parentCurrentID);

                                                    startActivity(intent);






                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AddMyChild.this, "RealTime Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(AddMyChild.this, "Auth Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}