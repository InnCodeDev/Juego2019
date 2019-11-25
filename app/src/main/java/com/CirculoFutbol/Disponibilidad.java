package com.CirculoFutbol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Iterator;

public class Disponibilidad extends AppCompatActivity {
    ArrayList array = null;
    String disp="";
    String FB_user="";
    String FB_mail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disponibilidad);

        Intent a = getIntent();
        FB_user = a.getExtras().getString("user");
        FB_mail = a.getExtras().getString("mail");

        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActualizarMiDisponibilidad();
            }
        });

        this.getDisponibilidad();
    }

    public void getDisponibilidad (){
        try {

            JSONGetDisponibilidad json = new JSONGetDisponibilidad(this, this.FB_user, this.FB_mail);
            json.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void continuarJSONDisponibilidad(ArrayList a){

        Iterator I = a.iterator();
        while(I.hasNext()){
            String comb = (String) I.next();
            String turno= comb.substring(0,comb.lastIndexOf("-"));
            String dia=comb.substring(comb.lastIndexOf("-")+1);

            CheckBox ck=null;

            int resID = getResources().getIdentifier( "checkBox"+ Integer.valueOf(turno)+""+Integer.valueOf(dia), "id", this.getPackageName());
            ck = findViewById(resID);
            ck.setChecked(true);
//            System.out.println("DISPONIBILIDAD:  Dia " + Integer.valueOf(dia) + " - Turno: " + Integer.valueOf(turno));
        }
    }

    public void ActualizarMiDisponibilidad(){

        CheckBox ck;
        for(int i=3; i<19; i++){ //turno
            for(int j=1; j<7; j++){ //dia
                System.out.println("ACT DISPONIBILIDAD: Turno: " + j + " -- Dia " + i );
                int resID = getResources().getIdentifier("checkBox"+i+""+j, "id", this.getPackageName());
                ck = findViewById(resID);
                if (ck.isChecked()){

                    disp=disp +i + "-" + j + "*";
                }
            }
        }
        System.out.println(disp);

        //llamar a JSONActualizarMiDisponibilidad y pasar String disp
        try {

            JSONActualizarMiDisponibilidad json = new JSONActualizarMiDisponibilidad(this, disp, FB_user, FB_mail);
            json.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
