/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g01.muu.models;

/**
 * Class created to manage the "cows"
 * @author Sergio
 */
public class VacaDTO 
{
    
    private Long id;
    
    private String nombre;
    
    private String descripcion;
    

    public VacaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
     
    
}
