package client;

import server.Message;
import server.ServerController;

import java.util.*;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ClientController extends Thread{

    private boolean debugMode = false;
    private String clientName;
    private ServerController serverCTR;

    private Map<String, ReturnType> returnToSender;

    public ClientController(String name) {
        clientName = name;

        HashMap hashMap = new HashMap<String, ReturnType>();

        returnToSender = Collections.synchronizedMap(hashMap);
    }

    public void setServerCTR(ServerController serverCTR) {
        this.serverCTR = serverCTR;
    }

    public void setClientName(String arg) {
        clientName = arg;
    }


    public void run() {
        Message msg = null;
        while(true){
            try {
                Thread.sleep(5);
            } catch (Exception e){}

            if(msg == null){
                msg = serverCTR.getMessage();
            }
            if(msg != null){
                    if(msg.iscInteger()){
                        System.out.println(msg.getI());
                        String sendTo = msg.getMessageFrom();
                        msg.setI(msg.getI()+5,clientName);
                        serverCTR.sendOnly(sendTo, msg);
                    }
                    if(msg.isNodeCount_1st()){
                        if(msg.isNodeCount_2nd()){
                            answerNodeCount(msg);
                        } else {
                            forwardNodeCount(msg);
                        }
                    }
                    if(msg.isNodeGraph_1st()){
                        if(msg.isNodeGraph_2nd()){
                            answerNodeGraph(msg);
                        } else {
                            forwardNodeGraph(msg);
                        }
                    }
                msg = null;
                }
        }
    }

    public synchronized void initNodeGraph() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeGraphRequest(clientName, dontVisit);

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP());
        retType.setEchoRequest(dontVisit, clientName);
        returnToSender.put(clientName, retType);

        serverCTR.sendAll(new HashSet<String>(), true, msg);
    }
    public synchronized void answerNodeGraph(Message msg) {
        if (msg.isNodeGraph_2nd()) {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Message ist eine Antwort");
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if (deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName()))
                deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
            if (deliveryInformations != null) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Für die Message gibt es einen Eintrag");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if (deliveryInformations.waitingForAnswer.isEmpty()) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Keine ausstehenden Antworten");
                GraphPaul returnGraph = new GraphPaul();
                returnGraph.addNodeSet(serverCTR.generateNodeSet());
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    returnGraph.addGraph(deliveryInformations.answers.get(count).getSpannBaum());
                    }
                if (serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())) {
                    if(debugMode)
                        System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin der Knoten der gefragt hat");
                    System.out.println(returnGraph);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                } else {
                    if(debugMode)
                        System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin nicht der Knoten der gefragt hat");
                    msg.setNodeGrapAnswer(serverCTR.getServerName(),returnGraph);
                    System.out.println(returnGraph);
                    if(debugMode)
                        System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Sende Nachricht an " + msg.getREQUEST_CREATOR());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                }
            }
        } else {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Antwort_6 Ich bin der Letzte Knoten  in der Kette");
            String sendBackTo = msg.getMessageFrom();
            GraphPaul returnGraph = new GraphPaul();
            returnGraph.addNodeSet(serverCTR.generateNodeSet());
            msg.setNodeGrapAnswer(serverCTR.getServerName(),returnGraph);
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Sende Nachricht an " + sendBackTo);
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public synchronized void forwardNodeGraph(Message msg) {
        if (!returnToSender.containsKey(msg.getREQUEST_CREATOR()) ||
                returnToSender.get(msg.getREQUEST_CREATOR()).REQUEST_TIMESTAMP != msg.getREQUEST_TIMESTAMP()) {
            if (isLastNode(msg.getNodeSet())) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Ich bin der Letzte Knoten -> Generiere meinen Graph");
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP());
                returnToSender.put(msg.getREQUEST_CREATOR(),sendBack);
                answerNodeGraph(msg);
            } else {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Nicht der letzte Knoten. Nachricht Weiterleiten!");
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP());
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setEchoRequest(waitingFor, msg.getMessageFrom());
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setNodeGraphRequest(serverCTR.getServerName(), dontVisist);
                serverCTR.sendAll(waitingFor, false, msg);
            }
        } else {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Der bekommt einen Leeren Graphen");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeGrapAnswer(serverCTR.getServerName(), new GraphPaul());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }


    public synchronized void initNodeCount() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeCountRequest(dontVisit, clientName);

        HashSet<String> empty = new HashSet<String>();

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP());
        retType.setEchoRequest(dontVisit, clientName);
        returnToSender.put(clientName, retType);

        serverCTR.sendAll(empty, true, msg);
    }

    public synchronized void answerNodeCount(Message msg) {
        if (msg.isNodeCount_2nd()) {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Message ist eine Antwort");
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if (deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName()))
                deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
            if (deliveryInformations != null) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Für die Message gibt es einen Eintrag");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if (deliveryInformations.waitingForAnswer.isEmpty()) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Keine ausstehenden Antworten");
                int retInt = 1;
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    if (deliveryInformations.answers.get(count) != null) {
                        retInt += deliveryInformations.answers.get(count).getNodeCount();
                    }
                }
                if (serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())) {
                    if(debugMode)
                        System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin der Knoten der gefragt hat");
                    System.out.println("Soooo viele Knoten: " + retInt);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                } else {
                    if(debugMode)
                        System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin nicht der Knoten der gefragt hat");
                    msg.setNodeCountAnswer(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                }
            }
        } else {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Antwort_6");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCountAnswer(1, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public synchronized void forwardNodeCount(Message msg) {
        if (!returnToSender.containsKey(msg.getREQUEST_CREATOR()) ||
             returnToSender.get(msg.getREQUEST_CREATOR()).REQUEST_TIMESTAMP != msg.getREQUEST_TIMESTAMP()) {
            if (isLastNode(msg.getNodeSet())) {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Zurück damit. Bin der Letzte Node");
                answerNodeCount(msg);
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP());
                returnToSender.put(msg.getREQUEST_CREATOR(),sendBack);
            } else {
                if(debugMode)
                    System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Nicht der letzte Node. Nachricht Weiterleiten!");
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP());
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setEchoRequest(waitingFor, msg.getMessageFrom());
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setNodeCountRequest(dontVisist, serverCTR.getServerName());

                serverCTR.sendAll(waitingFor, false, msg);
            }
        } else {
            if(debugMode)
                System.out.println("Nachricht von " + msg.getMessageFrom() + ":" + "Der bekommt nur Müll zurück");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCountAnswer(0, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public boolean isLastNode(HashSet<String> visited) {
        return visited.containsAll(serverCTR.generateNodeSet());
    }

    public GraphPaul generateGraph() {
        HashSet<String> arg = serverCTR.generateNodeSet();
        String[] array = arg.toArray(new String[0]);

        GraphPaul graph = new GraphPaul();

        for (int i = 0; i < array.length; ++i) {
            for (int c = 0; c < array.length; ++c) {
                graph.connectNode(array[i], array[c]);
            }
        }
        return graph;
    }
}
