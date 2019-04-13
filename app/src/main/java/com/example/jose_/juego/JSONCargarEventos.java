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
 * Created by jose_ on 15/9/2018.
 */
public class JSONCargarEventos extends AsyncTask<String, String, String>{
        Callback callback;
        String txtFinal = "";
        ArrayList<String> arrayDispo = new ArrayList <String> ();
        ProgressDialog pDialog;
        Context context;
        String minFecha = "";
//        private final Handler handler = new Handler();
        boolean fromPopUp;

        public ArrayList <String> getarrayDispo (){
        return arrayDispo;
    }

        public JSONCargarEventos (MainActivity disp, String min, boolean fromPop){//Ademas tiene que recibir el nombre de usuario loggeado
        //User = u;
            context = disp;
            minFecha = min;
            fromPopUp = fromPop;
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
            String urlString = server.DBserver+"cargarEventos.php?fecha="+minFecha; //Pasar la fecha a partir de cuando filtrar
            //Pasar el usuario para ver si participa en ese evento!!!

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            responseCode = conn.getResponseCode();
            isr = conn.getInputStream();

        }catch(Exception e){
            Log.e("log_tag", "-Error in http connection- "+e.toString());

            txtFinal = "JSONCargarEventos - Couldnt connect to database - " + e.toString();
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
            Log.e("log_tag", "JSONCargarEventos - Error converting result - "+e.toString());
        }


        String s = "";

        try {
            System.out.println("JSONListaCategorias : " + result);

            if (result != null){

                JSONParser jsonParser = new JSONParser();

                String r = result.substring(0, result.indexOf("]")+1) ;

                JSONArray jsonArrayResult = (JSONArray) jsonParser.parse(r);


                System.out.println("jSONArrayResult: " + jsonArrayResult.toString());

                for (int i=0; i<jsonArrayResult.size() ;i++){
                    JSONObject b = (JSONObject) jsonArrayResult.get(i);
                    //String id = (String) b.get("id");
                    String turno = (String) b.get("turno");
                    String dia = (String) b.get("fecha");
                    String cantidad = (String) b.get("Cantidad");
                    String cancha = (String) b.get("cancha");
                    System.out.println("Turno: " + turno + " - Dia: " + dia + " - Cantidad: " + cantidad + " - Cancha: " + cancha);

                    s =  turno + "*" + dia + "*" + cantidad+ "/"+ cancha; //Formato: 10-2-16/5
                    arrayDispo.add(s); //Agrega cada combinacion Turno-Dia en el Array
                }
            }
        }catch (Exception e){
            Log.e("log_tag", "JSONCargarEventos - Error analizando Archivo JSON from PHP- " + e.toString());
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
            ((MainActivity) context).continuarJSONCargarEventos(arrayDispo, fromPopUp);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

}