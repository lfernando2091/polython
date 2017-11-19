package com.hackaton.ambiental.Servicio;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.hackaton.ambiental.DataBase.Controler.Sesion;
import com.hackaton.ambiental.DataBase.Tablas.ContactoEO;
import com.hackaton.ambiental.DataBase.Tablas.ContingenteEO;
import com.hackaton.ambiental.DataBase.Tablas.GruposEO;
import com.hackaton.ambiental.DataBase.Tablas.RegistroEO;
import com.hackaton.ambiental.DataBase.Tablas.SeccionesEO;
import com.hackaton.ambiental.Variables.AmbientalC;
import com.hackaton.ambiental.logger.LoggerC;
import com.hackaton.ambiental.sync.PostMetod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chris on 07/07/2017.
 */

public class Nube extends Service {
    LoggerC log=new LoggerC(Nube.class);
    private Sesion sesion;

    public static final int notify = 1000*60;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    private PostMetod postMetod;

    @Override
    public void onCreate() {
        super.onCreate();
        log.printf("Sevicio...onCreate");
        sesion = new Sesion(this);
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 1000, notify);   //Schedule task
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    log.printf("Service is running");
                    new ACs().execute();
                    Toast.makeText(Nube.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class ACs extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMetod= new PostMetod(sesion);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(getInternetConnection()){
                if(postMetod.JsonReader(
                        AmbientalC.URL_REGISTRO,
                        new String[]{"key"},
                        new String[]{getMD5(AmbientalC.ApiWeb)}
                )!=null){
                    return postMetod.getBytes();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            log.printf(aVoid);
            try {
                parseJSon(aVoid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws Exception {
        if (data == null)return;
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.optJSONArray("servicio");
        RegistroEO registro= new RegistroEO();
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.optJSONObject(i);
            JSONArray jsonidContingente = jsonRoute.optJSONArray("idContingente");
            JSONArray jsonidGrupos = jsonRoute.optJSONArray("idGrupos");
            //Integer id = jsonRoute.optInt("id");
            for(int j = 0; j < jsonidContingente.length(); j++){
                JSONObject jsonRoute1 = jsonidContingente.optJSONObject(j);
                postMetod.saveAll(new ContingenteEO().JSONValues(jsonRoute1),0);
                //Integer id1 = jsonRoute1.optInt("id");
                registro.setIdContingente(jsonRoute1.optInt("id"));
                //String nombre1= jsonRoute1.optString("nombre");
                //String unidadMedida1= jsonRoute1.optString("unidadMedida");
                //Integer unidadMedidaValor1 = jsonRoute1.optInt("unidadMedidaValor");
                //String descripcion1= jsonRoute1.optString("descripcion");
                //String acciones1= jsonRoute1.optString("acciones");
            }
            for(int j = 0; j < jsonidGrupos.length(); j++){
                JSONObject jsonRoute1 = jsonidGrupos.optJSONObject(j);
                GruposEO grupo=new GruposEO();
                //Integer id1 = jsonRoute1.optInt("id");
                registro.setIdGrupos(jsonRoute1.optInt("id"));
                grupo.setId(jsonRoute1.optInt("id"));
                JSONArray jsonidContacto = jsonRoute1.optJSONArray("idContacto");
                for(int k = 0; k < jsonidContacto.length(); k++){
                    JSONObject jsonRoute2 = jsonidContacto.optJSONObject(k);
                    postMetod.saveAll(new ContactoEO().JSONValues(jsonRoute2),0);
                    //Integer id2 = jsonRoute2.optInt("id");
                    grupo.setIdContacto(jsonRoute2.optInt("id"));
                    //String usuario= jsonRoute2.optString("usuario");
                    //String telefono= jsonRoute2.optString("telefono");
                    //String email= jsonRoute2.optString("email");
                }
                JSONArray jsonidSeccion = jsonRoute1.optJSONArray("idSeccion");
                for(int k = 0; k < jsonidSeccion.length(); k++){
                    JSONObject jsonRoute2 = jsonidSeccion.optJSONObject(k);
                    postMetod.saveAll(new SeccionesEO().JSONValues(jsonRoute2),0);
                    //Integer id2 = jsonRoute2.optInt("id");
                    grupo.setIdSeccion(jsonRoute2.optInt("id"));
                    //String nombre= jsonRoute2.optString("nombre");
                    //String descripcion= jsonRoute2.optString("descripcion");
                }
                postMetod.saveAll(grupo,0);
            }
            registro.setFecha(Date.valueOf(jsonRoute.optString("fecha")));
            registro.setInformado(jsonRoute.optBoolean("informado"));
            postMetod.saveAll(registro,0);
            //String fecha= jsonRoute.optString("fecha");
            //Boolean informado= jsonRoute.optBoolean("informado");
        }
    }

    private boolean getInternetConnection(){
        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
            /*Wifi*/
            return true;
        } else if( mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED ){
            /*Mobile 3G*/
            return true;
        } else {
            return false;
        }
    }

    private String getMD5(String s) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
            return String.valueOf(new BigInteger(1, m.digest()).toString(16));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        log.printf("Sevicio...onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        log.printf("Sevicio...onRebind");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        log.printf("Adios...onTaskRemoved");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        log.printf("O por dios...onLowMemory");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.printf("Sevicio...onStartCommand");
        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}
