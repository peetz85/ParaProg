package client;

import server.ConnectionLabel;
import server.Message;
import server.ServerController;

import java.util.ArrayList;
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

    public void initEcho(){
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeSet(dontVisit,clientName);
        HashSet<String> empty = new HashSet<String>();
        serverCTR.sendAll(empty, true, msg);
    }

    public void answerEcho(Message msg) {
        if(msg.isEchoAnswer()){
            ReturnType waitingFor = returnToSender.get(msg.getMESSAGE_CREATOR());
            if(waitingFor !=null){
                waitingFor.getAnswers().add(msg);
                waitingFor.getWaitingForAnswer().remove(msg.getMessageFrom());
            }
            if(waitingFor.getWaitingForAnswer().isEmpty()){
                int retInt =0;
                ArrayList<Message> messages = waitingFor.getAnswers();
                for(int count=0; count<=messages.size();++count){
                    retInt += messages.get(count).getNodeCount();
                }
                if(serverCTR.getServerName() == msg.getMESSAGE_CREATOR()){
                    System.out.println("Soooo viele Knoten: " + retInt);
                } else {
                    msg.setNodeCount(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(waitingFor.getSendBackTo(), msg);
                }
            }

        } else {
            Message returnMessage = msg;
            returnMessage.setNodeCount(1,serverCTR.getServerName());
            serverCTR.sendOnly(msg.getMessageFrom(),returnMessage);
        }


    }

    public void forwardEcho(Message msg) {

        if (isLastNode(msg.getNodeSet())) {
            System.out.println("LastNode");
            if (!returnToSender.containsKey(msg.getMessageFrom())) {

                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType();
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setEchoRequest(waitingFor,msg.getMessageFrom());
                returnToSender.put(msg.getMESSAGE_CREATOR(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setNodeSet(dontVisist, serverCTR.getServerName());

                serverCTR.sendAll(waitingFor, false, msg);
            }
        } else {
            System.out.println("Zurück damit");
            answerEcho(msg);
        }

    }

    public boolean isLastNode(HashSet<String> visited) {
        return serverCTR.generateNodeSet().containsAll(visited);
    }

}
