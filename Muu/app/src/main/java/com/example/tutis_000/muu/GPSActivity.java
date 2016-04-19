package com.example.tutis_000.muu;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import Mundo.GPS_Tracker;

/**
 * Clase creada para probar la correcta recopilacion del GPS
 * Basada en: http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
 */
public class GPSActivity extends Activity {

    Button btnShowLocation;

    // GPSTracker class
    GPS_Tracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("impr:", "entro al oncreate del GPSActivity");
        setContentView(R.layout.activity_gps);
        Log.d("impr:", "paso el xml del GPSActivity");

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPS_Tracker(GPSActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }
/*
//Send the info to the server.
    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, MIN_DISTANCE, locationListener);

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            final HttpClient httpclient = new DefaultHttpClient();
            final HttpPost httppost = new HttpPost("http://www.yourremoteserver.com/mywebservice.php");

            try {
                final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Latitude", String.valueOf(location.getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("Longitude", String.valueOf(location.getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("username", "Sergio");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
            }
            catch (final ClientProtocolException e) { e.printStackTrace(); }
            catch (final IOException e) { e.printStackTrace(); }
        }
*/
}