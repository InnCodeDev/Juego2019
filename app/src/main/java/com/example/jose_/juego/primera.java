package com.example.jose_.juego;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class primera extends android.support.v4.app.Fragment {
    Calendar cal2;
    View v;
    String minDay;
    String maxDay;

    ArrayList primera, primeraM, primeraZ = new ArrayList();
    boolean creado=false;
    Bundle bundle;

    public primera () {
        cal2 = Calendar.getInstance();
        getDiaSemana();

        cal2.add(Calendar.DAY_OF_WEEK,0);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        minDay = formateador.format(cal2.getTime());

        cal2.add(Calendar.DAY_OF_WEEK,5);
        maxDay = formateador.format(cal2.getTime()); //cal2.getTime();

        System.out.println("1--- MINIMA FECHA: " + minDay + " ---- " + "MAX FECHA: " +  maxDay );
        cal2.add(Calendar.DAY_OF_WEEK,-5);
        bundle = new Bundle();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle = this.getArguments();
        if (bundle != null) {
     //       System.out.println("BUNDLE 1 NO ES NULL..........." );
            primera = bundle.getStringArrayList("a");
            primeraM = bundle.getStringArrayList("b");
            primeraZ = bundle.getStringArrayList("c");
        }
        System.out.println("CREOOOOOOOOO VISTA 1 -- " ); //+ primera.size() + " -- " + primeraM.size());
        v = inflater.inflate(R.layout.fragment_primera, container, false);
        return v;
    }

    public void Refresh(){
        LimpiarTodo();
        getEventosSemana();
        getEventosUsuario();
        getEventosNULL();
    }

    public void RELOADFRAGMENT (Bundle bu, ArrayList a, ArrayList b, ArrayList c){
        bundle = bu;
        primera = a;
        primeraM = b;
        primeraZ = c;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        Refresh();

    }

    public void getDiaSemana(){
        switch (cal2.get(Calendar.DAY_OF_WEEK)){
            case 1: //Domingo
                cal2.add(Calendar.DAY_OF_WEEK,1);
                break;
            case 2: //Lunes
                cal2.add(Calendar.DAY_OF_WEEK,0);
                break;
            case 3: //Martes
                cal2.add(Calendar.DAY_OF_WEEK,-1);
                break;
            case 4: //Miercoles
                cal2.add(Calendar.DAY_OF_WEEK,-2);
                break;
            case 5: //Jueves
                cal2.add(Calendar.DAY_OF_WEEK,-3);
                break;
            case 6: //Viernes
                cal2.add(Calendar.DAY_OF_WEEK,-4);
                break;
            case 7: //Sabado
                cal2.add(Calendar.DAY_OF_WEEK,-5);
                break;
        }
    }
    public void getEventosNULL(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (primeraZ != null && primeraZ.size()>0){
            System.out.println("PRIMERAZ !! ... tiene " + primeraZ.size());
            Iterator I = primeraZ.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //5#2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1, txt.length());
                  //  System.out.println("PRI1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = (TextView) this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setBackgroundColor(Color.LTGRAY);
                    ta.setText("");
                }
            }
        }else{
            System.out.println("......primera es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosSemana(){  //(cant1 + "*" + "textView" + semana1 + tur + r);
        if (primera != null && primera.size()>0){
            System.out.println("PRIMERA !! ... tiene " + primera.size());
            Iterator I = primera.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //5#2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                  //  System.out.println("STRIIIIIIIIIIIING " + txt + " - " + cant);
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1, txt.length());
                //    System.out.println("PRI1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = (TextView) this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setText(cant);
                }
            }
        }else{
            System.out.println("......primera es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosUsuario(){
        // primeraM.add("textView" + semana +  turn + r);
        if (primeraM != null && primeraM.size()>0){
            System.out.println("PRIMERAMMM !! ... tiene " + primeraM.size());
            Iterator I = primeraM.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
               //         String cant = txt.substring(0,txt.indexOf("*"));
                    String txF = txt.substring(txt.indexOf("*")+1,txt.length());

                    resID = getResources().getIdentifier(txF, "id", getActivity().getPackageName());
                    TextView ta = (TextView) this.getActivity().findViewById(resID); //view.findViewById(resID);
                    // ta.setText(cant);
                //    System.out.println("PrimeraM: " + txt + " -- " + txF );
                    ta.setBackgroundColor(Color.GREEN);
                }
            }
        }else{
            System.out.println("......primeraM es NULL o IGUAL A CERO...");
        }
    }

    public void LimpiarTodo (){
        String S="";
        for (int i=1;i<17; i++){
            for (int j=1;j<7;j++){
                if (i<10){
                    S = "0"+String.valueOf(i);
                }else{
                    S = String.valueOf(i);
                }
                int resID = getResources().getIdentifier("textView1"+S+j, "id", getActivity().getPackageName());
                TextView ta = (TextView) this.getActivity().findViewById(resID); //view.findViewById(resID);
                ta.setText("-");
                ta.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        this.LimpiarTodo();

        getDiaSemana();
        //  getEventosSemana();
        //  getEventosUsuario();

        TextView d1 = (TextView) view.findViewById(R.id.textView1001);
        cal2.add(Calendar.DAY_OF_WEEK,0);
        d1.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));

        TextView d2 = (TextView) view.findViewById(R.id.textView1002);
        cal2.add(Calendar.DAY_OF_WEEK,1);
        d2.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));
        TextView d3 = (TextView) view.findViewById(R.id.textView1003);
        cal2.add(Calendar.DAY_OF_WEEK,1);
        d3.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));
        TextView d4 = (TextView) view.findViewById(R.id.textView1004);
        cal2.add(Calendar.DAY_OF_WEEK,1);
        d4.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));
        TextView d5 = (TextView) view.findViewById(R.id.textView1005);
        cal2.add(Calendar.DAY_OF_WEEK,1);
        d5.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));
        TextView d6 = (TextView) view.findViewById(R.id.textView1006);
        cal2.add(Calendar.DAY_OF_WEEK,1);
        d6.setText(String.valueOf(cal2.get(Calendar.DAY_OF_MONTH )));

        cal2.add(Calendar.DAY_OF_WEEK,-5);

        System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 1" );

        Refresh();
    }

    public String getMinDay(){
        return minDay;
    }
    public String getMaxDay(){
        return maxDay;
    }

}
