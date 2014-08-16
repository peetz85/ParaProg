package server;


import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.ProcessManager;
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
public class ServerController{
    private String serverName;
    private String serverIP;

    private int nextFreePort;
    private ServerChannel incomingConnection;

    public HashMap<ConnectionLabel, ServerChannel> connections;
    //private Parallel channelListener;

    public ServerController(String name) {
        setServerName(name);
        setServerIP();

        connections = new HashMap<ConnectionLabel, ServerChannel>();
        //channelListener = new Parallel();
        nextFreePort = 0;

    }

    public int getNextFreePort(){
        return nextFreePort;
    }

    public void setServerName(String arg){
        serverName = arg;
    }

    public String getServerName(){
        return serverName;
    }

    public void setServerIP(){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverIP = (String) ip.getHostAddress();
    }

    public String getServerIP(){
        return serverIP;
    }

    public void setCNSServer(String arg) {
        System.out.println("Setting CNS Server!");
        arg += ":51526";

        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", arg);
            Node.getInstance().init();
        } catch (NodeInitFailedException e) {}
    }

    public void saveConnection(Message msg){
        ConnectionLabel connection = new ConnectionLabel(msg.getLabel(),String.valueOf(nextFreePort-1));
        connections.put(connection,incomingConnection);
        incomingConnection = null;
    }

    public void connectToNode(String target, String port, boolean arg) {
        ServerChannel channel = new ServerChannel(this);
        if(!arg){
            ConnectionLabel connection = new ConnectionLabel(target,port);
            connections.put(connection, channel);
        } else {
            incomingConnection = channel;
            ++nextFreePort;
        }
        channel.connect(target+port, arg);
        if(arg){
            channel.handshake();
        }
        channel.start();

    }

    public void removeConnection(String server){
        ServerChannel toDelete = connections.get(server);
        toDelete.setRunning(false);
        connections.remove(server);
    }
}


