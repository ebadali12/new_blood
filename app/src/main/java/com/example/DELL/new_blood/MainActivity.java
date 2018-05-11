package com.example.DELL.new_blood;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import android.Manifest;


public class MainActivity extends AppCompatActivity implements LocationListener {


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RadioGroup radioGroup;
    Button next_button;
    RadioButton r1;
    RadioButton r2;
    TextView t ;
    LocationManager locationManager;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);





        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);




            //checkLocationPermission();
            radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
            t = (TextView) findViewById(R.id.text);
            next_button = (Button) findViewById(R.id.next);
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the checked Radio Button ID from Radio Grou[
                    int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

                    // If nothing is selected from Radio Group, then it return -1
                    if (selectedRadioButtonID != -1) {

                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                        String selectedRadioButtonText = selectedRadioButton.getText().toString();


                        if(selectedRadioButtonText.contains("Blood center")){
                            startActivity(new Intent(MainActivity.this, BCLoginActivity.class));
                        }
                        else if(selectedRadioButtonText.contains("Donor/Request")){
                            startActivity(new Intent(MainActivity.this, splash.class));

                        }
                    }
                    else{
                        /// t.setText("Nothing selected from Radio Group.");
                        Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            
            return;
        }else{


            // Write you code here if permission already given.




            //checkLocationPermission();
            radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
            t = (TextView) findViewById(R.id.text);
            next_button = (Button) findViewById(R.id.next);
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the checked Radio Button ID from Radio Grou[
                    int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

                    // If nothing is selected from Radio Group, then it return -1
                    if (selectedRadioButtonID != -1) {

                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                        String selectedRadioButtonText = selectedRadioButton.getText().toString();

                        //t.setText(selectedRadioButtonText + " selected.");
                        if(selectedRadioButtonText.contains("Blood center")){
                            startActivity(new Intent(MainActivity.this, BCLoginActivity.class));
                        }
                        else if(selectedRadioButtonText.contains("Donor/Request")){
                            startActivity(new Intent(MainActivity.this, splash.class));
                            //Crashlytics.getInstance().crash();
                            //throw new RuntimeException("This is a crash");
                        }
                    }
                    else{
                        /// t.setText("Nothing selected from Radio Group.");
                        Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

   }




    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("GPS Permissiong")
                        .setMessage("Give location access")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, MainActivity.this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
