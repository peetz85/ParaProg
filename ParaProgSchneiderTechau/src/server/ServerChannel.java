package server;

import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel extends Thread /*implements CSProcess*/ {

    private boolean running = true;
    private NetChannelOutput output;
    private NetAltingChannelInput input;

    private ServerController parent;


    public ServerChannel(ServerController parent) {
        this.parent = parent;
    }

    public void setRunning(boolean arg){
        running = arg;
    }

    public boolean isRunning(){
        return running;
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

    public void handshake(){
        Message msg = new Message(parent.getServerName());
        msg.setLabel(parent.getServerName(), true);
        send(msg);
    }

    public void send(Message arg) {
        output.write(arg);
    }

    private Message recive(){
        if(input.pending()){
            return (Message) input.read();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        Message msg;
        while (running) {
            msg = recive();

            try {
                Thread.sleep(1000);
                System.out.println("Lebe noch");
            } catch (Exception e) {}

            if(msg != null){
                if(msg.iscInteger()){
                    System.out.println(msg.getI());
                    msg.setI(msg.getI()+5);
                    send(msg);
                }
                if(msg.isTerminateSignal()){
                    parent.removeConnection(msg.getTerminateServerName());
                }

                if(msg.isHandshake()){
                    if(msg.isHandshakeRequest()){
                        msg = new Message(parent.getServerName());
                        msg.setLabel(parent.getServerName(),false);
                        send(msg);
                    }else {
                        parent.saveConnection(msg);
                    }
                }

                if(msg.isEchoRequest()){
                    System.out.println("Es gibt ein ECHO Request!");
                    if(msg.isEchoAnswer()){
                        parent.clientCTR.answerEcho(msg);
                    } else {
                        parent.clientCTR.forwardEcho(msg);
                    }
                }

            }
        }
    }
}


