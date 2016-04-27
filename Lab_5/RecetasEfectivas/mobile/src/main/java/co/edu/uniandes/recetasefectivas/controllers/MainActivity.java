package co.edu.uniandes.recetasefectivas.controllers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import co.edu.uniandes.recetasefectivas.R;
import co.edu.uniandes.recetasefectivas.helpers.Constants;
import co.edu.uniandes.recetasefectivas.mundo.CantidadIngrediente;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;

public class MainActivity extends AppCompatActivity   {

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeGoogleApiClient();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Random r = new Random();
                buildWearableOnlyNotification("Hola", "Hola Watch!"+r.nextInt(),
                        Constants.WATCH_ONLY_PATH);
            }
        });

        renderProducts();

    }

    private void initializeGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            renderProducts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void buildWearableOnlyNotification(String title, String content, String path) {
        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
            putDataMapRequest.getDataMap().putString(Constants.KEY_CONTENT, content);
            putDataMapRequest.getDataMap().putString(Constants.KEY_TITLE, title);
            PutDataRequest request = putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            if (!dataItemResult.getStatus().isSuccess()) {
                                Log.e("Test", "buildWatchOnlyNotification(): Failed to set the data, "
                                        + "status: " + dataItemResult.getStatus().getStatusCode());
                            }else{
                                Log.e("Test", "buildWatchOnlyNotification(): Data sent "
                                        + "status: " + dataItemResult.getStatus().getStatusCode());
                            }
                        }
                    });
        } else {
            Log.e("Test", "buildWearableOnlyNotification(): no Google API Client connection");
        }
    }

    public void renderProducts()
    {
        RecetasEfectivas re = RecetasEfectivas.darInstancia();
        CantidadIngrediente_adapter adapter =
                new CantidadIngrediente_adapter(this,re.pendientes);

        ListView list = (ListView)findViewById(R.id.productsList);

        list.setAdapter(adapter);

        CantidadIngrediente_adapter adapter1 =
                new CantidadIngrediente_adapter(this,re.comprados);

        ListView list1 = (ListView)findViewById(R.id.boughtList);

        list1.setAdapter(adapter1);

        CantidadIngrediente_adapter adapter2 =
                new CantidadIngrediente_adapter(this,re.faltantes);

        ListView list2 = (ListView)findViewById(R.id.missingList);

        list2.setAdapter(adapter2);
    }

    private class CantidadIngrediente_adapter  extends ArrayAdapter {

        Activity context;
        ArrayList<CantidadIngrediente> products;

        public CantidadIngrediente_adapter(Activity context, ArrayList<CantidadIngrediente> products) {
            super(context, R.layout.cantidad_element,products );
            this.context = context;
            this.products = products;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.cantidad_element, null);

            TextView element_name = (TextView) item.findViewById(R.id.element_name);
            TextView element_price = (TextView) item.findViewById(R.id.element_price);

            element_name.setText(products.get(position).getIngrediente().getNombre());
            String moneyString = products.get(position).getCantidad()+" "+products.get(position).getIngrediente().getUnidadMedida();
            element_price.setText(moneyString);

            return (item);
        }
    }


}
