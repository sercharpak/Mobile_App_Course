package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Mundo.Ingrediente;
import Mundo.Participante;
import Mundo.RecetasEfectivas;

/**
 * Created by shernand on 3/3/16.
 */
public class VerVacaActivity extends Activity {
    private RecetasEfectivas mundo = null;

    private String nombreVaca;
    private String id_evento;
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vaca);
        mundo = RecetasEfectivas.darInstancia();
        Intent ind = getIntent();
        Uri uriEvents = ind.getData();
        if (uriEvents != null) {
            Log.d("imp:", "el evento no es null");
            try {
                String[] cols = {CalendarContract.Events._ID};

                Cursor cursor = managedQuery(uriEvents, cols, null, null, null);

                cursor.moveToFirst();
                id_evento = cursor.getString(0);
                ArrayList<Ingrediente> ingredientes = mundo.getIngredientes();
                for (int i = 0; i < ingredientes.size(); i++) {
                    Ingrediente ingrediente = ingredientes.get(i);
                    if (ingrediente.getId_evento().equals(id_evento)) {
                        nombreVaca = ingrediente.getNombre();
                        llenarCampos(ingrediente);
                    }
                }
            } catch (Exception e) {

                crearDialogo("Error recuperando el evento del calendario.");
            }
        }
    }

    private void llenarCampos(Ingrediente ing)
    {
        TextView txt_nombre = (TextView) findViewById(R.id.text_Nombre);
        TextView text_valor = (TextView) findViewById(R.id.text_valor);
        txt_nombre.setText(nombreVaca);
        text_valor.setText(ing.getCostototal());
        mList = (ListView) findViewById(R.id.listaParticipantes);
        String[] nombres_participantes = mundo.darListaParticipantes(nombreVaca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, nombres_participantes );
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                //Por ahora no hace nada si hace clic.
                /*
                // selected item
                String nombrePart = ((TextView) view).getText().toString();
                String[] result = nombrePart.split(":");
                String nombre = result[0];

                Log.d("impr:", "dio clic");

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), MensajeActivity.class);
                Log.d("impr:", "paso el intent");
                // sending data to new activity
                i.putExtra("par", nombre);
                startActivity(i);
                */
            }
        });


    }

    private Dialog crearDialogo(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }

    public void editar_Vaca(View v){
        // Launching new Activity on selecting single List Item
        Intent i = new Intent(getApplicationContext(), SaldosModificarActivity.class);
        // sending data to new activity
        i.putExtra("nombreVaca", nombreVaca);
        startActivity(i);
    }

}
