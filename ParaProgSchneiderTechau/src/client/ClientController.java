package client;

import semesteraufgabe.Starter;
import server.Message;
import server.ServerController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

        ReturnType retType = new ReturnType();
        retType.setEchoRequest(dontVisit, clientName);
        returnToSender.put(clientName,retType);

        serverCTR.sendAll(empty, true, msg);
    }

    public void answerEcho(Message msg) {
        if(msg.isEchoRequest_2nd()){
            System.out.println("antwort_1");
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if(deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName()))
                deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
            if(deliveryInformations !=null){
                System.out.println("antwort_2");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if(deliveryInformations.waitingForAnswer.isEmpty()){
                System.out.println("antwort_3");
                int retInt=1;
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    if(deliveryInformations.answers.get(count) != null){
                        retInt += deliveryInformations.answers.get(count).getNodeCount();
                    }
                }
                if(serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())){
                    System.out.println("antwort_4");
                    System.out.println("Soooo viele Knoten: " + retInt);
                } else {
                    System.out.println("antwort_5");
                    msg.setNodeCount(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                }
            }
        } else {
            System.out.println("Antwort_6");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCount(1,serverCTR.getServerName());
            System.out.println(sendBackTo);
            serverCTR.sendOnly(sendBackTo,msg);
        }
    }

    public void forwardEcho(Message msg) {
        if (isLastNode(msg.getNodeSet())) {
            System.out.println("Zurück damit");
            answerEcho(msg);
        } else {
            System.out.println("LastNode");
            if (returnToSender.containsKey(msg.getREQUEST_CREATOR())) {
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType();
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setEchoRequest(waitingFor, msg.getMessageFrom());
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setNodeSet(dontVisist, serverCTR.getServerName());

                serverCTR.sendAll(waitingFor, false, msg);
            }

        }
    }

    public boolean isLastNode(HashSet<String> visited) {
        return serverCTR.generateNodeSet().containsAll(visited);
    }

}
