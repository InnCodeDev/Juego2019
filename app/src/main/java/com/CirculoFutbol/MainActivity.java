package com.CirculoFutbol;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class  MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList primera, primeraM, segunda, segundaM, tercera, terceraM, cuarta, cuartaM = new ArrayList();
    ArrayList primeraZ, segundaZ, terceraZ, cuartaZ  = new ArrayList();
    ArrayList primeraX, segundaX, terceraX, cuartaX  = new ArrayList();

    Calendar cal;
    FirebaseAuth mAuth;
    String FB_mail, FB_user, FB_foto;
    PopUpEvento popEvento;
    String minDay = "";
    Calendar cal2;
    int publicidad;

    // FragmentPagerAdapter adapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;

    Bundle bundleP, bundleS, bundleT, bundleC;// = new Bundle();
    primera pri;
    segunda seg;
    tercera ter;
    cuarta cuar;
    UbicacionFragment ubicFrag;
    SectionsPagerAdapter mSectionsPagerAdapter;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cal = Calendar.getInstance();
        getDiaSemana();
        cal2 = cal; //Calendar.getInstance();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        minDay = formateador.format(cal2.getTime());
        System.out.println("MINIMO DIA: " + minDay);

        publicidad = 0;

        bundleP = new Bundle();
        bundleS = new Bundle();
        bundleT = new Bundle();
        bundleC = new Bundle();

        pri = new primera();
        seg = new segunda();
        ter = new tercera();
        cuar = new cuarta();

        ubicFrag = new UbicacionFragment();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Aca estaba codigo de Floating Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONEventos(false);
                Snackbar.make(view, "Actualizando grillas.. aguarde! ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Busca los turnos que no tienen disponibilidad
        JSONCargarStocks jsonCargar = new JSONCargarStocks(this,minDay, false);
        jsonCargar.execute();


        ejecutar1erJSON();


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            View headerLayout = navigationView.getHeaderView(0);

            FB_user = mAuth.getCurrentUser().getDisplayName();
            FB_mail = mAuth.getCurrentUser().getEmail();
            FB_foto = this.getIntent().getStringExtra("FotoURL"); //mAuth.getCurrentUser().getPhotoUrl().toString();
            System.out.println("Usuario: " + FB_user + " - Foto: " + FB_foto + " --- " + mAuth.getCurrentUser().getPhotoUrl().toString());

            TextView t_user = headerLayout.findViewById(R.id.nombreUser); //navigationView.findViewById(R.id.nombreUser);
            t_user.setText(FB_user);
            TextView t_mail = headerLayout.findViewById(R.id.mailUser); //navigationView.findViewById(R.id.mailUser);
            t_mail.setText(FB_mail);

            ImageView t_img = headerLayout.findViewById(R.id.imageUser);

            Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl().toString())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(t_img);

            JSONRegistrarUsuario jsonUsuario = new JSONRegistrarUsuario (this, FB_user, FB_mail);
            jsonUsuario.execute();

        } else {
            mAuth.signOut();
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                mViewPager.getChildAt(i + 1);
        }

    }

    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
    */
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.ContainerGral);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            findViewById(R.id.Titulo).setVisibility(View.VISIBLE);
            findViewById(R.id.Subtitulo).setVisibility(View.VISIBLE);
            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment).commit();

            } else {
                super.onBackPressed();
            }
    //    }
    }

    public void ShowInterstial (){
        if (publicidad >= 5){
            MobileAds.initialize(this, this.getResources().getString(R.string.app_ad_unit_id));
            mInterstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.interstitial_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Toast.makeText(getApplicationContext(),
                            "onAdFailedToLoad() with error code: " + errorCode,
                            Toast.LENGTH_SHORT).show();
                    System.out.println("EROOOOOOOOOOOOOOOOOOOOOOOOOR " + errorCode);
                }
                }
            );

            publicidad = 0;

            System.out.println("INSTERTITIAL CREADO!!!");
        }else
            publicidad ++;
        System.out.println("CONTADOR INSTERTITIAL : " + publicidad);
    }


    public void cargarFullStock(){
        JSONCargarFullStocks jFULL = new JSONCargarFullStocks(this, minDay, false);
        jFULL.execute();
    }

    public void ejecutar1erJSON(){
        bundleP = new Bundle();
        bundleS = new Bundle();
        bundleT = new Bundle();
        bundleC = new Bundle();
        JSONCargarEventos json = new JSONCargarEventos(this, minDay, false);
        json.execute();

    }

    public void ejecutar2doJSON(){
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        JSONEventosUsuario jsonE = new JSONEventosUsuario(this, formateador.format(cal2.getTime()), FB_user, false); //cal.getTime()),"Jose");
        jsonE.execute();
      //  ReloadALL(false);
    }

    public void ReloadALL(Boolean fromPopUP){
        //Determina cual es el fragment activo y recargar ese
        //Recargar ese y los del costado
        System.out.println("ITEMMMMMMMMMMMMMMMMMMMMMMMM: " + mViewPager.getCurrentItem());
        switch (mViewPager.getCurrentItem()) {
            case 0:
                pri.RELOADFRAGMENT(bundleP, primera, primeraM, primeraZ, primeraX);
                seg.RELOADFRAGMENT(bundleS, segunda, segundaM, segundaZ, segundaX);
                break;
            case 1:
                pri.RELOADFRAGMENT(bundleP, primera, primeraM, primeraZ, primeraX);
                seg.RELOADFRAGMENT(bundleS, segunda, segundaM, segundaZ, segundaX);
                ter.RELOADFRAGMENT(bundleT, tercera, terceraM, terceraZ, terceraX);
                break;
            case 2:
                seg.RELOADFRAGMENT(bundleS, segunda, segundaM, segundaZ, segundaX);
                ter.RELOADFRAGMENT(bundleT, tercera, terceraM, terceraZ, terceraX);
                cuar.RELOADFRAGMENT(bundleC, cuarta, cuartaM, cuartaZ, cuartaX);
                break;
            case 3:
                ter.RELOADFRAGMENT(bundleT, tercera, terceraM, terceraZ, terceraX);
                cuar.RELOADFRAGMENT(bundleC, cuarta, cuartaM, cuartaZ, cuartaX);
                break;
            case 4:
                mViewPager.setAlpha(0f);
                break;
        }
    }

    public void JSONEventos(Boolean fromPopUP) {
        ejecutar1erJSON();
    }

    public void cargarBundleEventos (){
        bundleP.putStringArrayList("a", primera);
        bundleS.putStringArrayList("a", segunda);
        bundleT.putStringArrayList("a", tercera);
        bundleC.putStringArrayList("a", cuarta);

        cargarBundleEventosUsuario ();
    }

    public void cargarBundleEventosUsuario (){
        bundleP.putStringArrayList("b", primeraM);
        bundleS.putStringArrayList("b", segundaM);
        bundleT.putStringArrayList("b", terceraM);
        bundleC.putStringArrayList("b", cuartaM);

        bundleP.putStringArrayList("c", primeraZ);
        bundleS.putStringArrayList("c", segundaZ);
        bundleT.putStringArrayList("c", terceraZ);
        bundleC.putStringArrayList("c", cuartaZ);

        bundleP.putStringArrayList("d", primeraX);
        bundleS.putStringArrayList("d", segundaX);
        bundleT.putStringArrayList("d", terceraX);
        bundleC.putStringArrayList("d", cuartaX);

        this.terminarJSONEVENTOS();
    }

    public void terminarJSONEVENTOS(){
        pri.setArguments(bundleP);
        seg.setArguments(bundleS);
        ter.setArguments(bundleT);
        cuar.setArguments(bundleC);

      //  pri.actualizarBundle(bundleP, primera, primeraM);
      //  seg.actualizarBundle(bundleS, segunda, segundaM);
        this.ReloadALL(true);

     //  System.out.println("...........SALIENDO DE JSONEVENTOS................");
    }

    public void continuarJSONEmostrarUsuarios(View v, TextView bt, TextView s, ArrayList a){

        ArrayList usuarios = a;
        String btn = getResources().getResourceEntryName(bt.getId());
        String lbl_sc = getResources().getResourceEntryName(s.getId());

        System.out.println("ON CLICK USUARIOSSS " + btn + " -- " + lbl_sc );

        s.setMovementMethod(new ScrollingMovementMethod());

        s.setHeight(150);
        //bt.setText("Ocultar Participantes\n");

        Iterator I = usuarios.iterator();
        System.out.println("Cantidad de participantes: " + usuarios.size());

        if (usuarios.size() > 0) {
            s.append("PARTICIPANTES: \n");
            while (I.hasNext()) {
                String user = (String) I.next();
                System.out.println("USUARIOS: " + user);
                s.append(user + "\n");
            }
        }
        s.setMaxLines(5);

    }

    public void onClickEvento(View v) {
        String semana = "";
        String NroTurno="";
        String dia= "";
        String btn="";
        int resID;
        String tviewTurno = ""; //textView1010 - 1020 - 1030
        String tviewDia = ""; //textView1001 al 1006

        System.out.println("ON CLICK EVENTOOOOOOOOOOOOO   - " + getResources().getResourceEntryName(v.getId()));
        btn = getResources().getResourceEntryName(v.getId()); //textView1073

        semana = btn.substring(btn.indexOf("w") + 1, btn.indexOf("w") + 2);//1
        NroTurno = btn.substring(btn.length() - 3, btn.length() - 1);//07
        dia = btn.substring(btn.length() - 1);//3


        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        Calendar aux = cal;
        long r;

        Calendar c = Calendar.getInstance();
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.DAY_OF_MONTH, Integer.valueOf(minDay.substring(0, 2)));
        cc.set(Calendar.MONTH, Integer.valueOf(minDay.substring(3, 5)) - 1);
        cc.set(Calendar.YEAR, Integer.valueOf(minDay.substring(6, 10)));
        cc.add(Calendar.DAY_OF_MONTH, Integer.valueOf(dia) - 1);
        c.setTimeInMillis(System.currentTimeMillis());

        //Verificar que la fecha y turno que se seleccionó sea futura, no haya pasado
        //El dia minimo de la semana + los dias desplazados del lunes, tiene que ser mayor a la fecha de hoy



            tviewTurno = NroTurno;
            Calendar aux2 = cal;
            try {
                switch (semana) {
                    case "1":
                        aux.setTime((formateador).parse(minDay));
                        aux.add(Calendar.DAY_OF_MONTH, Integer.valueOf(dia) - 1);
                        tviewDia = formateador.format(aux.getTime());
                        aux.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dia) + 1);
                        break;
                    case "2":
                        aux.setTime((formateador).parse(minDay));
                        aux.add(Calendar.DAY_OF_MONTH, Integer.valueOf(dia) + 6);
                        tviewDia = formateador.format(aux.getTime());
                        aux.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dia) - 6);
                        break;
                    case "3":
                        aux.setTime((formateador).parse(minDay));
                        aux.add(Calendar.DAY_OF_MONTH, Integer.valueOf(dia) + 13);
                        tviewDia = formateador.format(aux.getTime());
                        aux.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dia) - 13);
                        break;
                    case "4":
                        aux.setTime((formateador).parse(minDay));
                        aux.add(Calendar.DAY_OF_MONTH, Integer.valueOf(dia) + 20);
                        tviewDia = formateador.format(aux.getTime());
                        aux.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dia) - 20);
                        break;
                    default:
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            resID = getResources().getIdentifier("textView"+semana+NroTurno+"0","id",this.getPackageName());
            tviewTurno = ((TextView) findViewById(resID)).getText().toString();

            resID = getResources().getIdentifier(btn, "id", this.getPackageName());
            TextView tview = findViewById(resID);

            if (((ColorDrawable) tview.getBackground()).getColor() == Color.LTGRAY) {
                Toast.makeText(this, "Turno no disponible", Toast.LENGTH_SHORT).show();
            }else {
                if (semana.compareTo("1") == 0 && c.getTime().compareTo(cc.getTime()) > 0) {
                    Toast.makeText(MainActivity.this, "Elija una fecha posterior al dia de hoy!!", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("TURNO: " + NroTurno + " -- TViewTurno: "  + tviewTurno + " - TViewDia: " + tviewDia);

                    boolean inscripto = false;
                    //Si BackgroundColor es Verde; esta inscripto.. si es gris (o no verde) se puede inscripto = falso
                    if (((ColorDrawable) tview.getBackground()).getColor() == Color.GREEN) {
                        inscripto = true; //Esta Participando
                    }

                    //MainActivity disp, View v, String min, String TextoT, String tur, boolean fromPop, boolean inscr)
                    JSONDetalleEvento jsonD = new JSONDetalleEvento(this, v, tviewDia, tviewTurno, NroTurno,  true, inscripto);
                    jsonD.execute();
                    //String inscriptos = tview.getText().toString();
                }
            }
        }

    public void ContinuarOnClickEvento (View v, String Fecha, String TextoTurno, String Nroturno,  Boolean fromPopUp, Boolean inscripto, ArrayList ar){
        int CantF5=0;
        int CantF7=0;

        Iterator I = ar.iterator();
        while (I.hasNext()) {
            String t = (String) I.next(); //5/12  - 7/30
            if (t.substring(0, t.indexOf("/")).compareTo("5") == 0){
                CantF5 = Integer.valueOf(t.substring(t.indexOf("/")+1));
            }else
                CantF7 = Integer.valueOf(t.substring(t.indexOf("/")+1));
        }
        System.out.println(ar.size() + "...........CANCHA 5: " + CantF5 + " -- Cancha 7: " + CantF7);

        if (inscripto == true){
            //MainActivity context, String TextoTurno, String Fecha, String User, String NroTurno, boolean inscripto, String CantF5, String CantF7)
            PopUpEventoBorrarse popEventoBorrar = PopUpEventoBorrarse.newInstance(this, TextoTurno, Fecha, FB_user, Nroturno, inscripto, CantF5, CantF7 );
            popEventoBorrar.show(getFragmentManager(), "BORRAR PARTICIPACION!");
        }else{ //No esta inscripto
            //MainActivity cont, String turno, String dia, String usuario, String Nrotur, boolean inscripto, String F5, String F7) {
            popEvento = PopUpEvento.newInstance(this, TextoTurno, Fecha, FB_user, Nroturno, inscripto, CantF5, CantF7 );
            popEvento.show(getFragmentManager(), "INSCRIPCION A EVENTO!"); //turno, dia, "user"); //fm, "fragment_edit_name");

        }  //MainActivity context, String TextoTurno, String Fecha, String User, String NroTurno, boolean inscripto, int CantF5, int CantF7)


        this.ShowInterstial(); //incrementa en +1 el contador para el banner grande
    }

    //Tiene cargado UNICAMENTE los turnos que estan completos
    public void continuarJSONCargarFullStock(ArrayList a, boolean fromPopUp) {
        primeraX = new ArrayList();
        segundaX = new ArrayList();
        terceraX = new ArrayList();
        cuartaX = new ArrayList();

        ArrayList eventosSemana = new ArrayList();
        ArrayList arrayEvento = a; //new ArrayList<String>();

        Iterator I = arrayEvento.iterator();
        String turno, fecha, cant = "", turn = "";
        Date da = new Date();

        int semana = 0;
        while (I.hasNext()) {               //dia + "*" + turno + "*" + cantidad
            String t = (String) I.next();   //24-06-2019*6*0
            fecha = t.substring(0, t.indexOf("*"));
            turno = t.substring(t.indexOf("*") + 1, t.lastIndexOf("*"));
            cant = t.substring(t.lastIndexOf("*") + 1); //t.indexOf("/"));
            //   cancha = t.substring(t.indexOf("/")+1, t.length());

            if (turno.length() == 1) {
                turn = "0" + turno;
            } else turn = turno;

            String dia = (fecha.substring(0, fecha.indexOf("-")));
            if (dia.length() == 1)
                dia = "0" + dia;
            int mes = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 1, fecha.indexOf("-") + 3));
            int ano = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 4));
            System.out.println("------ " + t + " --- " + dia + " /-/ " + mes + " /-/ " + ano);  //t = 12*15-11-2018*9

            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            da.setDate(Integer.valueOf(dia));
            da.setMonth(mes - 1);
            da.setYear(ano - 1900);

            long sem = 0;
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date dat2 = formatoDelTexto.parse(minDay);

                //Fecha del evento - Fecha Menor dia de la primer semana
                sem = (da.getTime() - dat2.getTime()) / (1000 * 60 * 60 * 24); //Diferencia de dias entre ambas fechas
                float div = (float)sem/6;
                BigDecimal bigDecimal = new BigDecimal(div).setScale(2, RoundingMode.UP);

                if (bigDecimal.doubleValue() < 1) {
                    semana = 1;
                } else {
                    if (bigDecimal.doubleValue() <= 2) {
                        semana = 2;
                    } else {
                        if (bigDecimal.doubleValue() < 3.5) {
                            semana = 3;
                        } else {
                            semana = 4;
                        }
                    }
                }
       //         System.out.println("SEMMM_CargarEventos " + sem + " -- " + semana + " -- " + bigDecimal.doubleValue());

        //        System.out.println("SEMANA: " + da.toLocaleString() + "  - Semana: " + semana + " -- " + cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da) + " -- " + formateador.format(dat2));
