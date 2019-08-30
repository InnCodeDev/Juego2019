package com.example.jose_.juego;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jose_ on 27/8/2018.
 */
public class PopUpMail extends DialogFragment {
    static String user = "";
    static Context c;
    TextView sc_particip;
    TextView btn;
    boolean viendoPart;

    public PopUpMail(){ }

    public static PopUpMail newInstance(MainActivity context, String User, String UserMail){
        PopUpMail pop = new PopUpMail();
        c=context;
        Bundle args = new Bundle();
        args.putString("user", User);
        args.putString("userMail", UserMail);
        pop.setArguments(args);

        return pop;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mail, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView user = (TextView) view.findViewById(R.id.textView3);
        TextView para = (TextView) view.findViewById(R.id.textView4);
        final EditText txtMsj = (EditText) view.findViewById(R.id.editText);

        btn = view.findViewById(R.id.textView63);

        user.setText(getArguments().getString("user") + " - " + getArguments().getString("userMail") );
        para.setText("Admin - Circulo Futbol");
       // ins.setText(getArguments().getString("insc"));

        getDialog().setTitle("INSCRIPCION A EVENTO!");

        //Boton INDISTINTO
        TextView btnTViewIndist = (TextView) view.findViewById(R.id.textView63);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
            //        JSONPopUpMail jsonMial = new JSONPopUpMail((MainActivity) c,getArguments().getString("user"), getArguments().getString("userMail"), txtMsj.toString());
            //        jsonMial.execute();
                    PopUpMail.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }




}
