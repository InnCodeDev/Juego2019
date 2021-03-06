package com.CirculoFutbol;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jose_ on 13/9/2018.
 */
public class JSONActualizarMiDisponibilidad extends AsyncTask<String, String, String> {
    Callback callback;
    String txtFinal = "";
    ArrayList<String> arrayDispo = new ArrayList <String> ();
    ProgressDialog pDialog;
    Context context;
    String dispo="";
    String FB_user="";
    String FB_mail="";

    public ArrayList <String> getarrayDispo (){
        return arrayDispo;
    }

    public JSONActualizarMiDisponibilidad (Disponibilidad disp, String dis, String user, String mail){//Ademas tiene que recibir el nombre de usuario loggeado
        //User = u;
        dispo=dis;
        FB_user=user;
        FB_mail=mail;

        context = disp;
        pDialog = new ProgressDialog(disp);
        pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
        pDialog.setMessage("Actualizando su disponibilidad. \nPor favor espere..");
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

            String parametros = "user=Jose&disponibilidad="+dispo;

            String urlString = ServerID.DBserver +"actualizarMiDisponibilidad.php?user="+FB_user+"&disponibilidad="+dispo;


            urlString.replace(" ", "%20");
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();
            responseCode = conn.getResponseCode();
            isr = conn.getInputStream();

        }catch(Exception e){
            Log.e("log_tag", "JSONActualizarMiDisponibilidad -Error in http connection- "+e.toString());

            txtFinal = "Couldnt connect to database - " + e.toString();
        }

        //convert response to string
/*
        try{
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"UTF-8"),8);
                //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                isr.close();
                result = sb.toString();
            }else
                System.out.println("HTTPS RESPONSE CODE FALSE - "+responseCode);
        } catch(Exception e){
            Log.e("log_tag", "-Error converting result - "+e.toString());
        }
*/
/*
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
                    String estadoFinal = (String) b.get("estadoFinal");
                    System.out.println("estadoFinal: " + estadoFinal);

                }
            }
        }catch (Exception e){
            Log.e("log_tag", "-Error analizando Archivo JSON from PHP- " + e.toString());
        }
*/
        return AsyncTask.Status.FINISHED.toString();

    }

    public  interface Callback {
        void onComplete(String myData);
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            pDialog.dismiss();
            this.cancel(true); //finalize();
            ((Disponibilidad) context).continuarJSONDisponibilidad(this.getarrayDispo());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

}
