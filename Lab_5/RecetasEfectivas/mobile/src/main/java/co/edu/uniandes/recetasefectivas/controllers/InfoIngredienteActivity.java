package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.mundo.Ingrediente;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;


public class InfoIngredienteActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ingrediente);

        Intent i = getIntent();
        String nombre = i.getStringExtra("ingrediente");
        Ingrediente ing= RecetasEfectivas.darInstancia().darIngrediente(nombre);

        if(ing!=null){
            TextView txtNombre= (TextView)findViewById(R.id.txtNombre);
            txtNombre.setText(ing.getNombre());

            TextView txtCosto= (TextView)findViewById(R.id.txtCostoUnitario);
            txtCosto.setText(""+ing.getCostoUnitario());
            TextView txtUnidadesDsip= (TextView)findViewById(R.id.txtUnidadesDisponibles);
            txtUnidadesDsip.setText(""+ing.getUnidadesDisponibles());
            TextView txtUnidadesMedida= (TextView)findViewById(R.id.txtUnidadesMedida);
            txtUnidadesMedida.setText(ing.getUnidadMedida());
        }
    }

}