//cancha + "#"+
                eventosSemana.add(cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da));
                //5#1*textView1-02/12/04/2019
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

        Iterator I2 = eventosSemana.iterator();
        int resID = 0;
        while (I2.hasNext()) {
            String txt = (String) I2.next();  //VERSION2:  //5#1*textView1-02/12/04/2019
            //System.out.println("TXT : " + txt); //  7*textView1-02/02/11/2018
            //  6*textView2-02/13/11/2018
            //   String CantCancha = txt.substring(0, txt.indexOf("#"));
            String cant1 = txt.substring(0, txt.indexOf("*"));
            String tur = txt.substring(txt.indexOf("-") + 1, txt.indexOf("/"));
            String semana1 = txt.substring(txt.indexOf("w") + 1, txt.indexOf("w") + 2);
            String fech = txt.substring(txt.indexOf("/") + 1); //Fecha real del evento

            long r = 0;
            try {
                Date date1 = new Date();
                Date date2;

                date1.setDate(Integer.valueOf(fech.substring(0, 2)));
                date1.setMonth(Integer.valueOf(fech.substring(3, 5)) - 1);
                date1.setYear(Integer.valueOf(fech.substring(6)) - 1900);
                date1.setHours(0);
                date1.setMinutes(0);
                date1.setSeconds(0);
                cal2 = cal;

                switch (semana1) {
                    case "1":
                        //       System.out.println("FECH 1: " + fech);
                        //       System.out.println("MINIMO DIA 1: " + formateador.format(cal2.getTime()));
                        cal2.add(Calendar.DAY_OF_MONTH, 0);
                        date2 = formateador.parse(minDay);
                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
                        // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                        // System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                        //       System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        primeraX.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, 0);
                        break;

                    case "2":
                        //      System.out.println("FECH 2: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 7);
                        System.out.println("MINIMO DIA 2: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                        // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                        // System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                        //    System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        segundaX.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -7);
                        break;

                    case "3":
                        //        System.out.println("FECH 3: " + fech);

                        cal2.add(Calendar.DAY_OF_MONTH, 14);
                        //       System.out.println("MINIMO DIA 3: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                        // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                        //  System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                        //       System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        terceraX.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -14);
                        break;

                    case "4":
                        //     System.out.println("FECH 4: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 21);
                        //      System.out.println("MINIMO DIA 4: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                        //  System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                        //  System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                        //      System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        cuartaX.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -21);
                        break;
                    default:
                        break;
                }

                System.out.println("SQL FULL STOCK:" + cant1 + "*" + "textView" + semana1 + tur + r);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ARRAYS FULLSTOCK: " + primeraX.size() + " - " + segundaX.size() + " - " + terceraX.size() + " - " + cuartaX.size());

        ejecutar2doJSON();

    }

    //Tiene cargado UNICAMENTE los turnos que no tienen disponibilidad
    public void continuarJSONCargarStock(ArrayList a, boolean fromPopUp) {
        primeraZ = new ArrayList();
        segundaZ = new ArrayList();
        terceraZ = new ArrayList();
        cuartaZ = new ArrayList();

        ArrayList eventosSemana = new ArrayList();
        ArrayList arrayEvento = a; //new ArrayList<String>();

        Iterator I = arrayEvento.iterator();
        String turno, fecha, cant = "", turn = "", cancha;
        Date da = new Date();

        int semana = 0;
        while (I.hasNext()) {               //dia + "*" + turno + "*" + cantidad
            String t = (String) I.next();   //24-06-2019*6*0
         //   System.out.println("JJJJJJJJ... " + t);
            fecha = t.substring(0, t.indexOf("*"));
            turno = t.substring(t.indexOf("*") + 1, t.lastIndexOf("*"));
            cant = t.substring(t.lastIndexOf("*") + 1); //t.indexOf("/"));
            //   cancha = t.substring(t.indexOf("/")+1, t.length());

            if (turno.length() == 1) {
                turn = "0" + turno;
            } else turn = turno;

            String dia = (fecha.substring(0, fecha.indexOf("-")));
            if (dia.length() == 1)
                dia = "0" + dia;
            int mes = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 1, fecha.indexOf("-") + 3));
            int ano = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 4));
            //System.out.println("------ " + t + " --- " + dia + " /-/ " + mes + " /-/ " + ano);  //t = 12*15-11-2018*9

            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            da.setDate(Integer.valueOf(dia));
            da.setMonth(mes - 1);
            da.setYear(ano - 1900);

            long sem = 0;
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date dat2 = formatoDelTexto.parse(minDay);

         /*       dat2.setDate(Integer.valueOf(formatoDelTexto.format(minDay).substring(0,formatoDelTexto.format(minDay).indexOf("/"))));
                da.setMonth(Integer.valueOf(formatoDelTexto.format(minDay).substring(formatoDelTexto.format(minDay).indexOf("/")+1,formatoDelTexto.format(minDay).lastIndexOf("/"))) - 1);
                da.setYear(Integer.valueOf(formatoDelTexto.format(minDay).substring(formatoDelTexto.format(minDay).lastIndexOf("/")+1, formatoDelTexto.format(minDay).length())) - 1900);
       */         //formateador.parse(minDay);

                //Fecha del evento - Fecha Menor dia de la primer semana
                sem = (da.getTime() - dat2.getTime()) / (1000 * 60 * 60 * 24); //Diferencia de dias entre ambas fechas
                float div = (float)sem/6;
                BigDecimal bigDecimal = new BigDecimal(div).setScale(2, RoundingMode.UP);

                if (bigDecimal.doubleValue() < 1) {
                    semana = 1;
                } else {
                    if (bigDecimal.doubleValue() <= 2) {
                        semana = 2;
                    } else {
                        if (bigDecimal.doubleValue() <= 3.3) {
                            semana = 3;
                        } else {
                            semana = 4;
                        }
                    }
                }
                System.out.println("SEMMM_CargarEventos " + sem + " -- " + semana + " -- " + bigDecimal.doubleValue());

                System.out.println("SEMANA: " + da.toLocaleString() + "  - Semana: " + semana + " -- " + cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da) + " -- " + formateador.format(dat2));
