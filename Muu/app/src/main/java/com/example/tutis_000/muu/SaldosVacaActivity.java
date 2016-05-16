package com.example.tutis_000.muu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import Mundo.Muu;
import Mundo.Participante;

/**
 * Created by tutis_000 on 28/02/2016.
 */
public class SaldosVacaActivity extends Activity {

    private Muu mundo = null;

    private ListView mList;
    private String nombreVaca;
    private ShakeListener mShaker;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeDetector;
    private Participante par=null;
    private String numeroTelefonico;
    private String[] ingredientes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saldos);

        mundo = Muu.darInstancia();
        Intent i = getIntent();
        nombreVaca = i.getStringExtra("nombreVaca");
        Log.d("impr","vaca"+nombreVaca);
        mList = (ListView) findViewById(R.id.listaSaldos);
        mundo = Muu.darInstancia();
         ingredientes = mundo.darListaParticipantesconDeuda(nombreVaca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes );
        mList.setAdapter(adapter);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeListener();
        Log.d("imp:", "listener");
        mShakeDetector.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                //  handleShakeEvent(count);
                Log.d("imp:", "shake");
                Log.d("imp:", "contactos ="+ingredientes.length);
                for(int i=0;i<ingredientes.length;i++){
                    Log.d("imp:", "entro for =");
                  //imp for  ingredientes[i]
                    Log.d("imp:", "lista" + ingredientes[i]);
                    String[] ing=ingredientes[i].split(":");


                    Log.d("imp:", "lista transform"+ing[0]);
                    par = mundo.darParticipante(ing[0]);
                    Log.d("imp:", "contacto ="+ par.getNombre());
                    numeroTelefonico= par.getNumero();

                    if( numeroTelefonico!= null ) {
                        Log.d("imp:", "num ="+ par.getNumero());

                        String mensaje = "Debes a la vaca " +nombreVaca+": "+ par.getDebe();
                        SmsManager manejador = SmsManager.getDefault();
                        try {
                            manejador.sendTextMessage(numeroTelefonico, null, mensaje, null, null);
                            Log.d("SMS", "Se envió el mensaje correctamente");
                        } catch (Exception e) {
                            Log.d("SMS", "No se envió el mensaje, excepción: " + e.getMessage());
                        }

                    }

                }
                Toast.makeText(getApplicationContext(), "Se han enviado los mensajes recordatorios!" , Toast.LENGTH_LONG).show();

            }
        });



        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String nombrePart = ((TextView) view).getText().toString();
                String[] result = nombrePart.split(":");
                String nombre = result[0];

                Log.d("impr:", "dio clic");

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), MensajeActivity.class);
                Log.d("impr:", "paso el intent");
                // sending data to new activity
                i.putExtra("par", nombre);
                startActivity(i);
            }
        });
    }
   // public void saldos(View v){

     //   }

    public void volveraMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    }




