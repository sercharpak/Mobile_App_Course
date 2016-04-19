package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import Mundo.Participante;
import Mundo.Muu;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class AbonoActivity extends Activity {
    private Participante par;

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

    }

    public void devolverDinero(View v) {
        EditText txtDeuda = (EditText) findViewById(R.id.editValor);
        String Deuda = txtDeuda.getText().toString();

        if (Deuda.equals("")) {
            showDialog("Valores vacíos", "Ingrese el valor correctamente");
        } else {
            try {
                int Deudaint = Integer.parseInt(Deuda);
                if (Deudaint > 0) {
                    par.cambioPago(-Deudaint);
                   finish();

                } else {
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para los campos");
                }

            } catch (Exception e) {
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para los campos");
            }

        }
    }
    public void abonarDeuda(View v) {
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
}