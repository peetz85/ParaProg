package server;

import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

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
        if (localhost) {
            output = CNS.createOne2Net(arg + "_Output");
            input = CNS.createNet2One(arg + "_Input");
        } else {
            input = CNS.createNet2One(arg + "_Output");
            output = CNS.createOne2Net(arg + "_Input");
        }
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
                Message arg = (Message) input.read();

                System.out.println(arg.getI());
                arg.setI(arg.getI() + 5);
                send(arg);
            }
        }
    }
}


