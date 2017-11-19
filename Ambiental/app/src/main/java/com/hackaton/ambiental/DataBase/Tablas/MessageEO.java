package com.hackaton.ambiental.DataBase.Tablas;

import android.content.ContentValues;

import com.hackaton.ambiental.DataBase.Controler.Component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Chris on 07/07/2017.
 */

public class MessageEO extends Component implements Serializable {
    private int _id;
    private String mensaje;
    private String nombre;
    private String numero;

    public MessageEO(int _id, String mensaje, String nombre, String numero) {
        this._id = _id;
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.numero = numero;
    }

    public MessageEO() {
    }

    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public ContentValues contentValues() {
        return null;
    }

    @Override
    public Component JSONValues(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    public String tableName() {
        return null;
    }
}
