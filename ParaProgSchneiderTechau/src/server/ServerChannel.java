package server;

import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;
import org.jcsp.net.cns.CNSService;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel implements CSProcess {


    private NetChannelOutput output;
    private NetChannelInput input;

    private ServerController parent;


    public ServerChannel(ServerController parent) {
        this.parent = parent;
    }

    public void connect(String arg, boolean localhost) {
        if(localhost){
            output = CNS.createOne2Net(arg + "_Output");
            input = CNS.createNet2One(arg + "_Input");
        } else {
            input = CNS.createNet2One(arg + "_Output");
            output = CNS.createOne2Net(arg + "_Input");
        }
    }

    public void send(int arg) {
        output.write(arg);
    }

    public int recive() {
        return (Integer) input.read();
    }

    public void wakeup() {
        System.out.println("Aufwachen du Lutscher!!!");
    }

    @Override
    public void run() {
        while (true) {
            if (input != null) {
                int incoming = (Integer) input.read();
                if(incoming == 100){
                    parent.incomingConnectionFromNode("localhost");
                    send(50);
                }

                if(incoming == 50){
                    try {
                        Thread.sleep(250);
                    } catch (Exception e){};
                    System.out.println(incoming);
                    output.write(50);
                }


                /*
                int incoming = (Integer) input.read();
                   try {
                       Thread.sleep(150);
                   } catch (Exception e){}

                if (incoming != 0) {
                    System.out.println(incoming * 2);
                    incoming = (incoming * 2);
                    send(incoming);
                } else
                    send(1);
                 */
            }
        }
    }
}

