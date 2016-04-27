package co.edu.uniandes.recetasefectivas.mundo;

import java.util.ArrayList;

public class Receta {
	private String nombre;
	private String descripcion;
	private int calorias;
	private int numeroPersonas;
	private int tiempo;
	private String tipo;
	private ArrayList<String> invitados;
	private ArrayList<CantidadIngrediente> cantidades;
	private ArrayList<Receta> ingredRecetas;
	
	public Receta(String nombre, String descripcion, int calorias, int numeroPersonas, int tiempo,
			String tipo){
		this.nombre=nombre;
		this.descripcion=descripcion;
		this.calorias=calorias;
		this.numeroPersonas=numeroPersonas;
		this.tiempo=tiempo;
		this.tipo=tipo;
		invitados= new ArrayList<String>();
		cantidades= new ArrayList<CantidadIngrediente>();
		ingredRecetas= new ArrayList<Receta>();
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
	public int getCalorias() {
		return calorias;
	}
	public void setCalorias(int calorias) {
		this.calorias = calorias;
	}
	public int getNumeroPersonas() {
		return numeroPersonas;
	}
	public void setNumeroPersonas(int numeroPersonas) {
		this.numeroPersonas = numeroPersonas;
	}
	public int getTiempo() {
		return tiempo;
	}
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ArrayList<String> getInvitados() {
		return invitados;
	}
	public void setInvitados(ArrayList<String> invitados) {
		this.invitados = invitados;
	}
	
	public void agregarInvitado(String nombre){
		invitados.add(nombre);
	}


	public ArrayList<CantidadIngrediente> getCantidades() {
		return cantidades;
	}


	public void setCantidades(ArrayList<CantidadIngrediente> cantidades) {
		this.cantidades = cantidades;
	}
	
	public void agregarCantidadIngrediente(Ingrediente i, int cantidad){
		CantidadIngrediente ci= new CantidadIngrediente(i,cantidad);
		cantidades.add(ci);
	}


	public ArrayList<Receta> getIngredRecetas() {
		return ingredRecetas;
	}


	public void setIngredRecetas(ArrayList<Receta> ingredRecetas) {
		this.ingredRecetas = ingredRecetas;
	}
	
	public void agregarReceta(Receta r){
		ingredRecetas.add(r);
	}
	
	public String toString(){
		return tipo+":"+nombre;
	}
	
	public String[] darListaIngredientes(){
		String[] lista= new String[cantidades.size()];
		for(int i=0; i<cantidades.size(); i++){
			CantidadIngrediente cantIng=cantidades.get(i);
			lista[i]= cantIng.getIngrediente().getNombre()+"- Cantidad:"+cantIng.getCantidad();
		}
		return lista;
	}
	
	public String[] darListaRecetas(){
		String[] lista= new String[ingredRecetas.size()];
		for(int i=0; i<ingredRecetas.size(); i++){
			lista[i]= ingredRecetas.get(i).getNombre();
		}
		return lista;
	}
	
	public double darCosto(){
		double costo=0;
		for(int i=0; i<cantidades.size();i++){
			CantidadIngrediente ci= cantidades.get(i);
			costo=costo+ci.getCantidad()*ci.getIngrediente().getCostoUnitario();
		}
		
		for (int i=0; i<ingredRecetas.size();i++){
			Receta re= ingredRecetas.get(i);
			costo=costo+re.darCosto();
		}
		
		return costo;
	}
	
	public int darTiempoPreparacion(){
		int tiempoPrep= tiempo;
		for (int i=0; i<ingredRecetas.size();i++){
			Receta re= ingredRecetas.get(i);
			tiempoPrep=tiempoPrep+re.darTiempoPreparacion();
		}
		return tiempoPrep;
		
	}
}
