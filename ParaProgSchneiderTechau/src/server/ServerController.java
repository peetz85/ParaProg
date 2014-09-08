package server;


import client.ClientController;
import gui.console.OpenConnection;
import org.jcsp.net.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController{

    private String serverName;
    private String serverIP;
    public ClientController clientCTR;

    private LinkedBlockingQueue<Message> incommingMessages;

    private int nextFreePort;
    public HashMap<String,ServerChannel> incomingConnection;

    public HashMap<ConnectionLabel, ServerChannel> connections;

    public HashMap<String,OpenConnection> openConnections;


    /**
     * Konstruktor
     * @param name
     * @param clientCTR
     */
    public ServerController(String name, ClientController clientCTR) {
        setServerName(name);
        setServerIP();
        this.clientCTR = clientCTR;
        incommingMessages = new LinkedBlockingQueue<Message>();

        connections = new HashMap<ConnectionLabel, ServerChannel>();
        nextFreePort = 1;
        openConnections = new HashMap<String, OpenConnection>();
        incomingConnection = new HashMap<String, ServerChannel>();
    }

    public synchronized void putMessage(Message arg ){
        try {
            incommingMessages.put(arg);
        } catch (InterruptedException e) {}
    }

    public synchronized Message getMessage(){
        return incommingMessages.poll();
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

//    private void removeServerChannel(String server){
//        if(!connections.isEmpty()) {
//            for (ConnectionLabel key : connections.keySet()) {
//                if(key.getServerName().equals(server)){
//                    connections.remove(key);
//                }
//            }
//        }
//    }


    public void sendAll(HashSet<String> nodeSet, boolean except, Message msg) {
        if (!connections.isEmpty()) {
            if (except) {
                //Nachricht an ALLE senden die NICHT in der Liste sind!!!!
                for (Map.Entry<ConnectionLabel, ServerChannel> entry : connections.entrySet()) {
                    if (!nodeSet.contains(entry.getKey().getServerName())) {
                        entry.getValue().send(msg);
                    }
                }
            } else {
                //Nachricht an ALLE senden die in der Liste vorhanden sind
                for (Map.Entry<ConnectionLabel, ServerChannel> entry : connections.entrySet()) {
                    if (nodeSet.contains(entry.getKey().getServerName())){
                        entry.getValue().send(msg);
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
        return nextFreePort++;
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
        ConnectionLabel connection = new ConnectionLabel(msg.getMessageFrom(),String.valueOf(nextFreePort-1));
        connections.put(connection, incomingConnection.get(msg.getPortNumber()));
        incomingConnection.remove(msg.getPortNumber());
        openConnections.get(msg.getPortNumber()).dispose();
        openConnections.remove(msg.getPortNumber());
    }

    public void connectToNode(String target, String port) {
        ServerChannel channel;
        if(target.contains(serverName)){
            channel = new ServerChannel(target+":"+port, this);
            incomingConnection.put(target+":"+port,channel);
//            ++nextFreePort;
        } else {
            channel = new ServerChannel(target+":"+port, this);
            ConnectionLabel connection = new ConnectionLabel(target,port);
            connections.put(connection, channel);
        }
        channel.start();
    }



    public void printAllNodesFancy(){
        System.out.println("- - - - - - - Connections - - - - - - -");
        printAllNodes();
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
    }
    public void printAllNodes(){
        for (ConnectionLabel key : connections.keySet()) {
            System.out.println(key);
        }
    }

    public void removeConnection(Message msg){
        System.out.println("#S: Recieved Terminate Signal From " + msg.getMessageFrom());
        ServerChannel toRemove = null;
        ConnectionLabel toRemoveLabel = null;
        for (Map.Entry<ConnectionLabel, ServerChannel> entry : connections.entrySet()) {
            ConnectionLabel key = entry.getKey();
            if(key.getServerName().equals(msg.getREQUEST_CREATOR())){
                toRemoveLabel = key;
            }
        }
        toRemove = connections.remove(toRemoveLabel);
        toRemove.interrupt();
        if(!connections.isEmpty())
            printAllNodesFancy();
    }

    public void terminateConnections(){
        if(!connections.isEmpty()) {
            Message terminateSignal = new Message(serverName);
            terminateSignal.setTerminateSignal(serverName);
            sendAll(new HashSet<String>(),true,terminateSignal);
        }
    }
}


