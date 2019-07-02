package com.example.jose_.juego;

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
        DBserver = "    "; //"http://inncode.comoj.com/";
    }

}
