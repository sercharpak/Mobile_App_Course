package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.mundo.Receta;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;


public class CrearRecetaActivity extends Activity {

    private Receta receta;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);
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

    private void dialogoAgregarIngredientes(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                Intent intent= new Intent(getApplicationContext(), AgregarIngredientesRecActivity.class);
                if(receta!=null){
                    intent.putExtra("receta", receta.getNombre());
                }
                startActivity(intent);
            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();

    }

    public void agregarReceta(View v){
        EditText txtNombre = (EditText)findViewById(R.id.editTextNombre);
        EditText txtDescripcion = (EditText)findViewById(R.id.editTextDescripcion);
        EditText txtCalorias=(EditText)findViewById(R.id.editTextCalorias);
        EditText txtNumPersonas=(EditText)findViewById(R.id.editTextNumPersonas);
        EditText txtTiempo=(EditText)findViewById(R.id.editTextTiempo);

        Spinner spinnerTipo = (Spinner)findViewById(R.id.spinnerTipo);

        String nombre= txtNombre.getText().toString();
        String descripcion=txtDescripcion.getText().toString();
        String tCalorias= txtCalorias.getText().toString();
        String tNumPersonas=txtNumPersonas.getText().toString();
        String tTiempo=txtTiempo.getText().toString();

        String tipo = spinnerTipo.getSelectedItem().toString();

        if(nombre.equals("") || descripcion.equals("") || tCalorias.equals("") ||tNumPersonas.equals("")
                ||tTiempo.equals("") ){
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente.");
        }
        else{
            try{
                int calorias= Integer.parseInt(tCalorias);
                int numPersonas= Integer.parseInt(tNumPersonas);
                int tiempo= Integer.parseInt(tTiempo);

                if(calorias>0 && numPersonas>0&& tiempo>0){
                    receta= new Receta(nombre, descripcion, calorias, numPersonas, tiempo, tipo);

                    RecetasEfectivas.darInstancia().agregarReceta(receta);
                    dialogoAgregarIngredientes("Receta creada", "Se agregó la receta correctamente.");
                }else{
                    showDialog("Cantidades enteras", "Por favor ingrese valores positivos para el costo y las unidades disponibles.");
                }

            }catch(Exception e){
                showDialog("Cantidades enteras", "Por favor ingrese valores correctos para las calorias, el tiempo de duración de la receta y el número de personas.");
            }
        }


    }
}
