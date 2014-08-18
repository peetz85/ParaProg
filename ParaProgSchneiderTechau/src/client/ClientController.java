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
            System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+ "answerEcho - Message ist eine Antwort");
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if(deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName()))
                deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
            if(deliveryInformations !=null){
                System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"answerEcho - Für die Message gibt es einen Eintrag");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if(deliveryInformations.waitingForAnswer.isEmpty()){
                System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"answerEcho - Keine ausstehenden Antworten");
                int retInt=1;
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    if(deliveryInformations.answers.get(count) != null){
                        retInt += deliveryInformations.answers.get(count).getNodeCount();
                    }
                }
                if(serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())){
                    System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"answerEcho - Ich bin der Knoten der gefragt hat");
                    System.out.println("Soooo viele Knoten: " + retInt);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                } else {
                    System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"answerEcho - Ich bin nicht der Knoten der gefragt hat");
                    msg.setNodeCount(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                }
            }
        } else {
            System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"Antwort_6");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCount(1, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo,msg);
        }
    }

    public void forwardEcho(Message msg) {
        if (isLastNode(msg.getNodeSet())) {
            System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"Zurück damit. Bin der Letzte Node");
            answerEcho(msg);
        } else {
            System.out.println("Nachricht von "+ msg.getMessageFrom()+":"+"Nicht der letzte Node. Nachricht Weiterleiten!");
            if (!returnToSender.containsKey(msg.getREQUEST_CREATOR())) {
                System.out.println("Es gibt noch keinen Eintrag mit dieser Anfrage");
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
            } else {
            }
        }
    }

    public boolean isLastNode(HashSet<String> visited) {

        return visited.containsAll(serverCTR.generateNodeSet());
    }

    public GraphPaul generateGraph(){
        HashSet<String> arg = serverCTR.generateNodeSet();
        String[] array = arg.toArray(new String[0]);

        GraphPaul graph = new GraphPaul(array);

        for(int i=0; i<array.length;++i){
            for(int c=0; c<array.length;++c){
                graph.verbindeKnoten(array[i],array[c]);
            }
        }
        return graph;
    }
}
