package com.example.tutis_000.muu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.GregorianCalendar;

import Mundo.Ingrediente;
import Mundo.RecetasEfectivas;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("impr", "aja");
        RecetasEfectivas mundo = RecetasEfectivas.darInstancia();
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void agregarVaca(View v) {

        Intent intent = new Intent(this, CrearVacaActivity.class);
        startActivity(intent);
    }
    public void modificarVaca(View v) {

        Intent intent = new Intent(this, ModificarVacaActivity.class);
        startActivity(intent);
    }
    public void verCalendario(View v) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, Events.CONTENT_URI, this, VerVacaActivity.class);
        Intent intent = new Intent(Intent.ACTION_EDIT, Events.CONTENT_URI);
        startActivity(intent);
    }




}