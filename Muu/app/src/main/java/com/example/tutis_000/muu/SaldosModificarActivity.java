package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Mundo.Muu;
import Mundo.Vaca;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class SaldosModificarActivity extends Activity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
    private Muu mundo = null;

    private ListView mList;
    protected String nombreVaca;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private GestureDetectorCompat mDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saldos_modificar);
        mundo = Muu.darInstancia();
        Intent i = getIntent();
        nombreVaca = i.getStringExtra("nombreVaca");
        Log.d("impr","vaca"+nombreVaca);
        mList = (ListView) findViewById(R.id.listaSaldosVacasGuardadas);
        Muu mundo = Muu.darInstancia();
        String[] ingredientes = mundo.darListaParticipantesconDeuda(nombreVaca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes );
        mList.setAdapter(adapter);
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String nombrePart = ((TextView) view).getText().toString();
                String[] result = nombrePart.split(":");
                String nombre = result[0];

                Log.d("impr:", "dio clic");

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), AbonoActivity.class);
                Log.d("impr:", "paso el intent");
                // sending data to new activity
                i.putExtra("par", nombre);
                startActivity(i);
            }
        });
    }

    public void vacaFin(View v) {
        Log.d("impr:", "entro a finalizar");
        Log.d("impr:", "finalizar deuda " + mundo.todoAdeudado(nombreVaca));
        if (mundo.todoAdeudado(nombreVaca)==-1){
            showDialog("Deudaincorrecta", "La deuda es mayor que el monto de la vaca");

        }
        if (mundo.todoAdeudado(nombreVaca)==1){
            Log.d("impr:", "entro a todo adeudado");
            showDialog("Deuda incorrecta", "La deuda no cubre el monto de la vaca");

        }
        else if (mundo.todoPagado(nombreVaca)==1){
            Log.d("impr:", "entro a todo adeudado");
            showDialog("Pago incorrecta", "Los pagos no cubren el monto de la vaca");

        }
        else{
            FileOutputStream fos = null;

            try {

                fos = this.openFileOutput("vacamuu", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);

                ArrayList<Vaca> in = mundo.getVacas();
                os.writeObject(in);
                Log.d("Persistence", "en teoria guardó");

                os.close();
                fos.close();


                try {
                    new HTTPPostTask().execute();
                    Log.e("HTTP TEST", "HTTP SUCCESS: ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
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
            RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\"Vaca_1\",\"descripcion\":\"Descrp_1\"}");
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
            RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\""+nombreVaca+"\",\"descripcion\":\"Descrp_1\"}");
            Request request = new Request.Builder()
                    .url("http://fast-savannah-95487.herokuapp.com/muu/vacas")
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


        return false;
    }
}
