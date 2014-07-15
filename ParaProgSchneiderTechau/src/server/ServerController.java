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
    public HashMap<ServerController, ServerChannel> connections;
    private Parallel channelListener;

    private NodeKey key = null;
    private NodeID localNodeID = null;

    public ServerController(String name) {
        System.out.println("Server Test");
        serverName = name + "." + this.toString();

        connections = new HashMap<ServerController, ServerChannel>();
        channelListener = new Parallel();

        // NetChannelServer initializierung
        try {
            //Initialize a Node that does not have a CNS client
            key = Node.getInstance().init(new XMLNodeFactory("src/server/nocns.xml"));
            localNodeID = Node.getInstance().getNodeID();
            //Initialize the CNS Server Process
            CNS.install(key);
            NodeAddressID cnsAddress = localNodeID.getAddresses()[0];

        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    //LOCAL ServerControllerTest
    public void establishConnection(ServerChannel arg, ServerController target, boolean request){
        //request == True ... Neue Anfrage / Channel erstellen

        /*
        if(request){
            ServerChannel tmp = new ServerChannel();
            tmp.setInput(arg.getOutput());
            connections.put(target, tmp);
            channelListener.addProcess(tmp);
            target.establishConnection(tmp, this, false);
        } else {
            ServerChannel tmp = connections.get(target);
            tmp.setInput(arg.o2oChannelOutPut);
        }*/
    }
    public void startConnection(ServerController target){
        ServerChannel tmp = new ServerChannel();
        connections.put(target, tmp);
        channelListener.addProcess(tmp);
        target.establishConnection(tmp,this,true);
    }
}

