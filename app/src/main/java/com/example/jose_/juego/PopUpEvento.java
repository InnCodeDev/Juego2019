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
    TextView sc_particip;
    TextView btn;
    boolean viendoPart;

    public PopUpEvento(){

    }//this, TextoTurno, Fecha, FB_user, turno, inscripto, cantF5, cantF7 );
    public static PopUpEvento newInstance(MainActivity context, String TextoTurno, String Fecha, String User, String NroTurno, boolean inscripto, int CantF5, int CantF7){
        PopUpEvento pop = new PopUpEvento();
        c=context;
        int cantTotal = CantF5 + CantF7;
        Bundle args = new Bundle();
        args.putString("turn", TextoTurno);
        args.putString("Nroturn", NroTurno);
        args.putString("di", Fecha);
        args.putString("user", User);
        args.putString("insc", String.valueOf(0));
        args.putBoolean("bolinsc", inscripto);
        args.putString("cantTotal", String.valueOf(cantTotal));
        args.putString("cantF5", String.valueOf(CantF5));
        args.putString("cantF7", String.valueOf(CantF7));
        pop.setArguments(args);

        return pop;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evento, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView turno = (TextView) view.findViewById(R.id.textView3);
        TextView dia = (TextView) view.findViewById(R.id.textView4);
        final TextView sc = (TextView) view.findViewById(R.id.sc_particip);
        TextView insF5 = (TextView) view.findViewById(R.id.textView5);
        TextView insF7 = (TextView) view.findViewById(R.id.textView6);
        sc_particip = (TextView) view.findViewById(R.id.sc_particip);
        btn = view.findViewById(R.id.lbl_participantes);

        turno.setText(getArguments().getString("turn"));
        dia.setText(getArguments().getString("di"));
       // ins.setText(getArguments().getString("insc"));
        insF5.setText(getArguments().getString("cantF5"));
        insF7.setText(getArguments().getString("cantF7"));

        getDialog().setTitle("INSCRIPCION A EVENTO!");

        //Boton cancha 5
        TextView btnTView5 = (TextView) view.findViewById(R.id.textView61);
        btnTView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("A PARTICIPAR en Cancha 5!!");
                try {
                    JSONInscripcionEvento json = new JSONInscripcionEvento((MainActivity) c,getArguments().getString("Nroturn"), getArguments().getString("user"), getArguments().getString("di"), 5);
                    json.execute();
                    PopUpEvento.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Boton cancha 7
        TextView btnTView7 = (TextView) view.findViewById(R.id.textView62);
        btnTView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("A PARTICIPAR en Cancha 7!!");
                try {
                    JSONInscripcionEvento json = new JSONInscripcionEvento((MainActivity) c,getArguments().getString("Nroturn"), getArguments().getString("user"), getArguments().getString("di"), 7);
                    json.execute();
                    PopUpEvento.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Boton INDISTINTO
        TextView btnTViewIndist = (TextView) view.findViewById(R.id.textView63);
        btnTViewIndist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView btnC5 = (TextView) view.findViewById(R.id.textView5);
                TextView btnC7 = (TextView) view.findViewById(R.id.textView6);
                int c5 = Integer.valueOf(getArguments().getString("cantF5")); //Integer.valueOf(btnC5.getText().toString());
                int c7 = Integer.valueOf(getArguments().getString("cantF7")); // Integer.valueOf(btnC7.getText().toString());
                int cancha = 5; //por defecto en caso que sean igual cantidad
                if (c5 <= c7){
                    cancha = 7;
                }
                System.out.println("A PARTICIPAR en cancha " + cancha + " !! " );
                try {
                    JSONInscripcionEvento json = new JSONInscripcionEvento((MainActivity) c,getArguments().getString("Nroturn"), getArguments().getString("user"), getArguments().getString("di"), cancha);
                    json.execute();
                    PopUpEvento.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        //Asigna evento Click de VerParticipantes
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viendoPart == false){
                    System.out.println("Mostrando USUARIOS... !!");
                    try {
                        btn.setText("Ocultar Participantes");
                        viendoPart = true;
                        JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_particip);
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Ocultando USUARIOS... !!");
                    try {
                        btn.setText("Ver Participantes");
                        viendoPart = false;
                        sc_particip.setText("");
                        //JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_particip);
                       // json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*
                if (btn.getText().toString().compareTo("Ocultar Participantes") == 0){
                    btn.setText("Ver Participantes");
                    sc_particip.setText("");
                }else{
                    System.out.println("MOSTRAR USUARIOS... !!");
                    try {
                        btn.setText("Ocultar Participantes");
                        JSONmostrarUsuarios json = new JSONmostrarUsuarios(view,(MainActivity) c, getArguments().getString("Nroturn"), getArguments().getString("di"), btn, sc_particip);
                        json.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                */
            }
        });
    }




}
