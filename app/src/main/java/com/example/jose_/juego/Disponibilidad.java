package com.example.jose_.juego;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disponibilidad);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActualizarMiDisponibilidad();
            }
        });

        this.getDisponibilidad();
    }

    public void getDisponibilidad (){
        try {

            JSONGetDisponibilidad json = new JSONGetDisponibilidad(this);
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
            String dia=comb.substring(comb.lastIndexOf("-")+1,comb.length());

            CheckBox ck=null;

            int resID = getResources().getIdentifier( "checkBox"+ Integer.valueOf(turno)+""+Integer.valueOf(dia), "id", this.getPackageName());
            ck = (CheckBox) findViewById(resID);
            ck.setChecked(true);
//            System.out.println("DISPONIBILIDAD:  Dia " + Integer.valueOf(dia) + " - Turno: " + Integer.valueOf(turno));
        }
    }

    public void ActualizarMiDisponibilidad(){

        CheckBox ck;
        for(int i=1; i<13; i++){ //turno
            for(int j=1; j<7; j++){ //dia
                int resID = getResources().getIdentifier("checkBox"+i+""+j, "id", this.getPackageName());
                ck = (CheckBox) findViewById(resID);
                if (ck.isChecked()){
//                    System.out.println("ACT DISPONIBILIDAD: Turno: " + j + " -- Dia " + i );
                    disp=disp +i + "-" + j + "*";
                }
            }
        }
        System.out.println(disp);

        //llamar a JSONActualizarMiDisponibilidad y pasar String disp
        try {

            JSONActualizarMiDisponibilidad json = new JSONActualizarMiDisponibilidad(this, disp);
            json.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
