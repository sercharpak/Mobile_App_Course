package com.example.tutis_000.muu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
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


public class MainActivity extends Activity
        implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private GestureDetectorCompat mDetector;

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

            mDetector = new GestureDetectorCompat(this,this);
            mDetector.setOnDoubleTapListener(this);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("IMP:", "Down");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("IMP:", "long press");
        Intent intent = new Intent(this, ModificarVacaActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        //onSwipeRight();
                        Intent intent = new Intent(Intent.ACTION_EDIT, Events.CONTENT_URI);
                        startActivity(intent);
                    } else {
                        //onSwipeLeft();
                        Intent intent = new Intent(this, MapActivity.class);
                        startActivity(intent);
                    }
                }
                result = true;
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    //onSwipeBottom();
                } else {
                    //onSwipeTop();
                }
            }
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {



        Intent intent = new Intent(this, CrearVacaActivity.class);
        startActivity(intent);

        return false;
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