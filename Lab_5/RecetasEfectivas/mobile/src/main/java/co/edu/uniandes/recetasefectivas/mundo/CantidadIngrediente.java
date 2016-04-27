package co.edu.uniandes.recetasefectivas.mundo;

import org.json.JSONException;
import org.json.JSONObject;

public class CantidadIngrediente {
	
	private Ingrediente ingrediente;
	private int cantidad;
	
	public CantidadIngrediente(Ingrediente ingrediente, int cantidad){
		this.ingrediente=ingrediente;
		this.cantidad=cantidad;
	}
	
	public Ingrediente getIngrediente() {
		return ingrediente;
	}
	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", this.ingrediente.getNombre());
		json.put("name", this.ingrediente.getNombre());
		json.put("price", this.cantidad);
		json.put("unit", this.ingrediente.getUnidadMedida());
		return json;
	}

}
