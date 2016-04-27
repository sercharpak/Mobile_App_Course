package co.edu.uniandes.recetasefectivas.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import co.edu.uniandes.recetasefectivas.mundo.Receta;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;
import co.edu.uniandes.recetasefectivas.R;

public class CalcularCostosActivity extends Activity {

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
     * Botón para seleccionar los contactos
     */
    private Button btContactos;

    /**
     * Botón para enviar la invitación
     */
    private Button btEnviar;

    /**
     * Nombre del contacto seleccionado
     */
    private String nombreContacto;

    /**
     * Numero telefónico del contacto seleccionado
     */
    private String numeroTelefonico;

    /**
     * Spinner para la selección de las comidas
     */
    private Spinner spinerEntradas;
    private Spinner spinerFuertes;
    private Spinner spinerGuarnicion;
    private Spinner spinerPostres;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_costos);

        ArrayList<String> entradas= new ArrayList<String>();
        ArrayList<String> fuertes= new ArrayList<String>();
        ArrayList<String> guarnicion= new ArrayList<String>();
        ArrayList<String> postres= new ArrayList<String>();
        ArrayList<Receta> recetas= RecetasEfectivas.darInstancia().getRecetas();
        for(int i=0; i<recetas.size(); i++){
            Receta re= recetas.get(i);
            if(re.getTipo().equals("Entrada Fria") || re.getTipo().equals("Ensalada") || re.getTipo().equals("Sopa")|| re.getTipo().equals("Entrada Caliente")){
                entradas.add(re.getNombre());
            }else if(re.getTipo().equals("Carne")|| re.getTipo().equals("Pescado") || re.getTipo().equals("Pollo")){
                fuertes.add(re.getNombre());
            }else if(re.getTipo().equals("Guarnición")){
                guarnicion.add(re.getNombre());
            }else if(re.getTipo().equals("Postre")){
                postres.add(re.getNombre());
            }

        }

        String [] aEntradas= entradas.toArray(new String[entradas.size()]);
        String [] aFuertes= fuertes.toArray(new String[fuertes.size()]);
        String [] aGuarnicion= guarnicion.toArray(new String[guarnicion.size()]);
        String [] aPostres= postres.toArray(new String[postres.size()]);

        spinerEntradas= (Spinner) findViewById(R.id.spinnerEntradas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,aEntradas);
        spinerEntradas.setAdapter(adapter);

        spinerFuertes= (Spinner) findViewById(R.id.spinnerFuerte);
        ArrayAdapter<String> adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,aFuertes);
        spinerFuertes.setAdapter(adapterF);

        spinerGuarnicion= (Spinner) findViewById(R.id.spinnerGuarnicion);
        ArrayAdapter<String> adapterG = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,aGuarnicion);
        spinerGuarnicion.setAdapter(adapterG);

        spinerPostres= (Spinner) findViewById(R.id.spinnerPostre);
        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,aPostres);
        spinerPostres.setAdapter(adapterP);

        btContactos= (Button)findViewById(R.id.button1Contacto);
        btEnviar=(Button)findViewById(R.id.button1Enviar);
    }

    public void calcularCostos(View v){
        try{
            String nomEntrada=spinerEntradas.getSelectedItem().toString();
            String nomFuerte=spinerFuertes.getSelectedItem().toString();
            String nomGuarnicion=spinerGuarnicion.getSelectedItem().toString();
            String nomPostre=spinerPostres.getSelectedItem().toString();

            if(nomEntrada.equals("") || nomFuerte.equals("") || nomGuarnicion.equals("") || nomPostre.equals("")){
                showDialog("Faltan platos de la comida ", "Una comida solo se puede formar con un plato de cada uno de los tipos mostrados.");
            }else{
                RecetasEfectivas re= RecetasEfectivas.darInstancia();
                String[] nombres= new String[4];
                nombres[0]=nomEntrada;
                nombres[1]=nomFuerte;
                nombres[2]=nomGuarnicion;
                nombres[3]=nomPostre;
                int calorias=0;
                double costo=0;
                int tiempo=0;
                for(int i=0; i< nombres.length;i++){
                    Receta entrada = re.darReceta(nomEntrada);
                    if(entrada!=null){
                        calorias= calorias +entrada.getCalorias();
                        costo= costo + entrada.darCosto();
                        if(tiempo<entrada.darTiempoPreparacion()){
                            tiempo=entrada.darTiempoPreparacion();
                        }
                    }
                }

                showDialog("Costos", "Los costos son: \n Tiempo:"+ tiempo+"min\n Costo: "+ costo+"pesos \n Calorias:"+calorias);

            }
        }catch(Exception e){
            showDialog("Faltan platos de la comida ", "Una comida solo se puede formar con un plato de cada uno de los tipos mostrados. Faltan algunos y por ello no se puede calcular");
        }
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

    /**
     * Envía un mensaje de texto al contacto seleccionado
     */
    public void enviarSMS(View v) {
        if(numeroTelefonico != null ){
            try{
                String nomEntrada=spinerEntradas.getSelectedItem().toString();
                String nomFuerte=spinerFuertes.getSelectedItem().toString();
                String nomGuarnicion=spinerGuarnicion.getSelectedItem().toString();
                String nomPostre=spinerPostres.getSelectedItem().toString();

                if(nomEntrada.equals("") || nomFuerte.equals("") || nomGuarnicion.equals("") || nomPostre.equals("")){
                    showDialog("Faltan platos de la comida ", "Una comida solo se puede formar con un plato de cada uno de los tipos mostrados.");
                }else{
                    String mensaje = "Te quiero invitar a una comida compuesta por: "+ nomEntrada+"," + nomFuerte+","+nomGuarnicion+","+nomPostre;
                    SmsManager manejador = SmsManager.getDefault();
                    try {
                        manejador.sendTextMessage(numeroTelefonico, null, mensaje, null, null);
                        Log.i("SMS", "Se envió el mensaje correctamente");
                    } catch (Exception e) {
                        Log.i("SMS", "No se envió el mensaje, excepción: "+e.getMessage());
                    }
                    showDialog(DIALOGO_ENVIO_OK);
                }
            }catch(Exception e){
                showDialog("Faltan platos de la comida ", "Una comida solo se puede formar con un plato de cada uno de los tipos mostrados.");
            }


        }else{
            showDialog(DIALOGO_ERROR_DATOS);
        }
    }

    /**
     * Muestra la actividad de selección de contactos invocando las funciones del dispositivo
     */
    public void mostrarSeleccionContactos(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI),ESCOGER_CONTACTO);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        nombreContacto = "";
        if(resultCode == RESULT_OK){
            if(reqCode == ESCOGER_CONTACTO){
                Uri uriContacto = data.getData();
                if(uriContacto != null ){
                    try {
                        String[] cols = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                        Cursor cursor =  managedQuery(uriContacto, cols, null, null, null);
                        cursor.moveToFirst();
                        nombreContacto = cursor.getString(0);

                        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                        String[] columnas = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                        String seleccion = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" + nombreContacto + "'";
                        Cursor c = managedQuery(phoneUri,columnas,seleccion,null, null );
                        if(c.moveToFirst()){
                            numeroTelefonico = c.getString(0);
                        }

                    } catch (Exception e) {
                        numeroTelefonico = e.getMessage();
                        showDialog(DIALOGO_ERROR);
                    }
                    btContactos.setText(nombreContacto + "(Cambiar)");
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

    /**
     * Crea un dialogo con el mensaje que llega por parámetro
     * @param mensaje el mensaje que se desea desplegar
     * @return el dialogo con el mensaje a mostrar
     */
    private Dialog crearDialogo(String mensaje){
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

}
