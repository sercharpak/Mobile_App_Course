package Mundo;

import android.util.Log;

import java.util.ArrayList;

public class Muu {

    private ArrayList<Vaca> vacas;


    private static Muu instancia;

    public static Muu darInstancia() {
        if (instancia == null) {
            instancia = new Muu();
        }
        return instancia;
    }

    public Muu() {
        vacas = new ArrayList<Vaca>();
        //TODO leer storage aquí!!

    }

    public ArrayList<Vaca> getVacas() {
        return vacas;
    }

    public void setVacas(ArrayList<Vaca> vacas) {
        this.vacas = vacas;
    }


    public void agregarVaca(Vaca i) {
        vacas.add(i);
    }

    public void agregarParticipante(Participante p, String nombrevaca) {
        Log.d("impr:", "llega aregar");
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombrevaca)) {
                vaca.agregarParticipante(p);
            }
        }

    }



    public Vaca darVaca(String nombre) {
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombre)) {
                return vaca;
            }
        }
        return null;
    }

    public boolean hayVaca(String nombre) {
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public Participante darParticipante(String nombre) {
        for (int i = 0; i < vacas.size(); i++) {
            Participante[] p = vacas.get(i).darListaParticipantes();
            for (int j = 0; j < p.length; j++)

            {
                Participante participante = p[j];
                if (participante.getNombre().equals(nombre)) {
                    return participante;
                }
            }
        }
        return null;
    }

    public String[] darListaVacas() {
        String[] lista = new String[vacas.size()];
        for (int i = 0; i < vacas.size(); i++) {
            lista[i] = vacas.get(i).getNombre();
        }
        return lista;
    }

    public int darMontoVaca(String nombre) {
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombre)) {
                return vaca.getCostototal();
            }

        }
        return 0;

    }

    public int darMontoRecogido(String nombre) {

        int recogido=0;
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombre)) {
                Participante[] lista = vaca.darListaParticipantes();
              for(int j=0;j<lista.length;j++){
                  recogido+=lista[j].getDeuda();
                  Log.d("impr:", "deuda de participante" + j + " = " + vaca.darListaParticipantes()[j].getDeuda());
              }
            }

        }
        return recogido;
    }
    public int darPagoRecogido(String nombre) {

        int recogido=0;
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombre)) {
                Participante[] lista = vaca.darListaParticipantes();
                for(int j=0;j<lista.length;j++){
                    recogido+=lista[j].getPago();
                    Log.d("impr:", "pago de participante" + j + " = " + vaca.darListaParticipantes()[j].getDeuda());
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
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombrevaca)) {
                Log.d("impr:", "nombre de la vaca:" + vaca.getNombre());
                lista = new String[vacas.get(i).getParticipantes().size()];
                for (int j = 0; j < vacas.get(i).getParticipantes().size(); j++) {
                    lista[j] = vacas.get(i).getParticipantes().get(j).getNombre();
                    Log.d("impr:", "participante:" + vacas.get(i).getParticipantes().get(j).getNombre());
                }
            }
        }
        return lista;
    }

    public String[] darListaParticipantesconDeuda(String nombrevaca) {
        String[] lista = null;
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombrevaca)) {
                Log.d("impr:", "nombre de la vaca:" + vaca.getNombre());
                lista = new String[vacas.get(i).getParticipantes().size()];
                for (int j = 0; j < vacas.get(i).getParticipantes().size(); j++) {
                    lista[j] = vacas.get(i).getParticipantes().get(j).getNombre()+":                     "+ vacas.get(i).getParticipantes().get(j).getDebe();
                    Log.d("impr:", "participante:" + vacas.get(i).getParticipantes().get(j).getNombre());
                }
            }
        }
        return lista;
    }




    public void agregarEvento(String id_evento, String nombreVaca) {
        Log.d("impr:", "Esta agregando el evento");
        for (int i = 0; i < vacas.size(); i++) {
            Vaca vaca = vacas.get(i);
            if (vaca.getNombre().equals(nombreVaca)) {
                vaca.setId_evento(id_evento);
            }
        }
    }
}


	
	

