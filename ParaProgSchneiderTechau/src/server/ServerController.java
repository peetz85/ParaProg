package server;


import client.ClientController;
import org.jcsp.net.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController{
    private String serverName;
    private String serverIP;
    public ClientController clientCTR;

    private int nextFreePort;
    private ServerChannel incomingConnection;

    public HashMap<ConnectionLabel, ServerChannel> connections;

    public ServerController(String name, ClientController clientCTR) {
        setServerName(name);
        setServerIP();
        this.clientCTR = clientCTR;

        connections = new HashMap<ConnectionLabel, ServerChannel>();
        nextFreePort = 0;
    }

    public HashSet<String> generateNodeSet(){
        HashSet<String> nodeSet = new HashSet<String>();
        if(!connections.isEmpty()) {
            for (ConnectionLabel key : connections.keySet()) {
                nodeSet.add(key.getServerName());
                }
            }
        nodeSet.add(serverName);
        return nodeSet;
    }

    public ServerChannel getServerChannel(String server){
        ServerChannel returnValue = null;
        if(!connections.isEmpty()) {
            for (ConnectionLabel key : connections.keySet()) {
                    if(key.getServerName().equals(server)){
                        returnValue = connections.get(key);
                    }
            }
        }
        return returnValue;
    }

    private void removeServerChannel(String server){
        if(!connections.isEmpty()) {
            for (ConnectionLabel key : connections.keySet()) {
                if(key.getServerName().equals(server)){
                    connections.remove(key);
                }
            }
        }
    }


    public void sendAll(HashSet<String> nodeSet, boolean except, Message msg) {
        if (!connections.isEmpty()) {
            System.out.println("Sende Nachricht an:");
            if (except) {
                //Nachricht an ALLE senden die NICHT in der Liste sind!!!!
                for (Map.Entry<ConnectionLabel, ServerChannel> entry : connections.entrySet()) {
                    if (!nodeSet.contains(entry.getKey().getServerName())) {
                        entry.getValue().send(msg);
                        System.out.println(entry.getKey().getServerName());
                    }
                }
            } else {
                //Nachricht an ALLE senden die in der Liste vorhanden sind
                for (Map.Entry<ConnectionLabel, ServerChannel> entry : connections.entrySet()) {
                    if (nodeSet.contains(entry.getKey().getServerName())){
                        entry.getValue().send(msg);
                        System.out.println(entry.getKey().getServerName());
                    }
                }
            }
        }
    }

    public void sendOnly(String arg, Message msg){
        ServerChannel connection = getServerChannel(arg);
        if(connection != null) {
            connection.send(msg);
        }
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
        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", arg+":51526");
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
        channel.start();
        if(arg){
            channel.handshake();
        }

    }

    public void removeConnection(String server){
        ServerChannel toDelete = getServerChannel(server);
        toDelete.setRunning(false);
        connections.remove(server);
    }

    public void printAllNodesFancy(){
        System.out.println("- - - Node Connections - - -");
        printAllNodes();
        System.out.println("- - - - - - - - - - - - - - - - - - - - -");
    }
    public void printAllNodes(){
        for (ConnectionLabel key : connections.keySet()) {
            System.out.println(key);
        }
    }
    public void printAllNodes(HashSet<String> arg){
        Iterator iter = arg.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    public void terminateConnections(){
        if(!connections.isEmpty()) {
            Message terminateSignal = new Message(serverName);
            terminateSignal.setTerminateSignal(serverName);

            for (ServerChannel value : connections.values()) {
                value.send(terminateSignal);
            }
        }
    }

}


