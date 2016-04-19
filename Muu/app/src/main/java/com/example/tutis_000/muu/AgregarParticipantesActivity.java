package com.example.tutis_000.muu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//For the Date picking.

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Mundo.Vaca;
import Mundo.Participante;
import Mundo.Muu;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tutis_000 on 27/02/2016.
 */
public class AgregarParticipantesActivity extends Activity {
    /**
     * Constante que permite identificar la acción de seleccionar un contacto
     */
    private final static int ESCOGER_CONTACTO = 1;

    /**
     * Constante para crear un dialogo de error
     */
    private final static int DIALOGO_ERROR = 2;

    /**
     * Constante para crear un dialogo de envió exitoso
     */
    private final static int DIALOGO_ENVIO_OK = 3;

    /**
     * Constante para crear un dialogo de error de datos
     */
    private final static int DIALOGO_ERROR_DATOS = 4;

    /**
     * Constante para identificar la acción de escoger la fecha
     */
    private final static int ESCOGER_FECHA= 5;

    /**
     * Botón para seleccionar los contactos
     */
    private Button btContactos;

    /**
     * Botón para seleccionar la fecha
     */
    private Button btCalendario;

    /**
     * Botón para enviar la invitación
     */
    private Button btEnviar;

    /**
     * Nombre del contacto seleccionado
     */
    private String nombreContacto;

    /**
     * id del evento en el calendario
     */
    private String id_evento;

    /**
     * Numero telefónico del contacto seleccionado
     */
    private String numeroTelefonico;
    protected String nombreVaca;

    private ListView mList;
    private Muu mundo = null;

