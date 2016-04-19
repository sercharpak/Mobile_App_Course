package com.example.tutis_000.muu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class created to manage the service of the location
 * Based on: http://stackoverflow.com/questions/28535703/best-way-to-get-user-gps-location-in-background-in-android
 * Created by shernand on 4/18/16.
 */
public class GPS_Service extends Service
{
    private static final String TAG = "This is a GPS Test";
    private LocationManager mLocationManager = null;
    //private static final int LOCATION_INTERVAL = 1000;
    //private static final float LOCATION_DISTANCE = 10f;
    // The minimum distance to change Updates in meters
    private static final long LOCATION_DISTANCE = 1; // 10 meters

    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final long LOCATION_INTERVAL = 1000 * 300 * 1; // 5 minutes

    private static int TEST = 5; //Send 5 messages
    private int test_count = 0;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            String[] params = new String[2];
            String latitude = location.getLatitude()+"";
            String longitude = location.getLongitude()+"";
            params[0] = longitude;
            params[1] = latitude;
            mLastLocation.set(location);
            if(test_count<TEST){
                try {
                    new HTTPPostTask().execute(params);
                    Log.e("HTTP TEST", "HTTP SUCCESS: ");
                    test_count++;
                } catch (Exception e) {
                    e.printStackTrace();
                    test_count++;
                }

            }
        }

        /*
         * Method to test Posting HTTP
         */
        class HTTPPostTask extends AsyncTask<String, Integer, String> {

            private Exception exception;
            public String longitude;
            public String latitude;

            public Response testHTTPPOST_1() throws IOException {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\"Vaquita\",\"descripcion\":\"Descripcioncita\"}");
                Request request = new Request.Builder()
                        .url("http://fast-savannah-95487.herokuapp.com/muu/vacas")
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();
                return response;
            }
            public Response testHTTPPOST_2() throws IOException {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, "{\"tipo\":\"Participante\",\"username\":\"user1\", \"longitude\":\""+longitude+"\",\"latitude\":\""+latitude+"\"}");
            Request request = new Request.Builder()
                    .url("http://fast-savannah-95487.herokuapp.com/muu/tiposUsuario")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

                Response response = client.newCall(request).execute();
                return response;
            }

            @Override
            protected String doInBackground(String[] params) {
                try {
                    longitude = params[0];
                    latitude = params [1];
                    testHTTPPOST_1();
                    testHTTPPOST_2();
                    return "Success";
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }






        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                }
                catch (SecurityException e){
                    Log.i(TAG, "fail to remove location listners, ignore", e);
                }
                catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    //Metods from GPS_Tracker

}