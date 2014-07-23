package server;


import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;
import org.jcsp.net.cns.CNSService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController extends Thread implements CSProcess{
    final public String serverName;
    final public String serverIP;
    public HashMap<String, ServerChannel> connections;
    public Parallel channelListener;

    public ServerController(String name) {
        serverName = name + "." + this.toString();
        serverIP = getIPAdress();
        connections = new HashMap<String, ServerChannel>();
        channelListener = new Parallel();
    }

    public void setCNSServer(String arg){

        arg += ":51526";

        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", arg);
            Node.getInstance().init();
        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        }
        //initNewNode();
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

    public void run(){
        while(true) {
            channelListener.run();
            try {
                Thread.sleep(250);
                System.out.println("Runing!");
            } catch (Exception e) {
            }
        }
    }

    public void connectToNode(String target){
        ServerChannel tmp = new ServerChannel(this);
        connections.put(target, tmp);
        channelListener.addProcess(tmp);
        tmp.connect(target, false);
        tmp.send(100);
    }

    public void initNewNode(){

            ServerChannel arg = new ServerChannel(this);
            connections.put("nextFreeChannel",arg);
            channelListener.addProcess(arg);
            arg.connect("localhost",true);

    }

    public void incomingConnectionFromNode(String target){
        System.out.println("INCOMING!!!!");

        ServerChannel tmp = connections.get("nextFreeChannel");
/*
            connections.remove("nextFreeChannel");
            connections.put(target,tmp);
            channelListener.removeProcess(tmp);
            channelListener.addProcess(tmp);
*/
        tmp.send(50);
        System.out.println("Nachricht gesendet");
        //initNewNode();
    }
}

