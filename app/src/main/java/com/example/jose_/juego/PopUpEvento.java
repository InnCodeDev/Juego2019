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
public class PopUpEvento extends DialogFragment {
    static String user = "";
    static Context c;

    public PopUpEvento(){

    }

    public static PopUpEvento newInstance(MainActivity cont, String turno, String dia, String inscriptos, String usuario, boolean inscripto, String nroturno) {
        PopUpEvento pop = new PopUpEvento();
        c=cont;
        Bundle args = new Bundle();
        args.putString("turn", turno);
        args.putString("Nroturn", nroturno);
        args.putString("di", dia);
        args.putString("user", usuario);
        args.putString("insc", inscriptos);
        args.putBoolean("bolinsc", inscripto);
        pop.setArguments(args);

        return pop;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evento, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView turno = (TextView) view.findViewById(R.id.textView3);
        TextView dia = (TextView) view.findViewById(R.id.textView4);
        TextView ins = (TextView) view.findViewById(R.id.textView5);

        turno.setText(getArguments().getString("turn"));
        dia.setText(getArguments().getString("di"));
        ins.setText(getArguments().getString("insc"));

        getDialog().setTitle("EVENTO");

        TextView btnTView = (TextView) view.findViewById(R.id.textView6);

        if (getArguments().getBoolean("bolinsc") == true){
            btnTView.setText("NO JUGAR");
        }else
            btnTView.setText("PARTICIPAR !");


        btnTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getArguments().getBoolean("bolinsc") == true){
                    System.out.println("Se va a dar de bajar?");
                    try {
                        JSONBorrarParticipacion json = new JSONBorrarParticipacion((MainActivity) c,getArguments().getString("turn"), getArguments().getString("user"), getArguments().getString("di"));
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  //  Toast.makeText(MainActivity.this,"Se va a dar de bajar?", Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("A PARTICIPAR !!");
                    try {
                        JSONInscripcionEvento json = new JSONInscripcionEvento((MainActivity) c,getArguments().getString("Nroturn"), getArguments().getString("user"), getArguments().getString("di"));
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        TextView btn = view.findViewById(R.id.lbl_participantes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("MOSTRAR USUARIOS... !!");
                try {
                    JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("turn"), getArguments().getString("di"), (TextView)view.findViewById(R.id.lbl_participantes), (TextView)view.findViewById(R.id.sc_participantes));
                    json.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




}
