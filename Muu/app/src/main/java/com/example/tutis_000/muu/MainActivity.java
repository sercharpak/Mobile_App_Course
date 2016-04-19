package com.example.tutis_000.muu;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.facebook.FacebookSdk;
/*
import com.facebook.FacebookCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//Imports for the Messenger
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
*/
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import android.util.Base64;
import android.widget.Toast;
import Mundo.Muu;


public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("impr", "aja");
        Muu mundo = Muu.darInstancia();
        try {
            super.onCreate(savedInstanceState);
            //Initialize Facebook SDK
            FacebookSdk.sdkInitialize(this.getApplicationContext());
            setContentView(R.layout.activity_main);
            Log.d("Main Create", "Cargo el Facebook");
            //Service GPS
            startService(new Intent(this, GPS_Service.class));
            Log.d("Main Create", "Cargo el GPS Service");

    } catch (Exception e) {
        e.printStackTrace();
    }

        /*
        //To get the Key for Facebook Develop
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(
                    "com.example.tutis_000.muu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Toast.makeText(this.getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



/*
    public void loginFacebook (View v){
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { ... });
    }
*/
    public void agregarVaca(View v) {

        Intent intent = new Intent(this, CrearVacaActivity.class);
        startActivity(intent);
    }
    public void modificarVaca(View v) {

        Intent intent = new Intent(this, ModificarVacaActivity.class);
        startActivity(intent);
    }
    public void verCalendario(View v) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, Events.CONTENT_URI, this, VerVacaActivity.class);
        Intent intent = new Intent(Intent.ACTION_EDIT, Events.CONTENT_URI);
        startActivity(intent);
    }

    public void gpsActivity(View v) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, Events.CONTENT_URI, this, VerVacaActivity.class);
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }
    public void mapActivity(View v) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, Events.CONTENT_URI, this, VerVacaActivity.class);
        try {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


/*
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        try {
            //To get the log time.
            AppEventsLogger.activateApp(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    catch (Exception e){
        e.printStackTrace();
    }
    }
*/
}