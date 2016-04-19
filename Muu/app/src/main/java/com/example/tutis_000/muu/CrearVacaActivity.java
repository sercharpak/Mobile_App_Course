package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import Mundo.Muu;
import Mundo.Vaca;

public class CrearVacaActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("imp:", "llego al oncreate");
        setContentView(R.layout.activity_crear_ingrediente);
        Log.d("imp:", "llego al xml ");
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

    public void agregarUnaVaca(View v) {
        EditText txtNombre = (EditText) findViewById(R.id.editTextNombre);
        EditText txtCostoTotal = (EditText) findViewById(R.id.editCostoTotal);


        String nombre = txtNombre.getText().toString();
        String stCosto = txtCostoTotal.getText().toString();


        if (nombre.equals("") || stCosto.equals("")) {
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente");
        } else {
            try {
                Log.d("imp:", "el costo " + stCosto);
                int intCosto = Integer.parseInt(stCosto);


                if (intCosto > 0) {
                    Vaca vaca = new Vaca(nombre, intCosto);
                    Muu.darInstancia().agregarVaca(vaca);
                    showDialog("Vaca creada", "Se agregó la vaca correctamente.");
                    Intent intent = new Intent(this, AgregarParticipantesActivity.class);
                    intent.putExtra("vaca", nombre);
                    startActivity(intent);

                } else {
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para el costo");
                }

            } catch (Exception e) {
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para el costo");
            }


        }


    }
}