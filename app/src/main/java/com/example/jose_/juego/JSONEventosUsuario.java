package com.example.jose_.juego;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jose_ on 27/9/2018.
 */
public class JSONEventosUsuario extends AsyncTask<String, String, String>

{
    Callback callback;
    String txtFinal = "";
    ArrayList<String> arrayDispo = new ArrayList <String> ();
    ProgressDialog pDialog;
    Context context;
    String minFecha="";
    String user="";
    boolean popup;

//        private final Handler handler = new Handler();

    public ArrayList <String> getarrayDispo (){
        return arrayDispo;
    }

    public JSONEventosUsuario (MainActivity disp, String min, String us, boolean pop){//Ademas tiene que recibir el nombre de usuario loggeado
        //User = u;
       popup = pop;
        context = disp;
        minFecha = min;
        user = us;
        pDialog = new ProgressDialog(disp);
        pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
        pDialog.setMessage("Cargando eventos... \n\nPor favor espere..");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        pDialog.show();
    }

    protected String doInBackground(String... arg0) {
        //INSERTAR EN BD
        String result = "";
        InputStream isr = null;
        HttpURLConnection conn=null;
        int responseCode=0;
        try{
            ServerID server = ServerID.getInstance();
            System.out.println("min fecha:   " + minFecha);
            String urlString = server.DBserver+"cargarEventosUsuario.php?fecha="+minFecha+"&user="+user; //Pasar la fecha a partir de cuando filtrar
            //Pasar el usuario para ver si participa en ese evento!!!

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            responseCode = conn.getResponseCode();
            isr = conn.getInputStream();

        }catch(Exception e){
            Log.e("log_tag", "-Error in http connection- "+e.toString());

            txtFinal = "JSONEventosUsuario - Couldnt connect to database - " + e.toString();
        }

        //convert response to string
        try{
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"UTF-8"),8);
                //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("TOSTRING: " + reader.readLine());
                    sb.append(line);
                }
                isr.close();
                result = sb.toString();
            }else
                System.out.println("HTTPS RESPONSE CODE FALSE - "+responseCode);
        } catch(Exception e){
            Log.e("log_tag", "JSONEventosUsuario - Error converting result - "+e.toString());
        }


        String s = "";

        try {
            System.out.println("JSONListaCategorias : " + result);

            if (result.compareTo("[null]") != 0) { //== null){

                JSONParser jsonParser = new JSONParser();

                String r = result.substring(0, result.indexOf("]")+1) ;

                JSONArray jsonArrayResult = (JSONArray) jsonParser.parse(r);


                System.out.println("jSONArrayResult: " + jsonArrayResult.toString());

                for (int i=0; i<jsonArrayResult.size() ;i++){
                    JSONObject b = (JSONObject) jsonArrayResult.get(i);
                    String turno = (String) b.get("turno");
                    String dia = (String) b.get("fecha");
                    System.out.println("Turno: " + turno + " - Dia: " + dia );

                    s =  turno + "*" + dia; //Formato: "turno":"2","fecha":"13-11-2018"
                    arrayDispo.add(s); //Agrega cada combinacion Turno-Dia en el Array
                }
            }
        }catch (Exception e){
            Log.e("log_tag", "JSONEventosUsuario - Error analizando Archivo JSON from PHP- " + e.toString());
        }

        return AsyncTask.Status.FINISHED.toString();

    }

    public  interface Callback {
        public void onComplete(String myData);
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            pDialog.dismiss();
            this.cancel(true); //finalize();d
            ((MainActivity) context).continuarJSONEventosUsuario(arrayDispo, popup);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

}