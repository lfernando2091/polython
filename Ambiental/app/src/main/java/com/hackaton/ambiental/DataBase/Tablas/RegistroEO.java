package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Colums;
import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;

public class RegistroEO extends Component implements Serializable{

	private static final long serialVersionUID = -5720150316347981811L;
	private Integer id;
	private Integer idContingente;
	private Integer idGrupos;
	private Date fecha;
	private Boolean informado;
	private ContentValues v;

	public RegistroEO() {
	}

	public RegistroEO(Integer id, Integer idContingente, Integer idGrupos, Date fecha, Boolean informado) {
		this.id = id;
		this.idContingente = idContingente;
		this.idGrupos = idGrupos;
		this.fecha = fecha;
		this.informado = informado;
	}

	@Override
	public String toString() {
		return "RegistroEO [id=" + id + ", idContingente=" + idContingente + ", idGrupos=" + idGrupos + ", fecha="
				+ fecha + ", informado=" + informado + "]";
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdContingente() {
		return idContingente;
	}

	public void setIdContingente(Integer idContingente) {
		this.idContingente = idContingente;
	}
	
	public Integer getIdGrupos() {
		return idGrupos;
	}

	public void setIdGrupos(Integer idGrupos) {
		this.idGrupos = idGrupos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Boolean getInformado() {
		return informado;
	}

	public void setInformado(Boolean informado) {
		this.informado = informado;
	}

	@Override
	public ContentValues contentValues() {
		v=new ContentValues();
		v.put(Colums._ID,get_id());
		v.put(Colums.COLUMN_ID_CONTINGENCIA,getIdContingente());
		v.put(Colums.COLUMN_ID_GRUPOS,getIdGrupos());
		v.put(Colums.COLUMN_FECHA,getFecha().toString());
		v.put(Colums.COLUMN_INFORMADO,getInformado());
		return v;
	}

	@Override
	public Component JSONValues(JSONObject jsonObject) throws JSONException {
		return new RegistroEO(
				jsonObject.optInt("id"),
				jsonObject.optInt("usuario"),
				jsonObject.optInt("telefono"),
				Date.valueOf(jsonObject.optString("fecha")),
				Boolean.valueOf(jsonObject.optString("informado")));
	}

	@Override
	public String tableName() {
		return Colums.TABLE_REGISTRO;
	}
}
