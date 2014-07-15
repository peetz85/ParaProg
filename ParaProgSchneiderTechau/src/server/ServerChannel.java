package server;

import org.jcsp.lang.*;
import org.jcsp.net.NetChannelInput;
import org.jcsp.net.NetChannelOutput;
import org.jcsp.net.cns.CNS;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel implements CSProcess {

/*
    NetChannelInput in = CNS.createNet2One("in");
    //resolve the channel
    NetChannelOutput out = CNS.createOne2Net("in");
  */

    private NetChannelOutput output;
    private NetChannelInput input;


    public ServerChannel(){
        output = CNS.createOne2Net("output");
        input = CNS.createNet2One("input");
    }

    public void setInput(NetChannelInput arg){
        if(input == null & arg != null) {
            input = arg;
        }
    }
    public NetChannelOutput getOutput(){
        return output;
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
        while(true) {
            Message incoming = (Message) input.read();
            if(incoming.WAKEUP){
                wakeup();
            }
            else if(incoming.i != null) {
                System.out.println(incoming.i * 2);
            }
        }
    }
}
