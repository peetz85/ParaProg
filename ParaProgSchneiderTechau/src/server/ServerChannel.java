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


    public ServerChannel(String target, ServerController parent) {
        this.parent = parent;
        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", "localhost:51526");
            Node.getInstance().init();
        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        output = CNS.createOne2Net(parent.serverName + "_Output");
        input = CNS.createNet2One(parent.serverName + "_Input");
    }

    public void connect(String arg) {
        input = CNS.createNet2One(arg + "_Output");
        output = CNS.createOne2Net(arg + "_Input");
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

                if (incoming != 0) {
                    System.out.println(incoming * 2);
                    incoming = incoming * 2;
                    send(incoming);
                }
            }
        }
    }
}

