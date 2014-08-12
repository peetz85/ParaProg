package server;

import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel implements CSProcess {

    private boolean isInitialised;
    private NetChannelOutput output;
    private NetChannelInput input;

    private ServerController parent;
    private String connectionTo;


    public ServerChannel(ServerController parent) {
        this.parent = parent;
        isInitialised = false;
    }

    public ServerChannel(ServerController parent, boolean isInitialised){
        this.parent = parent;
        this.isInitialised = isInitialised;
    }

    public void connect(String arg, boolean localhost) {
        if (localhost) {
            output = CNS.createOne2Net(arg + "_Output");
            input = CNS.createNet2One(arg + "_Input");
        } else {
            input = CNS.createNet2One(arg + "_Output");
            output = CNS.createOne2Net(arg + "_Input");
        }
    }

    public void setConnectionTo(String arg){
        connectionTo = arg;
    }

    public String getConnectionTo(){
        return connectionTo;
    }

    public void send(Message arg) {
        output.write(arg);
    }

    public Message recive() {
        return (Message) input.read();
    }

    public void wakeup() {
        System.out.println("Aufwachen du Lutscher!!!");
    }

    @Override
    public void run() {
        while (true) {


            if (input != null) {
                try {
                    Thread.sleep(2000);
                    System.out.println("Lebe noch");
                } catch (Exception e){}
            }
        }
    }
}


