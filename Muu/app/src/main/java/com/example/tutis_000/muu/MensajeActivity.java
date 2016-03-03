package com.example.tutis_000.muu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import Mundo.Participante;
import Mundo.RecetasEfectivas;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class MensajeActivity extends Activity {
    Participante par=null;
    private String numeroTelefonico;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("impr:", "entro al oncreate Mensaje");
        setContentView(R.layout.activity_mensaje);
        Log.d("impr:", "paso el xml Mensaje");


        Intent i = getIntent();
        String nombre = i.getStringExtra("par");
        Log.d("impr:","participante "+ nombre);
        par = RecetasEfectivas.darInstancia().darParticipante(nombre);

        Log.d("impr:","participante "+ par);
        if (par != null) {
            TextView txtNombre = (TextView) findViewById(R.id.txtNombreP);
            txtNombre.setText(nombre);
            numeroTelefonico=par.getNumero();
            Log.d("impr:", "numero "+numeroTelefonico);
        }
    }

    public void enviarSMS(View v) {

        if( numeroTelefonico!= null ) {



            String mensaje = "Debes a la vaca: " + par.getDebe();
            SmsManager manejador = SmsManager.getDefault();
            try {
                manejador.sendTextMessage(numeroTelefonico, null, mensaje, null, null);
                Log.d("SMS", "Se envió el mensaje correctamente");
            } catch (Exception e) {
                Log.d("SMS", "No se envió el mensaje, excepción: " + e.getMessage());
            }

        }
    }


}
