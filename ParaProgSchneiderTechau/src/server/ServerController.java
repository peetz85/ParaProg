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
    public HashMap<String, ServerChannel> connections;
    private Parallel channelListener;

    public ServerController(String name) {
        serverName = name + "." + this.toString();
        connections = new HashMap<String, ServerChannel>();
        channelListener = new Parallel();

        //setCNSServer("localhost");

    }

    public void setCNSServer(String arg){

        //arg += ":51526";

        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", arg);
            Node.getInstance().init();
        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        }
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
        channelListener.run();
    }

    public void startConnection(String target){

        ServerChannel tmp = new ServerChannel(target,this);
        connections.put(target, tmp);
        channelListener.addProcess(tmp);
        tmp.connect(target, true);
    }
    public void connectConnection(String target){

        ServerChannel tmp = new ServerChannel(target,this);
        connections.put(target, tmp);
        channelListener.addProcess(tmp);
        tmp.connect(target, false);
    }
}

