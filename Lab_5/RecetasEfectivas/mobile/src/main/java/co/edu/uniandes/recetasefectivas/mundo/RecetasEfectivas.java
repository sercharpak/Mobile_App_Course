package co.edu.uniandes.recetasefectivas.mundo;

import android.util.Log;

import java.util.ArrayList;

public class RecetasEfectivas {
	
	private ArrayList<Ingrediente> ingredientes;
	private ArrayList<Receta> recetas;

    public ArrayList<CantidadIngrediente> pendientes;
    public ArrayList<CantidadIngrediente> comprados;
    public ArrayList<CantidadIngrediente> faltantes;

    private static RecetasEfectivas instancia;


	public static RecetasEfectivas darInstancia(){
		if(instancia == null)
		{
			instancia = new RecetasEfectivas();
		}
		return instancia;
	}
	
	public RecetasEfectivas(){
		ingredientes= new ArrayList<Ingrediente>();
		recetas= new ArrayList<Receta>();

        pendientes= new ArrayList<CantidadIngrediente>();
        comprados= new ArrayList<CantidadIngrediente>();
        faltantes= new ArrayList<CantidadIngrediente>();

        inicializar();
	}

    private void inicializar() {
        Ingrediente tomate = new Ingrediente("Tomate", 100, 1, "gr");
        Ingrediente cebolla = new Ingrediente("Cebolla", 200, 3, "gr");
        Ingrediente ajo = new Ingrediente("Ajo", 200, 0, "gr");
        Ingrediente pasta = new Ingrediente("Pasta", 1000, 10, "gr");
        Ingrediente carne = new Ingrediente("Carne",10000,100,"gr");

        ingredientes.add(tomate);
        ingredientes.add(cebolla);
        ingredientes.add(pasta);
        ingredientes.add(carne);


        Receta pastaCarne= new Receta("Pasta con carne", "Pasta preparada con carne molida",100,4, 30,"Guarnición");
        pastaCarne.agregarCantidadIngrediente(tomate, 10);
        pastaCarne.agregarCantidadIngrediente(cebolla, 5);
        pastaCarne.agregarCantidadIngrediente(pasta, 100);
        pastaCarne.agregarCantidadIngrediente(carne, 400);
        pastaCarne.agregarCantidadIngrediente(ajo, 2);
        recetas.add(pastaCarne);
        calcularListaCompra(pastaCarne);
    }

    public ArrayList<Ingrediente> getIngredientes() {
		return ingredientes;
	}
	public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	public ArrayList<Receta> getRecetas() {
		return recetas;
	}
	public void setRecetas(ArrayList<Receta> recetas) {
		this.recetas = recetas;
	}
	
	public void agregarIngrediente(Ingrediente i){
		ingredientes.add(i);
	}
	
	public void agregarReceta(Receta r){
		recetas.add(r);
	}
	
	public Receta darReceta(String nombre){
		for (int i = 0; i < recetas.size(); i++) {
			Receta r = recetas.get(i);
			if(r.getNombre().equals(nombre)){
				return r;
			}
		}
		return null;
	}
	
	public Ingrediente darIngrediente(String nombre){
		for (int i = 0; i < ingredientes.size(); i++) {
			Ingrediente ingrediente = ingredientes.get(i);
			if(ingrediente.getNombre().equals(nombre)){
				return ingrediente;
			}
		}
		return null;
	}
	
	public String[] darListaIngredientes(){
		String[] lista= new String[ingredientes.size()];
		for(int i=0; i<ingredientes.size(); i++){
			lista[i]= ingredientes.get(i).getNombre();
		}
		return lista;
	}
	
	public String[] darListaRecetas(){
		String[] lista= new String[recetas.size()];
		for(int i=0; i<recetas.size(); i++){
			lista[i]= recetas.get(i).getNombre();
		}
		return lista;
	}

    public void calcularListaCompra(Receta r){
        ArrayList<CantidadIngrediente> cantidadIngrediente= r.getCantidades();

        for (CantidadIngrediente c : cantidadIngrediente){
            Ingrediente i= c.getIngrediente();
            int cantidad= c.getCantidad();
            if(i.getUnidadesDisponibles()< cantidad){
                CantidadIngrediente cantidadAcomprar= new CantidadIngrediente(i, cantidad-i.getUnidadesDisponibles());
                pendientes.add(cantidadAcomprar);
            }
        }
	}

	public CantidadIngrediente findAndDelete(String id) {
		CantidadIngrediente resp= null;

		for (int i=0; i<pendientes.size(); i++){

			if(pendientes.get(i).getIngrediente().getNombre().equalsIgnoreCase(id)){

				resp=pendientes.get(i);
				pendientes.remove(i);
				break;
			}
		}

		return resp;
	}

	public void addBought(CantidadIngrediente cI) {
		comprados.add(cI);
	}

	public void addMissing(CantidadIngrediente cI) {
		faltantes.add(cI);
	}

	public void addToBuy(CantidadIngrediente cI) {
		pendientes.add(cI);
	}
}
