package Mundo;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecetasEfectivas {

    private ArrayList<Ingrediente> ingredientes;


    private static RecetasEfectivas instancia;

    public static RecetasEfectivas darInstancia() {
        if (instancia == null) {
            instancia = new RecetasEfectivas();
        }
        return instancia;
    }

    public RecetasEfectivas() {
        ingredientes = new ArrayList<Ingrediente>();
        //TODO leer storage aquí!!

    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }


    public void agregarIngrediente(Ingrediente i) {
        ingredientes.add(i);
    }

    public void agregarParticipante(Participante p, String nombrevaca) {
        Log.d("impr:", "llega aregar");
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombrevaca)) {
                ingrediente.agregarParticipante(p);
            }
        }

    }



    public Ingrediente darIngrediente(String nombre) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                return ingrediente;
            }
        }
        return null;
    }

    public boolean hayIngrediente(String nombre) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public Participante darParticipante(String nombre) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Participante[] p = ingredientes.get(i).darListaParticipantes();
            for (int j = 0; j < p.length; j++)

            {
                Participante ingrediente = p[j];
                if (ingrediente.getNombre().equals(nombre)) {
                    return ingrediente;
                }
            }
        }
        return null;
    }

    public String[] darListaIngredientes() {
        String[] lista = new String[ingredientes.size()];
        for (int i = 0; i < ingredientes.size(); i++) {
            lista[i] = ingredientes.get(i).getNombre();
        }
        return lista;
    }

    public int darMontoVaca(String nombre) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                return ingrediente.getCostototal();
            }

        }
        return 0;

    }

    public int darMontoRecogido(String nombre) {

        int recogido=0;
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                Participante[] lista = ingrediente.darListaParticipantes();
              for(int j=0;j<lista.length;j++){
                  recogido+=lista[j].getDeuda();
                  Log.d("impr:", "deuda de participante" + j + " = " + ingrediente.darListaParticipantes()[j].getDeuda());
              }
            }

        }
        return recogido;
    }
    public int darPagoRecogido(String nombre) {

        int recogido=0;
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                Participante[] lista = ingrediente.darListaParticipantes();
                for(int j=0;j<lista.length;j++){
                    recogido+=lista[j].getPago();
                    Log.d("impr:", "pago de participante" + j + " = " + ingrediente.darListaParticipantes()[j].getDeuda());
                }
            }

        }
        return recogido;
    }



    public int todoAdeudado(String nombre){
        int deuda=0;
        if(darMontoVaca(nombre)<darMontoRecogido(nombre)){
            deuda=-1;
        }
        if(darMontoVaca(nombre)>darMontoRecogido(nombre)){
            deuda=1;
        }
        return deuda;
    }

    public int todoPagado(String nombre){
        int deuda=0;
        if(darMontoVaca(nombre)<darPagoRecogido(nombre)){
            deuda=-1;
        }
        if(darMontoVaca(nombre)>darPagoRecogido(nombre)){
            deuda=1;
        }
        return deuda;
    }

    public String[] darListaParticipantes(String nombrevaca) {
        String[] lista = null;
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombrevaca)) {
                Log.d("impr:", "nombre de la vaca:" + ingrediente.getNombre());
                lista = new String[ingredientes.get(i).getParticipantes().size()];
                for (int j = 0; j < ingredientes.get(i).getParticipantes().size(); j++) {
                    lista[j] = ingredientes.get(i).getParticipantes().get(j).getNombre();
                    Log.d("impr:", "participante:" + ingredientes.get(i).getParticipantes().get(j).getNombre());
                }
            }
        }
        return lista;
    }

    public String[] darListaParticipantesconDeuda(String nombrevaca) {
        String[] lista = null;
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombrevaca)) {
                Log.d("impr:", "nombre de la vaca:" + ingrediente.getNombre());
                lista = new String[ingredientes.get(i).getParticipantes().size()];
                for (int j = 0; j < ingredientes.get(i).getParticipantes().size(); j++) {
                    lista[j] = ingredientes.get(i).getParticipantes().get(j).getNombre()+":                     "+ingredientes.get(i).getParticipantes().get(j).getDebe();
                    Log.d("impr:", "participante:" + ingredientes.get(i).getParticipantes().get(j).getNombre());
                }
            }
        }
        return lista;
    }




    public void agregarEvento(String id_evento, String nombreVaca) {
        Log.d("impr:", "Esta agregando el evento");
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ingrediente = ingredientes.get(i);
            if (ingrediente.getNombre().equals(nombreVaca)) {
                ingrediente.setId_evento(id_evento);
            }
        }
    }
}


	
	

