package com.example.jose_.juego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class UbicacionFragment extends android.support.v4.app.Fragment {

    public UbicacionFragment (){

    }

    @Override
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

        ImageView img = view.findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://www.google.com/maps/place/El+Circulo+Futbol/@-31.3654313,-64.2557612,17z/data=!4m5!3m4!1s0x0:0x46eaedcd953fc86!8m2!3d-31.3655412!4d-64.2546025";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        Button btnCall = view.findViewById(R.id.button);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:03517666352"));
                startActivity(callIntent);
            }
        });
    }

}
