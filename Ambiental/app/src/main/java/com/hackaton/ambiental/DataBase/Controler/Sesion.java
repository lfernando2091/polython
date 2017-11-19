package com.hackaton.ambiental.DataBase.Controler;

import android.content.ContentValues;
import android.content.Context;

import com.hackaton.ambiental.DataBase.Tablas.MessageEO;
import com.hackaton.ambiental.Variables.AmbientalC;
import com.hackaton.ambiental.logger.LoggerC;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


/**
 * Created by LuisFernando on 15/05/2017.
 */

public class Sesion extends SQLiteOpenHelper {
    LoggerC log=new LoggerC(Sesion.class);
    private static Sesion instance;
    Context c=null;

    public static synchronized Sesion getHelper(Context context) {
        if (instance == null)
            instance = new Sesion(context);
        return instance;
    }

    public Sesion(Context context) {
        super(context, AmbientalC.DATA_BASE_NAME, null, AmbientalC.DATA_BASE_VERSION);
        c=context;
        SQLiteDatabase.loadLibs(context);
    }

    public long SVE(Component component){
        SQLiteDatabase sqld= getWritableDatabase();
        return sqld.insert(component.tableName(),null,component.contentValues());
    }

    public long UDE(Component component){
        SQLiteDatabase sqld= getWritableDatabase();
        return sqld.update(component.tableName(),component.contentValues(),"_id="+component.get_id(), null);
    }

    public long UDE(Component component, String[] colums, String[] values){
        SQLiteDatabase sqld= getWritableDatabase();
        ContentValues v =new ContentValues();
        for(int c=0;c<colums.length; c++){
            v.put(colums[0],values[0]);
        }
        return sqld.update(component.tableName(),v,"_id="+component.get_id(), null);
    }

    public int getLastAutoId(Component component, SQLiteDatabase db) {
        int index = 0;
        Cursor cursor = component.AutoId(db);
        if(cursor.getCount() > 0){
            if (cursor.moveToFirst()) index = cursor.getInt(cursor.getColumnIndex(Colums.SEQ));
            cursor.close();return index+1;
        }else {
            return index;
        }
    }

    public int getLastAutoId(Component component) {
        return getLastAutoId(component, getReadableDatabase());
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CREATE_TABLES(sqLiteDatabase);
        SVE_DEFAULT_DATA(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_CONTACTO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_CONTINGENTE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_GRUPOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_REGISTRO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_SECCIONES);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    SQLiteDatabase getReadableDatabase() {
        return(super.getReadableDatabase(AmbientalC.DATA_BASE_SECURITY));
    }

    SQLiteDatabase getWritableDatabase() {
        return(super.getWritableDatabase(AmbientalC.DATA_BASE_SECURITY));
    }

    private  void CREATE_TABLES(SQLiteDatabase db) {
        db.execSQL(Colums.CREATE_TABLE_CONTACTO);
        db.execSQL(Colums.CREATE_INDEX_CONTACTO_ID);
        db.execSQL(Colums.CREATE_TABLE_CONTINGENTE);
        db.execSQL(Colums.CREATE_INDEX_CONTINGENTE_ID);
        db.execSQL(Colums.CREATE_TABLE_GRUPOS);
        db.execSQL(Colums.CREATE_INDEX_GRUPOS_ID);
        db.execSQL(Colums.CREATE_TABLE_REGISTRO);
        db.execSQL(Colums.CREATE_INDEX_REGISTRO_ID);
        db.execSQL(Colums.CREATE_TABLE_SECCIONES);
        db.execSQL(Colums.CREATE_INDEX_SECCIONES_ID);
    }

    private void SVE_DEFAULT_DATA(SQLiteDatabase db){

    }

    public MessageEO obtenerRegistros(){
        String rawQuery = "SELECT "+
                "a."+Colums._ID+","+
                "a."+ Colums.COLUMN_ID_CONTINGENCIA +","+
                "a."+ Colums.COLUMN_ID_GRUPOS +","+
                "a."+ Colums.COLUMN_FECHA +","+
                "a."+ Colums.COLUMN_INFORMADO+","+
                "b."+Colums.COLUMN_UNIDAD_MEDIDA+","+
                "b."+Colums.COLUMN_UNIDAD_MEDIDA_VALOR+","+
                "b."+Colums.COLUMN_NOMBRE+","+
                "b."+Colums.COLUMN_DESCRIPCION+","+
                "b."+ Colums.COLUMN_ACCIONES
                +" FROM " +
                Colums.TABLE_REGISTRO + " a"
                +" JOIN " +
                Colums.TABLE_CONTINGENTE+ " b"
                +" ON "+
                "b."+Colums._ID
                +" = "+
                "a."+ Colums.COLUMN_ID_CONTINGENCIA
                +" WHERE " +
                "a."+ Colums.COLUMN_INFORMADO
                + " = false"
                +" ORDER BY " +
                "a."+Colums._ID;
        log.printf("SQL getUserData:: " + rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    //BingoC.mSecciones.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NOMBRE+ "_C")));
                    log.printf("SQL getUserData:: " + cursor.getString(cursor.getColumnIndex(Colums.COLUMN_DESCRIPCION)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return null;
    }
}
