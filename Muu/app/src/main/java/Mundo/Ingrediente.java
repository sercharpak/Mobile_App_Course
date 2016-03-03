package Mundo;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Ingrediente implements Serializable {

    private String nombre;

    private String id_evento;

    private int costototal;

    private ArrayList<Participante> participantes;


    public Ingrediente(String nombre, int costoUnitario) {
        this.nombre = nombre;
        this.costototal = costoUnitario;
        participantes = new ArrayList<Participante>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCostototal() {
        return costototal;
    }

    public ArrayList<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Participante> ingredientes) {
        this.participantes = ingredientes;
    }

    public void agregarParticipante(Participante p) {
        Log.d("impr:", "llega aregar");

        participantes.add(p);
        for (int i = 0; i < participantes.size(); i++) {
            Log.d("impr:", "la lista actual es:" + i + " " + participantes.get(i).getNombre());
        }
    }

    public Participante darParticipante(String nombre) {
        for (int i = 0; i < participantes.size(); i++) {
            Participante ingrediente = participantes.get(i);
            if (ingrediente.getNombre().equals(nombre)) {
                return ingrediente;
            }
        }
        return null;
    }

    public Participante[] darListaParticipantes() {
        Participante[] lista = new Participante[participantes.size()];
        for (int i = 0; i < participantes.size(); i++) {
            lista[i] = participantes.get(i);
        }
        return lista;
    }


    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }
}
