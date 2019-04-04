package com.example.jose_.juego;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class segunda extends android.support.v4.app.Fragment {
    Calendar cal3;
    boolean creado = false;
    String minDay;
    String maxDay;
    ArrayList segunda, segundaM = new ArrayList();
    Bundle bundle;

    public segunda () {
        cal3 = Calendar.getInstance();
        getDiaSemana();

        cal3.add(Calendar.DAY_OF_WEEK,7);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        minDay = formateador.format(cal3.getTime());

        cal3.add(Calendar.DAY_OF_WEEK,5);
        maxDay = formateador.format(cal3.getTime()); //cal2.getTime();

        System.out.println("2--- MINIMA FECHA: " + minDay + " ---- " + "MAX FECHA: " +  maxDay);
        cal3.add(Calendar.DAY_OF_WEEK,-12);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle = this.getArguments();
        if (bundle != null) {
            System.out.println("BUNDLE 2 NO ES NULL..........." );
            segunda = bundle.getStringArrayList("a");
            segundaM = bundle.getStringArrayList("b");
        }
        System.out.println("CREOOOOOOOOO VISTA 2 -- " );
        View v = inflater.inflate(R.layout.fragment_segunda, container, false);
        return v;
    }

    public void Refresh(){
        LimpiarTodo();
        getEventosSemana();
        getEventosUsuario();
    }

    public void RELOADFRAGMENT (Bundle bu, ArrayList a, ArrayList b){
        bundle = bu;
        segunda = a;
        segundaM = b;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        Refresh();
    }
    public void actualizarBundle (Bundle bu, ArrayList a, ArrayList b){
        bundle = bu;
        segunda = a;
        segundaM = b;
    }

    public void getDiaSemana(){
        switch (cal3.get(Calendar.DAY_OF_WEEK)){
            case 1: //Domingo
                cal3.add(Calendar.DAY_OF_WEEK,1);
                break;
            case 2: //Lunes
                cal3.add(Calendar.DAY_OF_WEEK,0);
                break;
            case 3: //Martes
                cal3.add(Calendar.DAY_OF_WEEK,-1);
                break;
            case 4: //Miercoles
                cal3.add(Calendar.DAY_OF_WEEK,-2);
                break;
            case 5: //Jueves
                cal3.add(Calendar.DAY_OF_WEEK,-3);
                break;
            case 6: //Viernes
                cal3.add(Calendar.DAY_OF_WEEK,-4);
                break;
            case 7: //Sabado
                cal3.add(Calendar.DAY_OF_WEEK,-5);
                break;
        }
    }

    public void getEventosSemana(){//View view){
        if (segunda != null && segunda.size()>0){
            System.out.println("PRIMERA !! ... tiene " + segunda.size());
            Iterator I = segunda.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
                    String cant = txt.substring(0,txt.indexOf("*"));
                    //        int r = (Integer.valueOf(txt.substring(txt.length()-2,txt.length())) - Integer.valueOf(minDay.substring(0,2)))+1;
                    String txF = txt.substring(txt.indexOf("*")+1, txt.length());
                    System.out.println("SEG1: " + txt + " -- " + txF);

                    resID = getResources().getIdentifier(txF, "id",  getActivity().getPackageName());
                    TextView ta = (TextView) this.getActivity().findViewById(resID); // getView().findViewById(resID);
                    ta.setText(cant);
                }
            }
        }else{
            System.out.println("......segunda es NULL o IGUAL A CERO...");
        }
    }

    public void getEventosUsuario(){
        // primera.add(cant + "*" + "textView" + semana +  turn + r);
        if (segundaM != null && segundaM.size()>0){
            System.out.println("PRIMERAMMM !! ... tiene " + segundaM.size());
            Iterator I = segundaM.iterator();
            int resID;
            while(I.hasNext()){
                String txt = (String) I.next(); //2*textView10728
                if (txt.length()>1){
                        String cant = txt.substring(0,txt.indexOf("*"));
                    String txF = txt.substring(txt.indexOf("*")+1,txt.length());

                    resID = getResources().getIdentifier(txF, "id", getActivity().getPackageName());
                    TextView ta = (TextView) this.getActivity().findViewById(resID); //view.findViewById(resID);
                     ta.setText(cant);
                    System.out.println("segundaM: " + txt + " -- " + txF );
                    ta.setBackgroundColor(Color.GREEN);
                }
            }
        }else{
            System.out.println("......segundaM es NULL o IGUAL A CERO...");
        }
    }

    public void LimpiarTodo (){
        String S="";
        for (int i=1;i<13; i++){
            for (int j=1;j<7;j++){
                if (i<10){
                    S = "0"+String.valueOf(i);
                }else{
                    S = String.valueOf(i);
                }
                int resID = getResources().getIdentifier("textView2"+S+j, "id", getActivity().getPackageName());
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

        TextView d1 = (TextView) view.findViewById(R.id.textView2001);
        cal3.add(Calendar.DAY_OF_WEEK,7);
        d1.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));

        //     SimpleDateFormat formateador = new SimpleDateFormat("dd/mm/yyyy");
        //     minDay = formateador.format(cal3); //cal2.getTime();

        TextView d2 = (TextView) view.findViewById(R.id.textView2002);
        cal3.add(Calendar.DAY_OF_WEEK,1);
        d2.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));
        TextView d3 = (TextView) view.findViewById(R.id.textView2003);
        cal3.add(Calendar.DAY_OF_WEEK,1);
        d3.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));
        TextView d4 = (TextView) view.findViewById(R.id.textView2004);
        cal3.add(Calendar.DAY_OF_WEEK,1);
        d4.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));
        TextView d5 = (TextView) view.findViewById(R.id.textView2005);
        cal3.add(Calendar.DAY_OF_WEEK,1);
        d5.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));
        TextView d6 = (TextView) view.findViewById(R.id.textView2006);
        cal3.add(Calendar.DAY_OF_WEEK,1);
        d6.setText(String.valueOf(cal3.get(Calendar.DAY_OF_MONTH )));

        cal3.add(Calendar.DAY_OF_WEEK,-12);
        getEventosSemana();

        System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 2");

        this.Refresh();
    }

    public String getMinDay(){
        return minDay;
    }
    public String getMaxDay(){
        return maxDay;
    }

}
