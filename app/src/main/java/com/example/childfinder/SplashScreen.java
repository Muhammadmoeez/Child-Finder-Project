package com.example.childfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private FirebaseUser loginUser;
    private NetworkInfo activeNetworkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        loginUser = firebaseAuth.getCurrentUser();
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        hideNavigationBar();
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (loginUser==null)
                {
                    Intent intent=new Intent(SplashScreen.this, SignIn.class);
                    startActivity(intent);
                    finish();
                }
                else
                {

                    updateUI();
                }

            }
        },5000);
    }



    @Override
    protected void onResume(){

        super.onResume();

        hideNavigationBar();
    }
    private void hideNavigationBar() {

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private void updateUI() {

        if (loginUser != null && activeNetworkInfo != null) {
            DatabaseReference parentReference = FirebaseDatabase.getInstance().getReference()
                    .child("Parent")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String status = dataSnapshot.child("Role").getValue().toString();

                        if (status.equals("Parent")) {
                            Intent intent = new Intent(SplashScreen.this, ParentDashboard.class);
                            finish();
                            startActivity(intent);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SplashScreen.this,databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        if (loginUser != null && activeNetworkInfo != null){
            DatabaseReference childReference = FirebaseDatabase.getInstance().getReference()
                    .child("Child")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            childReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String status = dataSnapshot.child("Role").getValue().toString();

                        if (status.equals("Child")) {
                            Toast.makeText(SplashScreen.this, "Welcome Child", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashScreen.this, ChildDashboard.class);
                            finish();
                            startActivity(intent);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SplashScreen.this,databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }


    }


}