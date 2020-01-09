package com.CirculoFutbol;

/**
 * Created by jose_ on 9/9/2018.
 */
public class ServerID {
    private static ServerID serverID;
    static String DBserver;

    public static synchronized ServerID getInstance(){
        if (serverID == null){
            serverID = new ServerID();
        }
        return serverID;
    }

    public ServerID(){
      //  DBserver = "http://circulohost.esy.es/"; //micancha.000webhostapp.com/"; //hostingerapp.com/"; //"http://inncode.comoj.com/";
                            //";
        DBserver = "http://micancha.hostingerapp.com/";
    }

}
