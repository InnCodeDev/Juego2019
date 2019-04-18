package com.example.jose_.juego;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jose_ on 27/8/2018.
 */
public class PopUpEventoBorrarse extends DialogFragment {
    static String user = "";
    static Context c;

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

        TextView turno = (TextView) view.findViewById(R.id.textView3);
        TextView dia = (TextView) view.findViewById(R.id.textView4);
        TextView insF5 = (TextView) view.findViewById(R.id.textView5);
        TextView insF7 = (TextView) view.findViewById(R.id.textView6);

        turno.setText(getArguments().getString("turn"));
        dia.setText(getArguments().getString("di"));
        insF5.setText(getArguments().getString("cantF5"));
        insF7.setText(getArguments().getString("cantF7"));

        getDialog().setTitle("BORRAR PARTICIPACION");

        TextView btnTView = (TextView) view.findViewById(R.id.textView63);

        btnTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Se va a dar de bajar?");
                try {
                    JSONBorrarParticipacion json = new JSONBorrarParticipacion((MainActivity) c,getArguments().getString("turn"), getArguments().getString("user"), getArguments().getString("di"));
                    json.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

     /*   final TextView btn = view.findViewById(R.id.lbl_participantes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("MOSTRAR USUARIOS... !!");
                try {
                    JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("turn"), getArguments().getString("di"), (TextView)view.findViewById(R.id.lbl_participantes), btn);
                    json.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        */
    }




}