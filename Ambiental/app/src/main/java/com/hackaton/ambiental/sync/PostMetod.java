package com.hackaton.ambiental.sync;

import android.net.Uri;
import android.os.NetworkOnMainThreadException;

import com.hackaton.ambiental.DataBase.Controler.Component;
import com.hackaton.ambiental.DataBase.Controler.Sesion;
import com.hackaton.ambiental.Variables.AmbientalC;
import com.hackaton.ambiental.logger.LoggerC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by LuisFernando on 05/04/2017.
 */

public class PostMetod implements Serializable {
    private Sesion add;
    static final long serialVersionUID = -7607619678367524000L;
    private File file;
    public static final int SAFE_INSERT=0, SAFE_UPDATE=1;

    private LoggerC log=new LoggerC(PostMetod.class);

    private String bytes="";

    public String getBytes() {
        return bytes;
    }

    /**
     * Guardando datos en la base de dartos
     */
    public void saveAll(Component components, int type) {
        try {
            switch (type){
                case 0:add.SVE(components);break;
                case 1:add.UDE(components);break;
            }
        }catch (Exception e){}
    }

    public PostMetod(Sesion  add) {
        this.add=add;
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private HttpsURLConnection HttpsC(URL url) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {return new java.security.cert.X509Certificate[] {};}
        } };
        // Create the SSL connection
        SSLContext sc= SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        //establecemos la conexión con el destino
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(DO_NOT_VERIFY);
        return urlConnection;
    }

    private HttpsURLConnection HttpsS(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        // Create the SSL connection
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        urlConnection.setSSLSocketFactory(sc.getSocketFactory());
        return urlConnection;
    }

    public String JsonReader(String urls, String[] key, String[] value){
        try {
            //primero especificaremos el origen de nuestro archivo a descargar utilizando
            //la ruta completa
            URL url = new URL(urls);
            //establecemos la conexión con el destino //TODO camviar a HttpsS
            HttpsURLConnection urlConnection = HttpsC(url);
            //establecemos el método jet para nuestra conexión
            //el método setdooutput es necesario para este tipo de conexiones
            // Use this if you need SSL authentication
            //String userpass = "luis" + ":" + "heml980623hhgrns06";
            //String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            // urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder();
            if(key.length==value.length) for (int t=0; t<key.length; t++) builder.appendQueryParameter(key[t], value[t]);
            String query = builder.build().getEncodedQuery();
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();

            //leemos los datos desde la url
            InputStream is = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return bytes= buffer.toString();
//y gestionamos errores
        } catch (MalformedURLException e) {
            log.printf(e.getMessage());
            e.printStackTrace();
        } catch (IOException ex) {
            log.printf(ex.getMessage());
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException exe) {
            log.printf(exe.getMessage());
            exe.printStackTrace();
        } catch (KeyManagementException exep) {
            log.printf(exep.getMessage());
            exep.printStackTrace();
        }
        return null;
    }


    public int Gone(File file, String urls, String[] key, String[] value){
        try {
            this.file= file.getAbsoluteFile();
            //primero especificaremos el origen de nuestro archivo a descargar utilizando
            //la ruta completa
            URL url = new URL(urls);
            //establecemos la conexión con el destino //TODO camviar a HttpsS
            HttpsURLConnection urlConnection = HttpsC(url);
            //establecemos el método jet para nuestra conexión
            //el método setdooutput es necesario para este tipo de conexiones
            // Use this if you need SSL authentication
            //String userpass = "luis" + ":" + "heml980623hhgrns06";
            //String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            // urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder();
            if(key.length==value.length) for (int t=0; t<key.length; t++) builder.appendQueryParameter(key[t], value[t]);
            String query = builder.build().getEncodedQuery();
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            //por último establecemos nuestra conexión y cruzamos los dedos
            urlConnection.connect();
            //vamos a establecer la ruta de destino para nuestra descarga
            //para hacerlo sencillo en este ejemplo he decidido descargar en
            //la raíz de la tarjeta SD
            //vamos a crear un objeto del tipo de fichero
            //donde descargaremos nuestro fichero, debemos darle el nombre que
            //queramos, si quisieramos hacer esto mas completo
            //cogeríamos el nombre del origen
            //utilizaremos un objeto del tipo fileoutputstream
            //para escribir el archivo que descargamos en el nuevo
            FileOutputStream fileOutput = new FileOutputStream(file);
            //leemos los datos desde la url
            InputStream inputStream = urlConnection.getInputStream();
            //obtendremos el tamaño del archivo y lo asociaremos a una
            //variable de tipo entero
            //--int totalSize = urlConnection.getContentLength();
            //--int downloadedSize = 0;
            //creamos un buffer y una variable para ir almacenando el
            //tamaño temporal de este
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            //ahora iremos recorriendo el buffer para escribir el archivo de destino
            //siempre teniendo constancia de la cantidad descargada y el total del tamaño
            //con esto podremos crear una barra de progreso
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                //downloadedSize += bufferLength;
                //podríamos utilizar una función para ir actualizando el progreso de lo
                //descargado
                //actualizaProgreso(downloadedSize, totalSize);
            }
            //cerramos
            fileOutput.close();
            return urlConnection.getResponseCode();
//y gestionamos errores
        } catch (MalformedURLException e) {
            log.printf(e.getMessage());
            e.printStackTrace();
        } catch (IOException ex) {
            log.printf(ex.getMessage());
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException exe) {
            log.printf(exe.getMessage());
            exe.printStackTrace();
        } catch (KeyManagementException exep) {
            log.printf(exep.getMessage());
            exep.printStackTrace();
        }
        return 0;
    }

    public int Gone(String urls, String[] key, String[] value){
        try {
            //this.file= file;
            //primero especificaremos el origen de nuestro archivo a descargar utilizando
            //la ruta completa
            URL url = new URL(urls);
            //establecemos la conexión con el destino //TODO camviar a HttpsS
            HttpsURLConnection urlConnection = HttpsC(url);
            //establecemos el método jet para nuestra conexión
            //el método setdooutput es necesario para este tipo de conexiones
            // Use this if you need SSL authentication
            //String userpass = "luis" + ":" + "heml980623hhgrns06";
            //String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            // urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder();
            if(key.length==value.length) for (int t=0; t<key.length; t++) builder.appendQueryParameter(key[t], value[t]);
            String query = builder.build().getEncodedQuery();
            //log.printf(urlConnection.getOutputStream().toString());
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            //por último establecemos nuestra conexión y cruzamos los dedos
            urlConnection.connect();
            //vamos a establecer la ruta de destino para nuestra descarga
            //para hacerlo sencillo en este ejemplo he decidido descargar en
            //la raíz de la tarjeta SD
            //log.printf(urlConnection.getContent().toString());
            return urlConnection.getResponseCode();
//y gestionamos errores
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        }
        return 0;
    }

    public String Read(String[] nodos, Component[] components, boolean canDelete, int type){
        String msg="Sincronización terminada con exito.";
        if (file.exists()) {
            try {
                FileInputStream stream = new FileInputStream(file);
                JSONObject jsonObj= new JSONObject();
                JSONArray[] data;
                try {FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                    jsonObj = new JSONObject(Charset.defaultCharset().decode(bb).toString());}
                catch (Exception e) {e.printStackTrace();}
                finally {stream.close();}
                try {
                    data= new JSONArray[(nodos.length+2)];
                    for(int w=0; w<(nodos.length+2);w++){
                        if(w<nodos.length){
                            data[w] = jsonObj.optJSONArray(nodos[w]);
                            if (data[w] != null) for (int i = 0; i < data[w].length(); i++){
                                //saveAll(
                                        components[w]=components[w].JSONValues(data[w].getJSONObject(i));
                                //, type);
                            }
                        }else{
                            /*Codigo de errores del server*/
                            data[w] = jsonObj.optJSONArray(w==nodos.length? AmbientalC.ExeptionResultSeverNick:AmbientalC.ExeptionResultSeverPass);
                            if (data[w] != null) {
                                for (int i = 0; i < data[w].length(); i++) {
                                    JSONObject c = data[w].getJSONObject(i);
                                    msg= c.getString("descripcion");
                                }
                            }
                            else msg="Sincronización terminada.";
                        }
                    }
                } catch (JSONException e) {msg="El servidor no responde con datos validos.";
                }
            } catch (Exception e) {e.printStackTrace();}
        }
        if (canDelete) {
            file.delete();
        }
        return msg;
    }
}