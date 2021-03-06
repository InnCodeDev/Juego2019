package com.CirculoFutbol;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
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
public class cuarta extends android.support.v4.app.Fragment {
    Calendar cal5;
    boolean creado = false;
    String minDay;
    String maxDay;
    ArrayList cuarta, cuartaM, cuartaZ, cuartaX = new ArrayList();
    Bundle bundle;

    AdView mAdView;

    public cuarta () {
        cal5 = Calendar.getInstance();

        getDiaSemana();

        cal5.add(Calendar.DAY_OF_WEEK,21);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        minDay = formateador.format(cal5.getTime());

        cal5.add(Calendar.DAY_OF_WEEK,5);
        maxDay = formateador.format(cal5.getTime()); //cal2.getTime();

        System.out.println("4--- MINIMA FECHA: " + minDay + " ---- " + "MAX FECHA: " +  maxDay);
        cal5.add(Calendar.DAY_OF_WEEK,-26);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle = this.getArguments();
        if (bundle != null) {
            cuarta = bundle.getStringArrayList("a");
            cuartaM = bundle.getStringArrayList("b");
            cuartaZ = bundle.getStringArrayList("c");
            cuartaX = bundle.getStringArrayList("d");
        }
        View v = inflater.inflate(R.layout.fragment_cuarta, container, false);

        MobileAds.initialize(this.getContext(), this.getResources().getString(R.string.banner_ad_unit_id));
        mAdView = v.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
//       mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(this.getResources().getString(R.string.banner_ad_unit_id));
        mAdView.loadAd(adRequest);
        //mAdView.setEnabled(true);
        mAdView.setBackgroundColor(Color.BLACK);

        return v;
    }

    public void Refresh(){
        LimpiarTodo();
        getEventosSemana(); //this.getView());
        getEventosUsuario(); //this.getView());
        getEventosNULL();
        getEventosFULL();
    }

    public void RELOADFRAGMENT (Bundle bu, ArrayList a, ArrayList b, ArrayList c, ArrayList d){
        bundle = bu;
        cuarta = a;
        cuartaM = b;
        cuartaZ = c;
        cuartaX = d;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        Refresh();
    }

    public void getDiaSemana(){
        switch (cal5.get(Calendar.DAY_OF_WEEK)){
            case 1: //Domingo
                cal5.add(Calendar.DAY_OF_WEEK,1);
                break;
            case 2: //Lunes
                cal5.add(Calendar.DAY_OF_WEEK,0);
                break;
            case 3: //Martes
                cal5.add(Calendar.DAY_OF_WEEK,-1);
                break;
            case 4: //Miercoles
                cal5.add(Calendar.DAY_OF_WEEK,-2);
                break;
            case 5: //Jueves
                cal5.add(Calendar.DAY_OF_WEEK,-3);
                break;
            case 6: //Viernes
                cal5.add(Calendar.DAY_OF_WEEK,-4);
                break;
            case 7: //Sabado
                cal5.add(Calendar.DAY_OF_WEEK,-5);
                break;
        }
    }
    public void getEventosFULL(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (cuartaX != null && cuartaX.size()>0){
            System.out.println("CUARTAXXX !! ... tiene " + cuartaX.size());
            Iterator I = cuartaX.iterator();
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
            System.out.println("......primeraX es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosNULL(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (cuartaZ != null && cuartaZ.size()>0){
            System.out.println("cuartaZ !! ... tiene " + cuartaZ.size());
            Iterator I = cuartaZ.iterator();
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
            System.out.println("......cuartaZ es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosSemana(){
        if (cuarta != null && cuarta.size()>0){
            System.out.println("cuarta !! ... tiene " + cuarta.size());
            Iterator I = cuarta.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1);
                    System.out.println("CUA1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setText(cant);
                }
            }
        }else{
            System.out.println("......cuarta es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosUsuario(){
        // primera.add(cant + "*" + "textView" + semana +  turn + r);
        if (cuartaM != null && cuartaM.size()>0){
            System.out.println("cuartaMM !! ... tiene " + cuartaM.size());
            Iterator I = cuartaM.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
 //                       String cant = txt.substring(0,txt.indexOf("*"));

               //         String cant = txt.substring(0,txt.indexOf("*"));
                    String txF = txt.substring(txt.indexOf("*")+1);

                    resID = getResources().getIdentifier(txF, "id", getActivity().getPackageName());
                    TextView ta = this.getActivity().findViewById(resID); //view.findViewById(resID);
  //                   ta.setText(cant);
                //     ta.setText(cant);
                    System.out.println("cuartaM: " + txt + " -- " + txF );
                    ta.setBackgroundColor(Color.GREEN);
                }
            }
        }else{
            System.out.println("......cuartaM es NULL o IGUAL A CERO...");
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
                int resID = getResources().getIdentifier("textView4"+S+j, "id", getActivity().getPackageName());
                TextView ta = this.getActivity().findViewById(resID); //view.findViewById(resID);
                ta.setText("-");

               // ta.setBackgroundColor(Color.GREEN);

                ta.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
//    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.LimpiarTodo();

        getDiaSemana();

        System.out.println("4444444444444444444   " + cal5.get(Calendar.DAY_OF_MONTH) + " - " + cal5.get(Calendar.DAY_OF_MONTH));

        TextView d1 = view.findViewById(R.id.textView4001);
        cal5.add(Calendar.DAY_OF_WEEK, 21); //21
        d1.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));

        TextView d2 = view.findViewById(R.id.textView4002);
        cal5.add(Calendar.DAY_OF_WEEK, 1);
        d2.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));
        TextView d3 = view.findViewById(R.id.textView4003);
        cal5.add(Calendar.DAY_OF_WEEK, 1);
        d3.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));
        TextView d4 = view.findViewById(R.id.textView4004);
        cal5.add(Calendar.DAY_OF_WEEK, 1);
        d4.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));
        TextView d5 = view.findViewById(R.id.textView4005);
        cal5.add(Calendar.DAY_OF_WEEK, 1);
        d5.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));
        TextView d6 = view.findViewById(R.id.textView4006);
        cal5.add(Calendar.DAY_OF_WEEK, 1);
        d6.setText(String.valueOf(cal5.get(Calendar.DAY_OF_MONTH)));

        cal5.add(Calendar.DAY_OF_WEEK, -26);
        getEventosSemana();

        System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 4");

        Refresh();
    }


    public String getMinDay(){
        return minDay;
    }
    public String getMaxDay(){
        return maxDay;
    }

}
