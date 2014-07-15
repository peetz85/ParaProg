package server;

import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;
import org.jcsp.net.cns.CNSService;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel implements CSProcess {


    public NetChannelOutput output;
    public NetChannelInput input;


    public ServerChannel(String target){



        try {
            System.setProperty("org.jcsp.tcpip.DefaultCNSServer", "localhost:51526");
            Node.getInstance().init();
        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        }

        if(target == "1") {
            output = CNS.createOne2Net("Server_RAUS");
            input = CNS.createNet2One("Server_REIN");
        } else {
            input = CNS.createNet2One("Server_RAUS");
            output = CNS.createOne2Net("Server_REIN");
        }
    }

    public void send(Message arg){
        output.write(arg);
    }

    public Message recive(){
        return (Message) input.read();
    }

    public void wakeup(){
        System.out.println("Aufwachen du Lutscher!!!");
    }

    @Override
    public void run() {
        Message tmp = new Message(false);
        tmp.i = 5;
        send(tmp);
        while(true) {
            Message incoming = (Message) input.read();
            if(incoming.WAKEUP){
                wakeup();
            }
            else if(incoming.i != null) {
                System.out.println(incoming.i * 2);
                incoming.i = incoming.i*2;
                send(incoming);
            }
        }
    }
}
