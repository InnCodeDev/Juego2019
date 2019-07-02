package com.example.jose_.juego;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jose_ on 27/8/2018.
 */
public class PopUpEventoBorrarse extends DialogFragment {
    static String user = "";
    static Context c;
    TextView sc_participantes2;
    TextView btn;
    boolean viendoPart = false;

    public PopUpEventoBorrarse(){

    }//this, TextoTurno, Fecha, FB_user, turno, inscripto, cantF5, cantF7 );
    public static PopUpEventoBorrarse newInstance(MainActivity context, String TextoTurno, String Fecha, String User, String NroTurno, boolean inscripto, int CantF5, int CantF7){
        PopUpEventoBorrarse pop = new PopUpEventoBorrarse();
        c=context;
//        int cantTotal = Integer.valueOf(CantF5) + Integer.valueOf(CantF7);
        Bundle args = new Bundle();
        args.putString("turn", TextoTurno);
        args.putString("Nroturn", NroTurno);
        args.putString("di", Fecha);
        args.putString("user", User);
        args.putString("insc", String.valueOf(0));
        args.putBoolean("bolinsc", inscripto);
        args.putString("cantF5", String.valueOf(CantF5));
        args.putString("cantF7", String.valueOf(CantF7));
        pop.setArguments(args);

        return pop;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evento_borrarse, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView turno = (TextView) view.findViewById(R.id.textView32);
        TextView dia = (TextView) view.findViewById(R.id.textView42);
        TextView insF5 = (TextView) view.findViewById(R.id.textView52);
        TextView insF7 = (TextView) view.findViewById(R.id.textView62);

        turno.setText(getArguments().getString("turn"));
        dia.setText(getArguments().getString("di"));
        insF5.setText(getArguments().getString("cantF5"));
        insF7.setText(getArguments().getString("cantF7"));

        TextView titulo = view.findViewById(R.id.lbl_titulo2);
        titulo.setText("Borrar Participacion");

        getDialog().setTitle("BORRAR PARTICIPACION");

        TextView btnTView = (TextView) view.findViewById(R.id.textView632);

        btnTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Se va a dar de bajar?");
                try {
                    JSONBorrarParticipacion json = new JSONBorrarParticipacion((MainActivity) c,getArguments().getString("Nroturn"), getArguments().getString("user"), getArguments().getString("di"));
                    json.execute();
                    PopUpEventoBorrarse.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Asigna evento Click de VerParticipantes
        sc_participantes2 = (TextView) view.findViewById(R.id.sc_participantes2);
        btn = (TextView) view.findViewById(R.id.lbl_participantes2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viendoPart == false){
                    System.out.println("Mostrando USUARIOS... !!");
                    try {
                        btn.setText("Ocultar Participantes");
                        viendoPart = true;
                        JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_participantes2);
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Ocultando USUARIOS... !!");
                    try {
                        btn.setText("Ver Participantes");
                        sc_participantes2.setText("");
                        viendoPart = false;
                        sc_participantes2.setSystemUiVisibility(0);
                        sc_participantes2.setText("");
                        sc_participantes2.setHeight(0);
                       // JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_participantes2);
                      //  json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

/*
                System.out.println("----------" + btn.getText().toString());
                System.out.println("----> " + btn.getText().toString());
                if (btn.getText().toString().compareTo("Ocultar Participantes") == 0){
                   // btn.setText("Ver Participantes");
                    sc_participantes2.setText("");
                }else{
                    System.out.println("MOSTRAR USUARIOS... !!");
                    try {
                        btn.setText("Ocultar Participantes");
                        JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_participantes2);
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
*/            }
        });

    }





}
