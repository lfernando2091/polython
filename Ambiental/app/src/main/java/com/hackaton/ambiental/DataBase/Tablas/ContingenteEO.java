package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Colums;
import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ContingenteEO extends Component implements Serializable{
	private static final long serialVersionUID = 2681394202288126485L;
	private Integer id;
	private String unidadMedida;
	private Integer UnidadMedidaValor;
	private String nombre;
	private String descripcion;
	private String acciones;
	private ContentValues v;

	@Override
	public String toString() {
		return "ContingenteEO [id=" + id + ", unidadMedida=" + unidadMedida + ", UnidadMedidaValor=" + UnidadMedidaValor
				+ ", nombre=" + nombre + ", descripcion=" + descripcion + ", acciones=" + acciones + "]";
	}

	public ContingenteEO() {
	}

	public ContingenteEO(Integer id, String unidadMedida, Integer unidadMedidaValor, String nombre, String descripcion, String acciones) {
		this.id = id;
		this.unidadMedida = unidadMedida;
		UnidadMedidaValor = unidadMedidaValor;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.acciones = acciones;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Integer getUnidadMedidaValor() {
		return UnidadMedidaValor;
	}

	public void setUnidadMedidaValor(Integer unidadMedidaValor) {
		UnidadMedidaValor = unidadMedidaValor;
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

	public String getAcciones() {
		return acciones;
	}

	public void setAcciones(String acciones) {
		this.acciones = acciones;
	}


	@Override
	public ContentValues contentValues() {
		v=new ContentValues();
		v.put(Colums._ID,get_id());
		v.put(Colums.COLUMN_UNIDAD_MEDIDA,getUnidadMedida());
		v.put(Colums.COLUMN_UNIDAD_MEDIDA_VALOR,getUnidadMedidaValor());
		v.put(Colums.COLUMN_NOMBRE,getNombre());
		v.put(Colums.COLUMN_DESCRIPCION,getDescripcion());
		v.put(Colums.COLUMN_ACCIONES,getAcciones());
		return v;
	}

	@Override
	public Component JSONValues(JSONObject jsonObject) throws JSONException {
		return new ContingenteEO(
				jsonObject.optInt("id"),
				jsonObject.optString("unidadMedida"),
				jsonObject.optInt("unidadMedidaValor"),
				jsonObject.optString("nombre"),
				jsonObject.optString("descripcion"),
				jsonObject.optString("acciones"));
	}

	@Override
	public String tableName() {
		return Colums.TABLE_CONTINGENTE;
	}
}
