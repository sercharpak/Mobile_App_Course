package co.edu.uniandes.recetasefectivas.mundo;

public class Ingrediente {
	
	private String nombre;
	
	private int costoUnitario;
	
	private int unidadesDisponibles;
	
	private String unidadMedida;

	public Ingrediente(String nombre, int costoUnitario, int unidadesDisponibles, String unidadMedida){
		this.nombre=nombre;
		this.costoUnitario=costoUnitario;
		this.unidadesDisponibles=unidadesDisponibles;
		this.unidadMedida=unidadMedida;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCostoUnitario() {
		return costoUnitario;
	}

	public void setCostoUnitario(int costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public int getUnidadesDisponibles() {
		return unidadesDisponibles;
	}

	public void setUnidadesDisponibles(int unidadesDisponibles) {
		this.unidadesDisponibles = unidadesDisponibles;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	
}
