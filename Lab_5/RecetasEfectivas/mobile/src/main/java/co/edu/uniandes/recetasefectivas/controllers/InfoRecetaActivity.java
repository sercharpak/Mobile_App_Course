package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.mundo.Receta;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;

public class InfoRecetaActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_receta);

        Intent i = getIntent();
        String nombre = i.getStringExtra("receta");
        Receta receta= RecetasEfectivas.darInstancia().darReceta(nombre);

        if(receta!=null){
            TextView txtNombre= (TextView)findViewById(R.id.txtNombre);
            txtNombre.setText(receta.getNombre());

            TextView txtDescripcion= (TextView)findViewById(R.id.txtDescripcion);
            txtDescripcion.setText(receta.getDescripcion());

            TextView txtCalorias= (TextView)findViewById(R.id.txtCalorias);
            txtCalorias.setText(""+receta.getCalorias());

            TextView txtTiempo= (TextView)findViewById(R.id.txtTiempo);
            txtTiempo.setText(""+receta.getTiempo());

            TextView txtNumPersonas= (TextView)findViewById(R.id.txtNumPersonas);
            txtNumPersonas.setText(""+receta.getNumeroPersonas());

            TextView txtTipo= (TextView)findViewById(R.id.txtTipo);
            txtTipo.setText(""+receta.getTipo());

            Spinner spinnerIngredientes =(Spinner)findViewById(R.id.spinnerIngredientes);
            String[] ingredientes= receta.darListaIngredientes();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ingredientes);
            spinnerIngredientes.setAdapter(adapter);

            Spinner spinnerRecetas =(Spinner)findViewById(R.id.spinnerRecetas);
            String[] recetas= receta.darListaRecetas();
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,recetas);
            spinnerRecetas.setAdapter(adapter);
        }
    }

}
