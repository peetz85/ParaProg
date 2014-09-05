package server;

import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Pascal on 09.07.2014.
 */
public class ServerChannel extends Thread /*implements CSProcess*/ {

    private NetChannelOutput output;
    private NetAltingChannelInput input;
    private ServerController parent;

    private String connectTo;

    public ServerChannel(String connectTo, ServerController parent) {
        this.parent = parent;
        this.connectTo = connectTo;
    }

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    private void connect(String arg, boolean localhost) {
        if (localhost) {
            output = CNS.createOne2Net(arg + "_Output");
            input = CNS.createNet2One(arg + "_Input");
        } else {
            input = CNS.createNet2One(arg + "_Output");
            output = CNS.createOne2Net(arg + "_Input");
        }
    }

    private void handshake() {
        Message msg = new Message(parent.getServerName());
        msg.setLabel(parent.getServerName(), true);
        send(msg);
    }

    public void send(Message arg) {
        output.write(arg);
    }

    @Override
    public void run() {
        connect(connectTo,connectTo.contains(parent.getServerName()));
        if(connectTo.contains(parent.getServerName())){
            handshake();
        }

        Message msg = null;
        while (!isInterrupted()) {
            try {
                Thread.sleep(250);
                if (input.pending())
                    msg = (Message) input.read();
            } catch (Exception e) {}
            if (msg != null) {
                if (msg.isTerminateSignal() || msg.isHandshake_1st()) {
                    if (msg.isTerminateSignal()) {
                        parent.removeConnection(msg);
                    } else if (msg.isHandshake_1st()) {
                        if (msg.isHandshake_2nd()) {
                            msg = new Message(parent.getServerName());
                            msg.setLabel(parent.getServerName(), false);
                            send(msg);
                        } else {
                            parent.saveConnection(msg);
                        }
                    }
                } else {
                    parent.putMessage(msg);
                }
            }
            msg = null;
        }
    }
}


