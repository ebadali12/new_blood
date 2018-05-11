package com.example.DELL.new_blood;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by DELL on 2/24/2018.
 */
public class map_fragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Button hospital;
    Button blood;
    private int PROXIMITY_RADIUS = 10000;
    double lat;
    double lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment_layout, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        hospital = (Button) rootView.findViewById(R.id.hospital_btn);
        blood = (Button) rootView.findViewById(R.id.blood_btn);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;


                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.






                    return;
                }

                googleMap.setMyLocationEnabled(true);


//                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//                Criteria criteria = new Criteria();
//                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//                if (location != null)
//                {
//                    lat = location.getLatitude();
//                    lng = location.getLongitude();
//                }




                SingleShotLocationProvider.requestSingleUpdate(getActivity(),
                        new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                ///Log.d("Location", "my location is " + location.toString());
                                lat = location.latitude;
                                lng = location.longitude;






                                // For dropping a marker at a point on the Map
                                LatLng sydney = new LatLng(lat,lng);
                                googleMap.addMarker(new MarkerOptions().position(sydney).title("MY LOCATION").snippet(""));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }


                        });


                hospital.setOnClickListener(new View.OnClickListener() {
                    String Restaurant = "hospital";
                    @Override
                    public void onClick(View v) {
                        Log.d("onClick", "Button is Clicked");
                        googleMap.clear();
                        String url = getUrl(lat, lng, Restaurant);
                        Object[] DataTransfer = new Object[2];
                        DataTransfer[0] = googleMap;
                        DataTransfer[1] = url;
                        Log.d("onClick", url);
                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(DataTransfer);
                        Toast.makeText(getContext(),"Nearby hospitals", Toast.LENGTH_LONG).show();
                    }
                });

                blood.setOnClickListener(new View.OnClickListener() {
                    String Restaurant = "blood bank";
                    @Override
                    public void onClick(View v) {
                        Log.d("onClick", "Button is Clicked");
                        googleMap.clear();
                        String url = getUrl(lat, lng, Restaurant);
                        Object[] DataTransfer = new Object[2];
                        DataTransfer[0] = googleMap;
                        DataTransfer[1] = url;
                        Log.d("onClick", url);
                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(DataTransfer);
                        Toast.makeText(getContext(),"Nearby blood banks", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return rootView;
    }
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&keyword=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDMaYguby-b9-1a7DOIIPCbAW7E_Azzo8c");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}