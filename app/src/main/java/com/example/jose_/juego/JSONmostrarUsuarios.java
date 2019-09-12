package com.example.jose_.juego;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class JSONmostrarUsuarios extends AsyncTask<String, String, String>
{
    Callback callback;
    String txtFinal = "";
    ArrayList<String> arrayDispo = new ArrayList <String> ();
    ProgressDialog pDialog;
    Context context;
    String dia="";
    String turno="";
    boolean popup;
View view;
TextView btn, sc;
    public ArrayList <String> getarrayDispo (){
        return arrayDispo;
    }

    public JSONmostrarUsuarios(View v, MainActivity disp, String turn, String di, TextView bt, TextView s){//Ademas tiene que recibir el nombre de usuario loggeado
        view = v;
        context = disp;
        turno = turn;
        dia = di;
        btn = bt;
        sc = s;
        pDialog = new ProgressDialog(disp);
        pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
        pDialog.setMessage("Cargando participantes... \n\nPor favor espere..");
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

            String urlString = ServerID.DBserver +"mostrarUsuarios.php?fecha="+dia+"&turno="+turno; //Pasar la fecha a partir de cuando filtrar
            //Pasar el usuario para ver si participa en ese evento!!!

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            responseCode = conn.getResponseCode();
            isr = conn.getInputStream();

        }catch(Exception e){
            Log.e("log_tag", "-Error in http connection- "+e.toString());

            txtFinal = "JSONMostarUsuarios - Couldnt connect to database - " + e.toString();
        }

        //convert response to string
        try{
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, StandardCharsets.UTF_8),8);
                //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("TOSTRING USUARIOS: " + reader.readLine());
                    sb.append(line);
                }
                isr.close();
                result = sb.toString();
            }else
                System.out.println("HTTPS RESPONSE CODE FALSE - "+responseCode);
        } catch(Exception e){
            Log.e("log_tag", "JSONmostrarUsuarios - Error converting result - "+e.toString());
        }


        String s = "";

        try {
            System.out.println("JSONListaUsuarios : " + result);

            if (result.compareTo("<br />null") != 0) { //== null){

                JSONParser jsonParser = new JSONParser();

                String r = result.substring(0, result.indexOf("]")+1) ;

                JSONArray jsonArrayResult = (JSONArray) jsonParser.parse(r);


                System.out.println("jSONArrayResult: " + jsonArrayResult.toString());

                for (int i=0; i<jsonArrayResult.size() ;i++){
                    JSONObject b = (JSONObject) jsonArrayResult.get(i);
                   // String turno = (String) b.get("turno");
                  //  String dia = (String) b.get("fecha");
                    String usuario = (String) b.get("USUARIO");
                    System.out.println("Usuario: " + usuario);

                    s =  usuario; //turno + "*" + dia; //Formato: "turno":"2","fecha":"13-11-2018"
                    arrayDispo.add(s); //Agrega cada combinacion Turno-Dia en el Array
                }
            }
        }catch (Exception e){
            Log.e("log_tag", "JSONListaUsuario - Error analizando Archivo JSON from PHP- " + e.toString());
        }

        return AsyncTask.Status.FINISHED.toString();

    }

    public  interface Callback {
        void onComplete(String myData);
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            pDialog.dismiss();
                ((MainActivity) context).continuarJSONEmostrarUsuarios(view, btn, sc, arrayDispo);
            this.cancel(true); //finalize();d

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

}