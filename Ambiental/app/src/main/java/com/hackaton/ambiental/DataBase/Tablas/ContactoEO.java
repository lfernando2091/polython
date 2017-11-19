package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Colums;
import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ContactoEO extends Component implements Serializable{

	private static final long serialVersionUID = 3941415893283653896L;
	private Integer id;
	private String usuario;
	private String telefono;
	private String email;
	private ContentValues v;
	
	@Override
	public String toString() {
		return "ContactoEO [id=" + id + ", usuario=" + usuario + ", telefono=" + telefono + ", email=" + email + "]";
	}

	public ContactoEO() {
	}

	public ContactoEO(Integer id, String usuario, String telefono, String email) {
		this.id = id;
		this.usuario = usuario;
		this.telefono = telefono;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public ContentValues contentValues() {
		v=new ContentValues();
		v.put(Colums._ID,get_id());
		v.put(Colums.COLUMN_USUARIO,getUsuario());
		v.put(Colums.COLUMN_TELEFONO,getTelefono());
		v.put(Colums.COLUMN_EMAIL,getEmail());
		return v;
	}

	@Override
	public Component JSONValues(JSONObject jsonObject) throws JSONException {
		return new ContactoEO(
				jsonObject.optInt("id"),
				jsonObject.optString("usuario"),
				jsonObject.optString("telefono"),
				jsonObject.optString("email"));
	}

	@Override
	public String tableName() {
		return Colums.TABLE_CONTACTO;
	}
}
