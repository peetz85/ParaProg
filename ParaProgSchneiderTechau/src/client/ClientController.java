package client;

import server.ConnectionLabel;
import server.Message;
import server.ServerController;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ClientController {

    private String clientName;
    private ServerController serverCTR;

    private HashMap<String, ReturnType> returnToSender;

    public ClientController(String name) {
        clientName = name;
        returnToSender = new HashMap<String, ReturnType>();
    }

    public void setServerCTR(ServerController serverCTR) {
        this.serverCTR = serverCTR;
    }

    public void setClientName(String arg) {
        clientName = arg;
    }


    public void run() {

    }

    public void answerEcho(Message msg) {
        if(msg.isEchoAnswer()){

        } else {
            Message returnMessage = new Message();
            returnMessage.setNodeCount(1,serverCTR.getServerName());

        }


    }

    public void forwardEcho(Message msg) {
        if (!isLastNode(msg.getNodeSet())) {
            if (!returnToSender.containsKey(msg.getMessageFrom())) {

                //ReturnType erstellen mit Sender der alten Nachricht und
                //Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType();
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setEchoRequest(waitingFor);
                returnToSender.put(msg.getMessageFrom(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setNodeSet(dontVisist, serverCTR.getServerName());

                serverCTR.sendAll(waitingFor, false, msg);
            }
        } else
            answerEcho(msg);
    }

    public boolean isLastNode(HashSet<String> visited) {
        return serverCTR.generateNodeSet().containsAll(visited);
    }

}
