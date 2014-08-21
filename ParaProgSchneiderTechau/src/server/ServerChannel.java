package server;

import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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

    public void setRunning(boolean arg) {
        running = arg;
    }


    public boolean isRunning() {
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

    public void handshake() {
        Message msg = new Message(parent.getServerName());
        msg.setLabel(parent.getServerName(), true);
        send(msg);
    }

    public void send(Message arg) {
        output.write(arg);
    }

    @Override
    public void run() {
        Message msg = null;
        while (running) {
            try {
                Thread.sleep(250);
                if (input.pending())
                    msg = (Message) input.read();
            } catch (Exception e) {}
            if (msg != null) {
                if (msg.isTerminateSignal() || msg.isHandshake_1st()) {
                    if (msg.isTerminateSignal()) {
                        parent.removeConnection(msg.getTerminateServerName());
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


