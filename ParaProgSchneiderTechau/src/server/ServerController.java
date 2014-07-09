package server;


import org.jcsp.lang.Parallel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController extends Thread{

    final public String serverName;
    private HashMap<String, ServerChannel> connections;
    private Parallel channelListener;


    public ServerController(String name) {
        serverName = name + "." + this.toString();

        connections = new HashMap<String, ServerChannel>();
        channelListener = new Parallel();
    }



    public String getIPAdress(){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return (String) ip.getHostAddress();
    }

    public void establishConnection(ServerChannel arg, String connection, boolean request){

        //request == True ... Neue Anfrage / Channel erstellen
        if(request){
            ServerChannel tmp = new ServerChannel();
            tmp.setInputChannel(arg.o2oChannelOutPut);
            connections.put(connection, tmp);

            //TODO tmp an Connection senden für Verknüpfung (establishConnection(tmp, "eigene IP", false)

            channelListener.addProcess(tmp);
        } else {
            ServerChannel tmp = connections.get(connection);
            tmp.setInputChannel(arg.o2oChannelOutPut);
        }
    }

    public void startConnection(String connection){
        ServerChannel tmp = new ServerChannel();
        connections.put(connection, tmp);

        //TODO Verbidnung mit anderen Node herstellen (establishConnection(tmp, "eigene IP", true)

        channelListener.addProcess(tmp);
    }

    public void run(){
        channelListener.run();
    }



}
