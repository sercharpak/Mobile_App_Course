package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.mundo.Ingrediente;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;


public class CrearIngredienteActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ingrediente);
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();

    }

    public void agregarIngrediente(View v){
        EditText txtNombre = (EditText)findViewById(R.id.editTextNombre);
        EditText txtCosto = (EditText)findViewById(R.id.editCostoUnit);
        EditText txtUnidadesDisponibles = (EditText)findViewById(R.id.editUnidadesDisp);
        Spinner spinnerUnidadesMedida = (Spinner)findViewById(R.id.spinnerUnidadesMed);

        String nombre= txtNombre.getText().toString();
        String stCosto= txtCosto.getText().toString();
        String stUnidadesDis=txtUnidadesDisponibles.getText().toString();
        String unidadMedida = spinnerUnidadesMedida.getSelectedItem().toString();

        if(nombre.equals("") || stCosto.equals("") || stUnidadesDis.equals("") ||unidadMedida.equals("") ){
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente.");
        }
        else{
            try{
                int intCosto = Integer.parseInt(stCosto);
                int intUnidadesDisp= Integer.parseInt(stUnidadesDis);

                if(intCosto>0 && intUnidadesDisp>0){
                    Ingrediente ingrediente = new Ingrediente(nombre, intCosto, intUnidadesDisp, unidadMedida);
                    RecetasEfectivas.darInstancia().agregarIngrediente(ingrediente);
                    showDialog("Ingrediente creado", "Se agregó el ingrediente correctamente.");
                }else{
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para el costo y las unidades disponibles.");
                }

            }catch(Exception e){
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para el costo y las unidades disponibles.");
            }
        }


    }
}