package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Colums;
import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GruposEO extends Component implements Serializable{

	private static final long serialVersionUID = -4315911946690406771L;
	private Integer id;
	private Integer idSeccion;
	private Integer idContacto;
	private ContentValues v;

	public GruposEO() {
	}

	public GruposEO(Integer id, Integer idSeccion, Integer idContacto) {
		this.id = id;
		this.idSeccion = idSeccion;
		this.idContacto = idContacto;
	}

	@Override
	public String toString() {
		return "GruposEO [id=" + id + ", idSeccion=" + idSeccion + ", idContacto=" + idContacto + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(Integer idSeccion) {
		this.idSeccion = idSeccion;
	}

	public Integer getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}

	@Override
	public ContentValues contentValues() {
		v=new ContentValues();
		v.put(Colums._ID,get_id());
		v.put(Colums.COLUMN_ID_SECCION,getIdSeccion());
		v.put(Colums.COLUMN_ID_CONTACTO,getIdContacto());
		return v;
	}

	@Override
	public Component JSONValues(JSONObject jsonObject) throws JSONException {
		return null;
	}

	@Override
	public String tableName() {
		return Colums.TABLE_GRUPOS;
	}
}
