package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;

public class VerIngredientesActivity extends Activity {

    private ListView mList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ingredientes);

        mList= (ListView) findViewById(R.id.listaIngredientes);
        String[] ingredientes= RecetasEfectivas.darInstancia().darListaIngredientes();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String ingrediente = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), InfoIngredienteActivity.class);
                // sending data to new activity
                i.putExtra("ingrediente", ingrediente);
                startActivity(i);
            }

        });
    }

}