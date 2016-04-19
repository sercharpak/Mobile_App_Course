package com.example.tutis_000.muu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * La clase GetCommentsTask representa una tarea asincrona que realizará
 * las operaciones de red necesarias en segundo plano para obtener la informacion.
 * Created by shernand on 4/18/16.
 */


public class HTTPActivity extends ActionBarActivity {

    /*
    Lista de comentarios
     */
    ListView comments;

    /*
    Cliente para la conexión al servidor
     */
    HttpURLConnection con;


    /*
    URL del POST
     */
    protected static final String URL_POST = "http://fast-savannah-95487.herokuapp.com/muu/vacas";

    /*
    URL del GET
     */
    protected static final String URL_GET = "http://fast-savannah-95487.herokuapp.com/muu/vacas";
    /*
    Mensaje
     */
    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Obtener la instancia de la lista
        //comments = (ListView)findViewById(R.id.CommentsList);
        Intent i = getIntent();
        comment = i.getStringExtra("mensaje");

        if (comment==null || comment.compareTo("")==0)
            Toast.makeText(this, "Escriba un comentario",Toast.LENGTH_LONG).show();
        else {

            /*
            Comprobar la disponibilidad de la Red
             */
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // Iniciar Tarea asícrona
                try {
                    new PostCommentTask().
                            execute(
                                    new URL(URL_POST));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Finalizar actividad
                finish();
            }
            else{
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            try {
                new GetCommentsTask().
                        execute(
                                new URL(URL_GET);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }
*/

    /*
    La clase GetCommentsTask representa una tarea asincrona que realizará
    las operaciones de red necesarias en segundo plano para obtener toda la
    lista de comentarios alojada en el servidor.
     */
    public class GetCommentsTask extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {

            List<String> comments = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    comments = new ArrayList<>();
                    comments.add("El recurso no está disponible");
                    return comments;
                }
                else{

                    /*
                    Parsear el flujo con formato JSON a una lista de Strings
                    que permitan crean un adaptador
                     */
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JSONCommentsParser parser = new JSONCommentsParser();

                    comments = parser.readJsonStream(in);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return comments;

        }

        @Override
        protected void onPostExecute(List<String> s) {

            /*
            Se crea un adaptador con el el resultado del parsing
            que se realizó al arreglo JSON
             */
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    s);

            // Relacionar adaptador a la lista
            comments.setAdapter(adapter);
        }
    }

    public void onSend () throws MalformedURLException {

        // Obtener el valor del comentario
        //comment = body.getText().toString();

        /*
        Se comprueba la validez del comentario para no tener
        valores irregulares
         */
        if (comment==null || comment.compareTo("")==0)
            Toast.makeText(this, "Escriba un comentario",Toast.LENGTH_LONG).show();
        else {

            /*
            Comprobar la disponibilidad de la Red
             */
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // Iniciar Tarea asícrona
                new PostCommentTask().
                        execute(
                                new URL(URL_POST));

                // Finalizar actividad
                finish();
            }
            else{
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        }


    }

    /*
    La clase PostCommentTask permite enviar los datos hacia el servidor
    para guardar el comentario del usuario.
     */
    public class PostCommentTask extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {
                // Construir los datos a enviar
                String data = "body=" + URLEncoder.encode(comment, "UTF-8");

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                // Tamaño previamente conocido
                con.setFixedLengthStreamingMode(data.getBytes().length);

                // Establecer application/x-www-form-urlencoded debido al formato clave-valor
                con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                /*
                POST /muu/vacas HTTP/1.1
                Host: fast-savannah-95487.herokuapp.com
                Content-Type: application/json
                Cache-Control: no-cache
                Postman-Token: e2967948-7080-cc7b-8316-2368ecf5a8aa

                {"nombre":"Vaquita","descripcion":"Descripcioncita"}
                 */
                /*
                OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"nombre\":\"Vaquita\",\"descripcion\":\"Descripcioncita\"}");
Request request = new Request.Builder()
  .url("http://fast-savannah-95487.herokuapp.com/muu/vacas")
  .post(body)
  .addHeader("content-type", "application/json")
  .addHeader("cache-control", "no-cache")
  .addHeader("postman-token", "8908eacb-f0c2-4294-0ec9-2a1efb3048d7")
  .build();

Response response = client.newCall(request).execute();
                 */
                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(data.getBytes());
                out.flush();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con!=null)
                    con.disconnect();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void s) {
            Toast.makeText(getBaseContext(), "Comentario posteado", Toast.LENGTH_LONG).show();
        }
    }
}