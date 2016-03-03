package Mundo;

import java.io.Serializable;

/**
 * Created by tutis_000 on 27/02/2016.
 */
public class Participante implements Serializable {
    private String nombre;

    private String numero;

    private int deuda;

    private int pago;

    private int debe;


    public Participante(String nombre, String costoUnitario) {
        this.nombre = nombre;
        this.numero = costoUnitario;
        this.deuda = 0;
        this.pago = 0;
        this.debe=0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDeuda() {
        return deuda;
    }

    public int getPago() {
        return pago;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setDeuda(int deuda) {
        this.deuda = deuda;
    }

    public void setPago(int deuda) {
        this.pago = deuda;
    }

    public int getDebe(){
        return pago-deuda;
    }
    public void cambioPago(int pago2){
        pago+=pago2;
    }

}