    /*
    //Manejo de DatePicker
    public int year;
    public int month;
    public int day;
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_participantes);
        Intent i = getIntent();
        nombreVaca = i.getStringExtra("vaca");
        btContactos = (Button) findViewById(R.id.button1Contacto);
        btCalendario = (Button) findViewById(R.id.button1Calendario);
        if (mundo == null) {
            mundo = Muu.darInstancia();
        }


    }

    public void mostrarSeleccionContactos(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), ESCOGER_CONTACTO);
    }

    public void mostrarSeleccionCalendario(View v) {

        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, nombreVaca);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Evento Creado por Muu para la vaca " + nombreVaca);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        startActivityForResult(calIntent, ESCOGER_FECHA);
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



    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        nombreContacto = "";
        if (resultCode == RESULT_OK) {
            if (reqCode == ESCOGER_CONTACTO) {
                Uri uriContacto = data.getData();
                if (uriContacto != null) {
                    Log.d("imp:", "el contacto no es null");
                    try {


                        String[] cols = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                        Cursor cursor = managedQuery(uriContacto, cols, null, null, null);

                        cursor.moveToFirst();
                        nombreContacto = cursor.getString(0);


                        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                        String[] columnas = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                        String seleccion = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" + nombreContacto + "'";

                        Cursor c = managedQuery(phoneUri, columnas, seleccion, null, null);


                        if (c.moveToFirst()) {
                            Log.d("impr:", "numero contacto " + c.getString(0));
                            numeroTelefonico = c.getString(0);
                            Participante participante = new Participante(nombreContacto, numeroTelefonico);
                            Log.d("impr:", "paso el new");


                            Log.d("impr:", "paso la instancia");
                            mundo.agregarParticipante(participante, nombreVaca);
                            showDialog("Participante creado", "Se agregó el participante correctamente.");

                        }

                    } catch (Exception e) {
                        numeroTelefonico = e.getMessage();
                        showDialog(DIALOGO_ERROR);
                    }
                    mList = (ListView) findViewById(R.id.listaParticipantes);
                    String[] ingredientes = Muu.darInstancia().darListaParticipantes(nombreVaca);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, ingredientes);
                    mList.setAdapter(adapter);
                    mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView parent, View view,
                                                int position, long id) {
                            // selected item
                            String nombre = ((TextView) view).getText().toString();
                            Log.d("impr:", "dio clic");

                            // Launching new Activity on selecting single List Item
                            Intent i = new Intent(getApplicationContext(), DatosParticipanteActivity.class);
                            Log.d("impr:", "paso el intent");
                            // sending data to new activity
                            i.putExtra("participante", nombre);
                            startActivity(i);
                        }

                    });


                }
            }
            id_evento = "";
            if (reqCode == ESCOGER_FECHA) {
                Uri uriFecha = data.getData();
                if (uriFecha != null) {
                    Log.d("imp:", "la fecha no es null");
                    try {


                        String[] cols = {CalendarContract.Events._ID};

                        Cursor cursor = managedQuery(uriFecha, cols, null, null, null);

                        cursor.moveToFirst();
                        id_evento = cursor.getString(0);
                        Log.d("imp:", "el id del evento asociado es: " + id_evento);
                        mundo.agregarEvento(id_evento, nombreVaca);
                    } catch (Exception e) {
                        id_evento = e.getMessage();
                        showDialog(DIALOGO_ERROR);
                    }
                }
                else{
                    Log.d("imp:", "el id del evento es: " + id_evento);
                }
            }
        }

    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOGO_ERROR:
                return crearDialogo("No fue posible recuperar la información del contacto.");
            case DIALOGO_ENVIO_OK:
                return crearDialogo("Se ha enviado el mensaje a " + nombreContacto);
            case DIALOGO_ERROR_DATOS:
                return crearDialogo("No ha seleccionado un contacto o este no tiene un número telefónico.");
        }

        return null;
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


    public void vacaFin(View v) {
        Log.d("impr:", "entro a finalizar");
        Log.d("impr:", "finalizar deuda " + mundo.todoAdeudado(nombreVaca));
        if (mundo.todoAdeudado(nombreVaca)==-1){
            showDialog("Deudaincorrecta", "La deuda es mayor que el monto de la vaca");

        }
        if (mundo.todoAdeudado(nombreVaca)==1){
            Log.d("impr:", "entro a todo adeudado");
            showDialog("Deuda incorrecta", "La deuda no cubre el monto de la vaca");

        }
        else if (mundo.todoPagado(nombreVaca)==1){
            Log.d("impr:", "entro a todo adeudado");
            showDialog("Pago incorrecta", "Los pagos no cubren el monto de la vaca");

        }
        else{
            FileOutputStream fos = null;

            try {

                fos = this.openFileOutput("vacamuu", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);

                ArrayList<Vaca> in = mundo.getVacas();
                    os.writeObject(in);
                    Log.d("Persistence", "en teoria guardó");

                os.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Agregar aqui al calendario
            //TODO Aca persiste el calendario.
            //Esta persistiendo solo.

            try {
                new HTTPPostTask().execute();
                Log.e("HTTP TEST", "HTTP SUCCESS: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent i = new Intent(this, SaldosVacaActivity.class);
            i.putExtra("nombreVaca", nombreVaca);
            startActivity(i);


        }
    }

    /*
         * Method to test Posting HTTP
         */
    class HTTPPostTask extends AsyncTask<String, Integer, String> {

        private Exception exception;
        public String longitude;
        public String latitude;

        public Response testHTTPPOST_1() throws IOException {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\"Vaca_1\",\"descripcion\":\"Descrp_1\"}");
            Request request = new Request.Builder()
                    .url("http://fast-savannah-95487.herokuapp.com/muu/vacas")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            return response;
        }

        public Response testHTTPPOST_2() throws IOException {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\""+nombreVaca+"\",\"descripcion\":\"Descrp_1\"}");
            Request request = new Request.Builder()
                    .url("http://fast-savannah-95487.herokuapp.com/muu/vacas")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            return response;
        }


        @Override
        protected String doInBackground(String[] params) {
            try {
                testHTTPPOST_1();
                testHTTPPOST_2();
                return "Success";
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
