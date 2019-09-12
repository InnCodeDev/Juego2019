package com.example.jose_.juego;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class JSONPopUpMail extends AsyncTask<String, String, String>  {
    String txtFinal = "";
    ArrayList<String> arrayDispo = new ArrayList <String> ();
    ProgressDialog pDialog;
    Context context;
    String usuario;
    String userMail;
    String txtMsj;

    public JSONPopUpMail(MainActivity disp, String user, String userM, String texto){
        context = disp;
        usuario = user;
        userMail = userM;
        txtMsj = texto;

        pDialog = new ProgressDialog(disp);
        pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
        pDialog.setMessage("Enviando correo... \n\nGracias!");
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

            String urlString = ServerID.DBserver +"PopUpMail.php?user="+ usuario+"&userMail="+userMail+"&msjTxt=" + txtMsj;

            urlString.replace(" ",      "%20");
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();
            responseCode = conn.getResponseCode();
            isr = conn.getInputStream();

        }catch(Exception e){
            Log.e("log_tag", "-Error in http connection- "+e.toString());

            txtFinal = "JsonPopUpMail - Couldnt connect to database - " + e.toString();
        }
/*
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
            Log.e("log_tag", "JSONInscripcionEvento - Error converting result - "+e.toString());
        }

        String s = "";

        resultado = result.toString();
        System.out.println("JSONInscripcionEvento : " + result);
*/
/*
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
                    System.out.println("Turno: " + turno + " - Dia: " + dia + " - Cantidad: " + cantidad);

                    s =  turno + "*" + dia + "*" + cantidad; //Formato: 10-2-16
                    arrayDispo.add(s); //Agrega cada combinacion Turno-Dia en el Array
                }
            }
        }catch (Exception e){
            Log.e("log_tag", "JSONInscripcionEvento - Error analizando Archivo JSON from PHP- " + e.toString());
        }
*/
        return Status.FINISHED.toString();

    }

    public  interface Callback {
        void onComplete(String myData);
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            pDialog.dismiss();
            this.cancel(true); //finalize();d
         //   ((MainActivity) context).continuarJSONPopUP(false, resultado);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }
}
