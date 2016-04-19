package com.example.tutis_000.muu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Clase para mostrar en un mapa la ubicacion.
 * Basado en: http://stackoverflow.com/questions/15142089/how-to-display-my-location-on-google-maps-for-android-api-v2
 * Y en: https://github.com/architek360/HealthCareApp-1/blob/master/dAlert/src/com/example/dalert/MapActivity.java
 * Created by shernand on 4/18/16.
 */

public class MapActivity extends FragmentActivity implements LocationListener  {


    GoogleMap googleMap;
    LatLng myPosition;
    static boolean check = true;
    static ArrayList<CircleOptions> list=new ArrayList<CircleOptions>();

    // add all necessary things

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);

            // if  Google Play Services are available then
            initilizeMap();
            /*
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Getting latitude of the current location
                double latitude = location.getLatitude();

                // Getting longitude of the current location
                double longitude = location.getLongitude();

                // Creating a LatLng object for the current location
                LatLng latLng = new LatLng(latitude, longitude);
                myPosition = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(myPosition).title("You are here"));

                //plotPoint(list);
            }
            */
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public void plotPoint (ArrayList<CircleOptions> list){
        for (CircleOptions l:list){
            googleMap.addCircle(l);
        }
//		googleMap.
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            try {
                googleMap.setMyLocationEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null) {
                    // Getting latitude of the current location
                    double latitude = location.getLatitude();

                    // Getting longitude of the current location
                    double longitude = location.getLongitude();

                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(latitude, longitude);

                    myPosition = new LatLng(latitude, longitude);

                    googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(myPosition)      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }


            }
            catch(SecurityException e){
                e.printStackTrace();
            }
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}