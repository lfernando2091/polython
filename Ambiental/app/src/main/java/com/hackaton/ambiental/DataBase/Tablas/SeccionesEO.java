package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Colums;
import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SeccionesEO extends Component implements Serializable{

	private static final long serialVersionUID = -5365301582295385319L;
	private Integer id;
	private String nombre;
	private String descripcion;
	private ContentValues v;

	public SeccionesEO() {
	}

	public SeccionesEO(Integer id, String nombre, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "SeccionesEO [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public ContentValues contentValues() {
		v=new ContentValues();
		v.put(Colums._ID,get_id());
		v.put(Colums.COLUMN_NOMBRE,getNombre());
		v.put(Colums.COLUMN_DESCRIPCION,getDescripcion());
		return v;
	}

	@Override
	public Component JSONValues(JSONObject jsonObject) throws JSONException {
		return new SeccionesEO(
				jsonObject.optInt("id"),
				jsonObject.optString("nombre"),
				jsonObject.optString("descripcion"));
	}

	@Override
	public String tableName() {
		return Colums.TABLE_SECCIONES;
	}
}
