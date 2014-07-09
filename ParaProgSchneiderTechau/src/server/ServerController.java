package server;


import org.jcsp.lang.Parallel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController extends Thread{

    final public String serverName;
    public Vector<ServerChannel> connections;

    // FUSCH UND So
    public Parallel par = new Parallel();
    public ServerChannel t1 = new ServerChannel(true);

    public ServerController(String name) {
        serverName = name+"."+this.toString();



        // ---- TEST MÜLL

        ServerChannel t2 = new ServerChannel(false);

        t2.setInputChannel(t1.o2oChannelOutPut);
        t1.setInputChannel(t2.o2oChannelOutPut);


        par.addProcess(t1);
        par.addProcess(t2);
        // ___ TEST MÜLL

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
        par.run();
    }



}
