package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import Mundo.Ingrediente;
import Mundo.Participante;
import Mundo.RecetasEfectivas;

/**
 * Created by tutis_000 on 27/02/2016.
 */
public class DatosParticipanteActivity extends Activity {
    private Participante par;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("impr:", "entro al oncreate");
        setContentView(R.layout.activity_datos_participante);
        Log.d("impr:", "paso el xml");


        Intent i = getIntent();
        String nombre = i.getStringExtra("participante");
        par = RecetasEfectivas.darInstancia().darParticipante(nombre);
        if (par != null) {
            TextView txtNombre = (TextView) findViewById(R.id.txtNombrePart);
            txtNombre.setText(par.getNombre());
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

    public void agregarSaldosParticipante(View v) {
        EditText txtDeuda = (EditText) findViewById(R.id.editDeuda);
        EditText txtPago = (EditText) findViewById(R.id.editPago);


        String Deuda = txtDeuda.getText().toString();
        String Pago = txtPago.getText().toString();


        if (Deuda.equals("") || Pago.equals("")) {
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente");
        } else {
            try {
                Log.d("imp:", "el costo " + Pago);
                int Pagoint = Integer.parseInt(Pago);
                int Deudaint = Integer.parseInt(Deuda);


                if (Pagoint > 0 && Deudaint > 0) {
                    par.setPago(Pagoint);
                    par.setDeuda(Deudaint);
                    Log.d("impr:", "los datos del participante" + par.getDeuda() + " " + par.getPago());

                    showDialog("Datos Agregados", "Se agregaron los saldos al participante");

                    finish();
                } else {
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para los campos");
                }

            } catch (Exception e) {
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para los campos");
            }

        }

    }
}
