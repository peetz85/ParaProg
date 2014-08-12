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
public class ServerController extends Thread{
    private String serverName;
    private String serverIP;

    public HashMap<String, ServerChannel> connections;
    private Parallel channelListener;

    public ServerController(String name) {
        setServerName(name);
        setServerIP();

        connections = new HashMap<String, ServerChannel>();
        channelListener = new Parallel();

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
        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            channelListener.run();


        }
    }

    public void connectToNode(String target, boolean arg) {
        ServerChannel tmp = new ServerChannel(this);
        connections.put(target, tmp);
        channelListener.addProcess(tmp);
        tmp.connect(target, arg);
    }

    public void openConnection(){

    }

    public void closeConnection(){

    }
}


