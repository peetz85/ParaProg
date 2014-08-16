package client;

import server.ConnectionLabel;
import server.Message;
import server.ServerController;

import java.util.HashSet;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ClientController {

    private String clientName;
    private ServerController serverCTR;

    public ClientController(String name){
        clientName = name;

    }

    public void setServerCTR(ServerController serverCTR){
        this.serverCTR = serverCTR;
    }

    public void setClientName(String arg){
        clientName = arg;
    }


    public void run(){

    }

    public void answerEcho(Message msg) {
    }

    public void forwardEcho(Message msg) {
        if(!isLastNode(msg.getNodeSet())) {
            HashSet<String> dontVisit = msg.getNodeSet();
            msg.getNodeSet().addAll(serverCTR.generateNodeSet());
            serverCTR.sendAll(dontVisit, true, msg);
        } else
            answerEcho(msg);
    }

    public boolean isLastNode(HashSet<String> visited){
        return serverCTR.generateNodeSet().containsAll(visited);
    }

}
