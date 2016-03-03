package com.example.tutis_000.muu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Mundo.Ingrediente;
import Mundo.RecetasEfectivas;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class ModificarVacaActivity extends Activity {
    private RecetasEfectivas mundo = null;
    private ListView mList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("imp:", "llego al oncreate");
        setContentView(R.layout.activity_modificar_saldos);
        Log.d("imp:", "llego al xml ");
        try {
            FileInputStream fis = this.openFileInput("vacamuu");

            Log.d("impr:", "hay file");
            ObjectInputStream is = new ObjectInputStream(fis);

            ArrayList<Ingrediente> simpleClass = (ArrayList<Ingrediente>) is.readObject();
            mundo = RecetasEfectivas.darInstancia();
            for(int i =0; i<simpleClass.size();i++) {

                if(!mundo.hayIngrediente(simpleClass.get(i).getNombre())) {
                    mundo.agregarIngrediente(simpleClass.get(i));
                    Log.d("impr", "hay " + simpleClass.get(i).getNombre());
                }
            }
            is.close();
            fis.close();
        }
        catch(Exception e){

        }

        mList= (ListView) findViewById(R.id.listaVacas);
        String[] ingredientes= mundo.darListaIngredientes();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String nombre = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), SaldosModificarActivity.class);
                // sending data to new activity
                i.putExtra("nombreVaca", nombre);
                startActivity(i);
            }

        });
    }
    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

}
