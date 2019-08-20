package com.example.jose_.juego;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UbicacionFragment extends android.support.v4.app.Fragment {

    public UbicacionFragment (){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
  /*      bundle = this.getArguments();
        if (bundle != null) {
            tercera = bundle.getStringArrayList("a");
            terceraM = bundle.getStringArrayList("b");
            terceraZ = bundle.getStringArrayList("c");
            terceraX = bundle.getStringArrayList("d");
        }
  */      View v = inflater.inflate(R.layout.fragment_ubicacion, container, false);
        return v;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
