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

import Mundo.RecetasEfectivas;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class SaldosModificarActivity extends Activity {
    private RecetasEfectivas mundo = null;

    private ListView mList;
    private String nombreVaca;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saldos_modificar);
        mundo = RecetasEfectivas.darInstancia();
        Intent i = getIntent();
        nombreVaca = i.getStringExtra("nombreVaca");
        Log.d("impr","vaca"+nombreVaca);
        mList = (ListView) findViewById(R.id.listaSaldosVacasGuardadas);
        RecetasEfectivas mundo =RecetasEfectivas.darInstancia();
        String[] ingredientes = mundo.darListaParticipantesconDeuda(nombreVaca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes );
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String nombrePart = ((TextView) view).getText().toString();
                String[] result = nombrePart.split(":");
                String nombre = result[0];

                Log.d("impr:", "dio clic");

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), AbonoActivity.class);
                Log.d("impr:", "paso el intent");
                // sending data to new activity
                i.putExtra("par", nombre);
                startActivity(i);
            }
        });
    }
}
