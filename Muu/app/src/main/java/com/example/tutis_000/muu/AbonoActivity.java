package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import Mundo.Participante;
import Mundo.Muu;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class AbonoActivity extends Activity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
    private Participante par;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetectorCompat mDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("impr:", "entro al oncreate");
        setContentView(R.layout.activity_abonar);
        Log.d("impr:", "paso el xml");


        Intent i = getIntent();
        String nombre = i.getStringExtra("par");
        par = Muu.darInstancia().darParticipante(nombre);
        if (par != null) {
            TextView txtNombre = (TextView) findViewById(R.id.txtNombrePar);
            txtNombre.setText(par.getNombre());
        }
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

    }

    public void devolverDinero() {
        EditText txtDeuda = (EditText) findViewById(R.id.editValor);
        String Deuda = txtDeuda.getText().toString();

        if (Deuda.equals("")) {
            showDialog("Valores vacíos", "Ingrese el valor correctamente");
        } else {
            try {
                int Deudaint = Integer.parseInt(Deuda);
                if (Deudaint > 0) {
                  par.cambioDeuda(Deudaint);

                    finish();

                } else {
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para los campos");
                }

            } catch (Exception e) {
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para los campos");
            }

        }
    }
    public void abonarDeuda() {
        EditText txtDeuda = (EditText) findViewById(R.id.editValor);
        String Deuda = txtDeuda.getText().toString();

        if (Deuda.equals("")) {
            showDialog("Valores vacíos", "Ingrese el valor correctamente");
        } else {
            try {
                int Deudaint = Integer.parseInt(Deuda);
                if (Deudaint > 0) {
                    par.cambioPago(Deudaint);
                    finish();

                } else {
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para los campos");
                }

            } catch (Exception e) {
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para los campos");
            }

        }
    }
    private void showDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();


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

                    } else {
                        //onSwipeLeft();

                    }
                }
                result = true;
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    //onSwipeBottom();
                    Log.d("IMP:", "swipe bottom");
                    devolverDinero();

                } else {
                    //onSwipeTop();
                    Log.d("IMP:", "swipe up");
                    abonarDeuda();
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


        return false;
    }
}