//cancha + "#"+
                eventosSemana.add(cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da));
                //5#1*textView1-02/12/04/2019
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

        Iterator I2 = eventosSemana.iterator();
        int resID = 0;
        while (I2.hasNext()) {
            String txt = (String) I2.next();  //VERSION2:  //5#1*textView1-02/12/04/2019
            //System.out.println("TXT : " + txt); //  7*textView1-02/02/11/2018
            //  6*textView2-02/13/11/2018
            //   String CantCancha = txt.substring(0, txt.indexOf("#"));
            String cant1 = txt.substring(0, txt.indexOf("*"));
            String tur = txt.substring(txt.indexOf("-") + 1, txt.indexOf("/"));
            String semana1 = txt.substring(txt.indexOf("w") + 1, txt.indexOf("w") + 2);
            String fech = txt.substring(txt.indexOf("/") + 1); //Fecha real del evento

            long r = 0;
            try {
                Date date1 = new Date();
                Date date2;

                date1.setDate(Integer.valueOf(fech.substring(0, 2)));
                date1.setMonth(Integer.valueOf(fech.substring(3, 5)) - 1);
                date1.setYear(Integer.valueOf(fech.substring(6)) - 1900);
                date1.setHours(0);
                date1.setMinutes(0);
                date1.setSeconds(0);
                cal2 = cal;

                switch (semana1) {
                    case "1":
                 //       System.out.println("FECH 1: " + fech);
                 //       System.out.println("MINIMO DIA 1: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(minDay);
                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
                       // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                       // System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                 //       System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        primeraZ.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        break;

                    case "2":
                  //      System.out.println("FECH 2: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 7);
                        System.out.println("MINIMO DIA 2: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                       // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                       // System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                    //    System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        segundaZ.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -7);
                        break;

                    case "3":
                //        System.out.println("FECH 3: " + fech);

                        cal2.add(Calendar.DAY_OF_MONTH, 14);
                 //       System.out.println("MINIMO DIA 3: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                       // System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                      //  System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                 //       System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        terceraZ.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -14);
                        break;

                    case "4":
                   //     System.out.println("FECH 4: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 21);
                  //      System.out.println("MINIMO DIA 4: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

                      //  System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
                      //  System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
                  //      System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        cuartaZ.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -21);
                        break;
                    default:
                        break;
                }

         /*       String txF = txt.substring(txt.indexOf("*") + 1, txt.indexOf("w") + 2) + tur + r;
                System.out.println("PRII: " + txF + " - Cant: " + cant1);

                resID = getResources().getIdentifier(txF, "id", this.getPackageName());
                TextView ta = (TextView) this.findViewById(resID);
                ta.setText(cant1);
*/
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ARRAYS: " + primeraZ.size() + " - " + segundaZ.size() + " - " + terceraZ.size() + " - " + cuartaZ.size());

        cargarFullStock();
        //ejecutar2doJSON();
    }

    public void continuarJSONEventosUsuario(ArrayList a, boolean fromPopUp) {
        primeraM = new ArrayList();
        segundaM = new ArrayList();
        terceraM = new ArrayList();
        cuartaM = new ArrayList();

        ArrayList eventosUsuario = new ArrayList();
        String fecha = "";
        String turn = "";
        int semana = 0;

        if (a.size() > 0){
            ArrayList<String> arrayEvento = a; //new ArrayList<String>();

            Iterator I = arrayEvento.iterator();
            while (I.hasNext()) {
                String t = (String) I.next();  //7*28-09-2018 turno*fecha
  //              System.out.println("BBBBBBBBB: " + t); //2*02-11-2018*7
                String turno = t.substring(0, t.indexOf("*"));
                fecha = t.substring(t.indexOf("*") + 1);
//            int cant = Integer.valueOf(t.substring(t.lastIndexOf("*")+1),t.length());

                String dia = (fecha.substring(0, fecha.indexOf("-")));
                turn = "";
                if (turno.length() == 1)
                    turn = "0" + turno;
                else turn = turno;

                if (dia.length() == 1)
                    dia = "0" + dia;
                int mes = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-")));
                int ano = Integer.valueOf(fecha.substring(fecha.lastIndexOf("-") + 1));
                //      System.out.println("------ " + t + " --- " + dia + " /-/ " + mes + " /-/ " + ano);

                Date da = new Date();
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                da.setDate(Integer.valueOf(dia));
                da.setMonth(mes - 1);
                da.setYear(ano - 1900);

                try {
                    long sem = 0;
                    Date dat2 = formateador.parse(minDay);//pri.getMinDay());

                    //Fecha del evento - Fecha Menor dia de la primer semana
                    sem = (da.getTime() - dat2.getTime()) / (1000 * 60 * 60 * 24); //Diferencia de dias entre ambas fechas
                    float div = (float)sem/6;
                    BigDecimal bigDecimal = new BigDecimal(div).setScale(2, RoundingMode.UP);

                    if (bigDecimal.doubleValue() < 1) {
                        semana = 1;
                    } else {
                        if (bigDecimal.doubleValue() <= 2) {
                            semana = 2;
                        } else {
                            if (bigDecimal.doubleValue() <= 3) {
                                semana = 3;
                            } else {
                                semana = 4;
                            }
                        }
                    }
     //               System.out.println("SEMMM_CargarEventosUSUARIO " + sem + " -- " + semana + " -- " + bigDecimal.doubleValue());

                    //       System.out.println("SEM: " + semana);
                    //       System.out.println("LAAAA textView" + semana + "-" + turn + "/" + formateador.format(da));
                    eventosUsuario.add("textView" + semana + "-" + turn + "/" + formateador.format(da));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Iterator I2 = eventosUsuario.iterator();
            int resID = 0;
            while (I2.hasNext()) {
                String txt = (String) I2.next(); //  textView1-02/13/11/2018
                String tur = txt.substring(txt.indexOf("-") + 1, txt.indexOf("/"));
                String semana1 = txt.substring(txt.indexOf("w") + 1, txt.indexOf("w") + 2);
                String fech = txt.substring(txt.indexOf("/") + 1); //Fecha real del evento

                long r = 0;
                try {
                    Date date1 = new Date();
                    date1.setDate(Integer.valueOf(fech.substring(0, 2)));
                    date1.setMonth(Integer.valueOf(fech.substring(3, 5)) - 1);
                    date1.setYear(Integer.valueOf(fech.substring(6)) - 1900);
                    date1.setHours(0);
                    date1.setMinutes(0);
                    date1.setSeconds(0);
                    Date date2;
                    cal2 = cal;
                    switch (semana1) {
                        case "1":
                            cal2.add(Calendar.DAY_OF_MONTH, 0);
                            System.out.println("MINIMO-DIA-1: " + formateador.format(cal2.getTime()));
                            date2 = formateador.parse(formateador.format(cal2.getTime()));

                            r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        //                   System.out.println("eventosUsuario: " + date1.toLocaleString() + " - " + "textView" + semana1 + tur + r);
                            primeraM.add("textView" + semana1 + tur + r);
                            cal2.add(Calendar.DAY_OF_MONTH, 0);
                            break;

                        case "2":
                            cal2.add(Calendar.DAY_OF_MONTH, 7);
                            System.out.println("MINIMO-DIA-2: " + formateador.format(cal2.getTime()));
                            date2 = formateador.parse(formateador.format(cal2.getTime()));

                            r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
         //                   System.out.println("eventosUsuario: " + date1.toLocaleString() + " - " + "textView" + semana1 + tur + r);
                            segundaM.add("textView" + semana1 + tur + r);
                            cal2.add(Calendar.DAY_OF_MONTH, -7);
                            break;

                        case "3":
                            cal2.add(Calendar.DAY_OF_MONTH, 14);
                            System.out.println("MINIMO-DIA-3: " + formateador.format(cal2.getTime()));
                            date2 = formateador.parse(formateador.format(cal2.getTime()));

                            r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
          //                  System.out.println("eventosUsuario: " + date1.toLocaleString() + " - " + "textView" + semana1 + tur + r);
                            terceraM.add("textView" + semana1 + tur + r);
                            cal2.add(Calendar.DAY_OF_MONTH, -14);
                            break;

                        case "4":
                            cal2.add(Calendar.DAY_OF_MONTH, 21);
                            System.out.println("MINIMO-DIA-4: " + formateador.format(cal2.getTime()));
                            date2 = formateador.parse(formateador.format(cal2.getTime()));

                            r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
         //                   System.out.println("eventosUsuario: " + date1.toLocaleString() + " - " + "textView" + semana1 + tur + r);
                            cuartaM.add("textView" + semana1 + tur + r);
                            cal2.add(Calendar.DAY_OF_MONTH, -21);
                            break;
                    }

                    String txF = txt.substring(0, txt.indexOf("w") + 2) + tur + r;
                    System.out.println("eventosUsuario  TXF: " + txF);

                    resID = getResources().getIdentifier(txF, "id", this.getPackageName());
                    TextView ta = this.findViewById(resID);
                    // ta.setText(cant1);
//                ta.setBackgroundColor(Color.GREEN);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        cargarBundleEventos ();
    }

    public void continuarJSONCargarEventos(ArrayList a, boolean fromPopUp) {
        primera = new ArrayList();
        segunda = new ArrayList();
        tercera = new ArrayList();
        cuarta = new ArrayList();

        ArrayList eventosSemana = new ArrayList();
        ArrayList arrayEvento = a; //new ArrayList<String>();
        ArrayList Fut5, Fut7 = new ArrayList();

        Iterator I = arrayEvento.iterator();
        String turno, fecha, cant = "", turn = "", cancha;
        Date da = new Date();

        int semana = 0;
        while (I.hasNext()) {               //turno + "*" + dia + "*" + cantidad
            String t = (String) I.next();   //7*28-09-2018*2
            System.out.println("AAAAAAAAAA... " + t);
            turno = t.substring(0, t.indexOf("*"));
            fecha = t.substring(t.indexOf("*") + 1, t.lastIndexOf("*"));
            cant = t.substring(t.lastIndexOf("*") + 1); //t.indexOf("/"));
         //   cancha = t.substring(t.indexOf("/")+1, t.length());

            if (turno.length() == 1) {
                turn = "0" + turno;
            } else turn = turno;

            String dia = (fecha.substring(0, fecha.indexOf("-")));
            if (dia.length() == 1)
                dia = "0" + dia;
            int mes = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 1, fecha.indexOf("-") + 3));
            int ano = Integer.valueOf(fecha.substring(fecha.indexOf("-") + 4));
            //System.out.println("------ " + t + " --- " + dia + " /-/ " + mes + " /-/ " + ano);  //t = 12*15-11-2018*9

            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            da.setDate(Integer.valueOf(dia));
            da.setMonth(mes - 1);
            da.setYear(ano - 1900);

            long sem = 0;
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date dat2 = formatoDelTexto.parse(minDay);

         /*       dat2.setDate(Integer.valueOf(formatoDelTexto.format(minDay).substring(0,formatoDelTexto.format(minDay).indexOf("/"))));
                da.setMonth(Integer.valueOf(formatoDelTexto.format(minDay).substring(formatoDelTexto.format(minDay).indexOf("/")+1,formatoDelTexto.format(minDay).lastIndexOf("/"))) - 1);
                da.setYear(Integer.valueOf(formatoDelTexto.format(minDay).substring(formatoDelTexto.format(minDay).lastIndexOf("/")+1, formatoDelTexto.format(minDay).length())) - 1900);
       */         //formateador.parse(minDay);

                //Fecha del evento - Fecha Menor dia de la primer semana
                sem = (da.getTime() - dat2.getTime()) / (1000 * 60 * 60 * 24); //Diferencia de dias entre ambas fechas
                float div = (float)sem/6;
                BigDecimal bigDecimal = new BigDecimal(div).setScale(2, RoundingMode.UP);

                if (bigDecimal.doubleValue() < 1) {
                    semana = 1;
                } else {
                    if (bigDecimal.doubleValue() <= 2) {
                        semana = 2;
                    } else {
                        if (bigDecimal.doubleValue() <= 3) {
                            semana = 3;
                        } else {
                            semana = 4;
                        }
                    }
                }
      //          System.out.println("SEMMM_CargarEventos " + sem + " -- " + semana + " -- " + bigDecimal.doubleValue());

      //          System.out.println("SEMANA: " + da.toLocaleString() + "  - Semana: " + semana + " -- " + cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da) + " -- " + formateador.format(dat2));
//cancha + "#"+
                eventosSemana.add(cant + "*" + "textView" + semana + "-" + turn + "/" + formateador.format(da));
                //5#1*textView1-02/12/04/2019
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

        Iterator I2 = eventosSemana.iterator();
        int resID = 0;
        while (I2.hasNext()) {
            String txt = (String) I2.next();  //VERSION2:  //5#1*textView1-02/12/04/2019
            System.out.println("TXT : " + txt); //  7*textView1-02/02/11/2018
            //  6*textView2-02/13/11/2018
         //   String CantCancha = txt.substring(0, txt.indexOf("#"));
            String cant1 = txt.substring(0, txt.indexOf("*"));
            String tur = txt.substring(txt.indexOf("-") + 1, txt.indexOf("/"));
            String semana1 = txt.substring(txt.indexOf("w") + 1, txt.indexOf("w") + 2);
            String fech = txt.substring(txt.indexOf("/") + 1); //Fecha real del evento

            long r = 0;
            try {
                Date date1 = new Date();
                Date date2;

                date1.setDate(Integer.valueOf(fech.substring(0, 2)));
                date1.setMonth(Integer.valueOf(fech.substring(3, 5)) - 1);
                date1.setYear(Integer.valueOf(fech.substring(6)) - 1900);
                date1.setHours(0);
                date1.setMinutes(0);
                date1.setSeconds(0);
                cal2 = cal;

                switch (semana1) {
                    case "1":
                        System.out.println("FECH 1: " + fech);
         //               System.out.println("MINIMO DIA 1: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(minDay);
                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;
          //              System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
          //              System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
          //              System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        primera.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        break;

                    case "2":
                        System.out.println("FECH 2: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 7);
          //              System.out.println("MINIMO DIA 2: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

           //             System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
           //             System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
           //             System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        segunda.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -7);
                        break;

                    case "3":
                        System.out.println("FECH 3: " + fech);

                        cal2.add(Calendar.DAY_OF_MONTH, 14);
           //             System.out.println("MINIMO DIA 3: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

           //            System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
           //             System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
           //             System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        tercera.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -14);
                        break;

                    case "4":
                        System.out.println("FECH 4: " + fech);
                        cal2.add(Calendar.DAY_OF_MONTH, 21);
           //             System.out.println("MINIMO DIA 4: " + formateador.format(cal2.getTime()));
                        date2 = formateador.parse(formateador.format(cal2.getTime()));

                        r = ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)) + 1;

           //             System.out.println("date 1 : " + date1.getTime() + " - " + date1.toLocaleString() + " -- " + fech);
           //             System.out.println("date 2 : " + date2.getTime() + " - " + date2.toLocaleString() + " --- " + minDay);
          //              System.out.println("VALOR R: " + cant1 + "*" + "textView" + semana1 + tur + r);
                        cuarta.add(cant1 + "*" + "textView" + semana1 + tur + r);
                        cal2.add(Calendar.DAY_OF_MONTH, -21);
                        break;
                    default:
                        break;
                }

         /*       String txF = txt.substring(txt.indexOf("*") + 1, txt.indexOf("w") + 2) + tur + r;
                System.out.println("PRII: " + txF + " - Cant: " + cant1);

                resID = getResources().getIdentifier(txF, "id", this.getPackageName());
                TextView ta = (TextView) this.findViewById(resID);
                ta.setText(cant1);
*/
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ARRAYS: " + primera.size() + " - " + segunda.size() + " - " + tercera.size() + " - " + cuarta.size());

        ejecutar2doJSON();

    }

    public void  continuarJSONPopUP(boolean fromPopU, String res) {
        switch (res){
            case "OK":
                Toast.makeText(this.getApplicationContext(),"Se Inscribio correctamente.",Toast.LENGTH_SHORT).show();
                break;
            case "NOK1":
                Toast.makeText(this.getApplicationContext(),"Error al inscribirse. No hay cupo disponible.",Toast.LENGTH_LONG).show();
                cargarFullStock();
                break;
            case "NOK2":
                Toast.makeText(this.getApplicationContext(),"Error al inscribirse. Turno NO Disponible.",Toast.LENGTH_LONG).show();
                break;
        }
        JSONEventos(fromPopU);
        this.ReloadALL(true);
    }

    public void getDiaSemana() {
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1: //Domingo
                cal.add(Calendar.DAY_OF_WEEK, 1);
                break;
            case 2: //Lunes
                cal.add(Calendar.DAY_OF_WEEK, 0);
                break;
            case 3: //Martes
                cal.add(Calendar.DAY_OF_WEEK, -1);
                break;
            case 4: //Miercoles
                cal.add(Calendar.DAY_OF_WEEK, -2);
                break;
            case 5: //Jueves
                cal.add(Calendar.DAY_OF_WEEK, -3);
                break;
            case 6: //Viernes
                cal.add(Calendar.DAY_OF_WEEK, -4);
                break;
            case 7: //Sabado
                cal.add(Calendar.DAY_OF_WEEK, -5);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.MiDisponibilidad) {
            Intent nvo = new Intent(this, Disponibilidad.class);
            nvo.putExtra("user", FB_user);
            nvo.putExtra("mail", FB_mail);
            this.startActivity(nvo);
            // Handle the camera action
        } else if (id == R.id.inicio) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.hide(ubicFrag);
            ft.remove(ubicFrag);

            mViewPager.setAdapter(mSectionsPagerAdapter);
            //ft.replace(R.id.container, pri).commit();
            ft.show(pri).commit();
            findViewById(R.id.Titulo).setVisibility(View.VISIBLE);
            findViewById(R.id.Subtitulo).setVisibility(View.VISIBLE);

        } else if (id == R.id.dondestamos) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (!ubicFrag.isAdded())
                ft.add(R.id.ContainerGral, ubicFrag).commit();

            mViewPager.setAdapter(null);

            findViewById(R.id.Titulo).setVisibility(View.INVISIBLE);
            findViewById(R.id.Subtitulo).setVisibility(View.INVISIBLE);

            System.out.println("FRAGMENT DONDEESTAMOS: " + mViewPager.getCurrentItem());

        } else if (id == R.id.cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();

            Intent nvo = new Intent(this, LoginActivity.class);
            this.startActivity(nvo);

      //  } else if (id == R.id.nav_share) {
            //ESTE MENU ESTA OCULTO EN EL xml

        } else if (id == R.id.nav_send) {
            PopUpMail popMail =  PopUpMail.newInstance(this, FB_user, FB_mail);
            popMail.show(getFragmentManager(), "Contactenos");


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    /**
     * A {@link //SectionsPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
      /*      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
     //       ft.replace(R.id.container, pri);
            ft.add(R.id.container, seg);
            ft.add(R.id.container, ter);
            ft.add(R.id.container, cuar);
            ft.commit();
       */
            switch (position) {
                case 0: fragment = pri; break;
                case 1: fragment = seg; break;
                case 2: fragment = ter; break;
                case 3: fragment = cuar; break;
              //  case 4: fragment = ubicFrag; break;
                //default: fragment = pri; break;
                /*
                case 0: ft.show(pri); break;
                case 1: ft.show(seg); break;
                case 2: ft.show(ter); break;
                case 3: ft.show(cuar); break;
                */
            }


          //  ft.add(R.id.FragConteiner, seg, "frag2");
            // ft.add(R.id.FragConteiner, ter, "frag3");
            // ft.add(R.id.FragConteiner, cuar, "frag4");

          //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
           // ft.show(pri);
          //  ft.commit();


            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
                default:
                    return null;
            }
        }

    }

}
