package com.example.jose_.juego;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class tercera extends android.support.v4.app.Fragment {
    Calendar cal4;
    boolean creado = false;
    String minDay;
    String maxDay;
    ArrayList tercera, terceraM, terceraZ, terceraX = new ArrayList();
    Bundle bundle;
    AdView mAdView;

    public tercera () {
        cal4 = Calendar.getInstance();

        getDiaSemana();

        cal4.add(Calendar.DAY_OF_WEEK,14);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        minDay = formateador.format(cal4.getTime());

        cal4.add(Calendar.DAY_OF_WEEK,5);
        maxDay = formateador.format(cal4.getTime()); //cal2.getTime();

        System.out.println("3--- MINIMA FECHA: " + minDay + " ---- " + "MAX FECHA: " +  maxDay);
        cal4.add(Calendar.DAY_OF_WEEK,-19);
    }
/*
    public void setCalendar(ArrayList <String> ar, ArrayList <String> a) {

    }
*/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle = this.getArguments();
        if (bundle != null) {
            tercera = bundle.getStringArrayList("a");
            terceraM = bundle.getStringArrayList("b");
            terceraZ = bundle.getStringArrayList("c");
            terceraX = bundle.getStringArrayList("d");
        }
        View v = inflater.inflate(R.layout.fragment_tercera, container, false);

        mAdView = v.findViewById(R.id.adView2);
    //    mAdView.setAdSize(AdSize.BANNER);
    //    mAdView.setAdUnitId("ca-app-pub-5210550399898768/7402556140");
        //MobileAds.initialize(getActivity(), "ca-app-pub-5210550399898768~4967964499");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return v;
    }

    public void RELOADFRAGMENT (Bundle bu, ArrayList a, ArrayList b, ArrayList c, ArrayList d){
        bundle = bu;
        tercera = a;
        terceraM = b;
        terceraZ = c;
        terceraX = d;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        Refresh();
    }

    public void getDiaSemana(){
        switch (cal4.get(Calendar.DAY_OF_WEEK)){
            case 1: //Domingo
                cal4.add(Calendar.DAY_OF_WEEK,1);
                break;
            case 2: //Lunes
                cal4.add(Calendar.DAY_OF_WEEK,0);
                break;
            case 3: //Martes
                cal4.add(Calendar.DAY_OF_WEEK,-1);
                break;
            case 4: //Miercoles
                cal4.add(Calendar.DAY_OF_WEEK,-2);
                break;
            case 5: //Jueves
                cal4.add(Calendar.DAY_OF_WEEK,-3);
                break;
            case 6: //Viernes
                cal4.add(Calendar.DAY_OF_WEEK,-4);
                break;
            case 7: //Sabado
                cal4.add(Calendar.DAY_OF_WEEK,-5);
                break;
        }
    }

    public void getEventosSemana(){//View view){
        if (tercera != null && tercera.size()>0){
            System.out.println("tercera !! ... tiene " + tercera.size());
            Iterator I = tercera.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1);
                    System.out.println("TER1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setText(cant);
                }
            }
        }else{
            System.out.println("......tercera es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosFULL(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (terceraX != null && terceraX.size()>0){
            System.out.println("TERCERAXXX !! ... tiene " + terceraX.size());
            Iterator I = terceraX.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //5#2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1);
                    //  System.out.println("PRI1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    //ta.setBackgroundColor(Color.rgb(255,68,68) );
                    ta.setTextColor(Color.RED);
                }
            }
        }else{
            System.out.println("......terceraX es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosNULL(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (terceraZ != null && terceraZ.size()>0){
            System.out.println("terceraZ !! ... tiene " + terceraZ.size());
            Iterator I = terceraZ.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //5#2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setBackgroundColor(Color.LTGRAY);
                    ta.setText("");
                }
            }
        }else{
            System.out.println("......terceraZ es NULL o IGUAL A CERO...");
        }
    }


    public void getEventosUsuario(){
        if (terceraM != null && terceraM.size()>0){
            System.out.println("terceraM !! ... tiene " + terceraM.size());
            Iterator I = terceraM.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
  //                      String cant = txt.substring(0,txt.indexOf("*"));
                    String txF = txt.substring(txt.indexOf("*")+1);

                    resID = getResources().getIdentifier(txF, "id", getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); //view.findViewById(resID);
   //                  ta.setText(cant);
                  //   ta.setText(cant);
                    System.out.println("terceraM: " + txt + " -- " + txF );
                    ta.setBackgroundColor(Color.GREEN);
                }
            }
        }else{
            System.out.println("......terceraM es NULL o IGUAL A CERO...");
        }
    }

    public void LimpiarTodo (){
        String S="";
        for (int i=1;i<17; i++){
            for (int j=1;j<7;j++){
                if (i<10){
                    S = "0"+ i;
                }else{
                    S = String.valueOf(i);
                }
                int resID = getResources().getIdentifier("textView3"+S+j, "id", getActivity().getPackageName());
                TextView ta = this.getActivity().findViewById(resID); //view.findViewById(resID);
                ta.setText("-");
                ta.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void Refresh(){
        LimpiarTodo();
        getEventosSemana();
        getEventosUsuario();
        getEventosNULL();
        getEventosFULL();
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.LimpiarTodo();

        getDiaSemana();

        System.out.println("33333333333333333333   " + cal4.get(Calendar.DAY_OF_MONTH) + " - " +cal4.get(Calendar.DAY_OF_MONTH));

        TextView d1 = view.findViewById(R.id.textView3001);
        cal4.add(Calendar.DAY_OF_WEEK,14);
        d1.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));

        //      SimpleDateFormat formateador = new SimpleDateFormat("dd/mm/yyyy");
        //      minDay = formateador.format(cal4); //cal2.getTime();

        TextView d2 = view.findViewById(R.id.textView3002);
        cal4.add(Calendar.DAY_OF_WEEK,1);
        d2.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));
        TextView d3 = view.findViewById(R.id.textView3003);
        cal4.add(Calendar.DAY_OF_WEEK,1);
        d3.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));
        TextView d4 = view.findViewById(R.id.textView3004);
        cal4.add(Calendar.DAY_OF_WEEK,1);
        d4.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));
        TextView d5 = view.findViewById(R.id.textView3005);
        cal4.add(Calendar.DAY_OF_WEEK,1);
        d5.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));
        TextView d6 = view.findViewById(R.id.textView3006);
        cal4.add(Calendar.DAY_OF_WEEK,1);
        d6.setText(String.valueOf(cal4.get(Calendar.DAY_OF_MONTH )));

        cal4.add(Calendar.DAY_OF_WEEK,-19);
        getEventosSemana();

        System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 3" );

        Refresh();
    }

    public String getMinDay(){
        return minDay;
    }
    public String getMaxDay(){
        return maxDay;
    }
}
