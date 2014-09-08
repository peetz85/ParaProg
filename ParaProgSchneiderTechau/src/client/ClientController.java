package client;

import semesteraufgabe.Starter;
import server.Message;
import server.ServerController;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ClientController extends Thread {

    private String clientName;
    private ServerController serverCTR;

    private int nextElectionTimeInSeconds;
    private long nextElectionTimeStamp;

    private HashMap<String, ReturnType> returnToSender;


    public ClientController(String name) {
        clientName = name;
        returnToSender = new HashMap<String, ReturnType>();
        nextElectionTimeInSeconds = generateElectionTime();
        nextElectionTimeStamp = System.currentTimeMillis() + (nextElectionTimeInSeconds * 1000);
    }

    private int generateElectionTime() {
        int min = 120;
        int max = 600;
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

    public void setServerCTR(ServerController serverCTR) {
        this.serverCTR = serverCTR;
    }

    public void setClientName(String arg) {
        clientName = arg;
    }

    private boolean checkIfRequestExist(Message msg) {
        boolean returnArgument = false;
        if (returnToSender.containsKey(msg.getREQUEST_CREATOR())) {
            if (returnToSender.get(msg.getREQUEST_CREATOR()).REQUEST_TIMESTAMP == msg.getREQUEST_TIMESTAMP()) {
                returnArgument = true;
            }
        }
        return returnArgument;
    }

    public void run() {
        Message msg = null;
        while (true) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }

            if (System.currentTimeMillis() >= nextElectionTimeStamp) {
                System.out.println(nextElectionTimeInSeconds);
                nextElectionTimeInSeconds = generateElectionTime();
                nextElectionTimeStamp = System.currentTimeMillis() + (nextElectionTimeInSeconds * 1000);
            }

            if (msg == null) {
                msg = serverCTR.getMessage();
            }
            if (msg != null) {
                if (msg.iscInteger()) {
                    System.out.println(msg.getI());
                    String sendTo = msg.getMessageFrom();
                    msg.setI(msg.getI() + 5, clientName);
                    serverCTR.sendOnly(sendTo, msg);
                }
                if (msg.isNodeCount_1st()) {
                    if (msg.isNodeCount_2nd()) {
                        answerNodeCount(msg);
                    } else {
                        forwardNodeCount(msg);
                    }
                }
                if (msg.isNodeGraph_1st()) {
                    if (msg.isNodeGraph_2nd()) {
                        answerNodeGraph(msg);
                    } else {
                        forwardNodeGraph(msg);
                    }
                }
                if (msg.isElection_1st()) {
                    if (msg.isElection_2nd()) {
                        answerElection(msg);
                    } else {
                        forwardElection(msg);
                    }
                }
                msg = null;
            }
        }
    }

    public synchronized void initElection() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setElectionRequest(clientName, dontVisit);

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setElection(dontVisit, clientName);
        returnToSender.put(clientName, retType);
        if (Starter.debugMode)
            System.err.println("#S: " + "Election Request generiert!");
        serverCTR.sendAll(new HashSet<String>(), true, msg);
    }

    public synchronized void answerElection(Message msg) {
        if (msg.isNodeGraph_2nd()) {
            if (returnToSender.containsKey(msg.getREQUEST_CREATOR())) {
                ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
                if (deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName())) {
                    deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
                }
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());

                if (deliveryInformations.waitingForAnswer.isEmpty()) {

                    Graph returnGraph = new Graph();
//                    returnGraph.addNodeSet(serverCTR.generateNodeSet(), clientName);
                    for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                        if (deliveryInformations.answers.get(count).getSpannBaum().getNodes() == null) {
                            returnGraph.deleteConnection(deliveryInformations.answers.get(count).getMessageFrom(), clientName);
                        } else {
                            returnGraph.addGraph(deliveryInformations.answers.get(count).getSpannBaum());
                        }
                    }
                    //TODO
                    //TODO Nodes aus dem Graphen entfernen die wir nicht besuchen sollen
                    //TODO Wird momentan nur bei Nodes gemacht von denen wir ein Null zurück bekommen
                    //TODO

                    if (clientName.equals(msg.getREQUEST_CREATOR())) {
                        if (Starter.debugMode)
                            System.err.println("#S: " + "Ich bin der Node der gefragt hat. Spannbaum ausgeben!");
                        System.out.println(returnGraph);
                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    } else {
                        if (Starter.debugMode)
                            System.err.println("<- Nachricht an " + msg.getMessageFrom() + ":" + "Antwort Graph zurückleiten an Request Node");
                        msg.setNodeGrapAnswer(serverCTR.getServerName(), returnGraph);
                        serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    }
                }
            }
        } else {
            if (Starter.debugMode)
                System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "Spannbaum genriert. Sende Spannbaum zurück!");
            String sendBackTo = msg.getMessageFrom();
            Graph returnGraph = new Graph();
            returnGraph.addNode(sendBackTo);
            returnGraph.addNode(clientName);
            returnGraph.addConnection(sendBackTo, clientName);
            msg.setNodeGrapAnswer(serverCTR.getServerName(), returnGraph);
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public synchronized void forwardElection(Message msg) {
        if (checkIfRequestExist(msg)) {
            if (Starter.debugMode)
                System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Election Request bereits von einem anderen Node erhalten, Sende leere Nachricht!");
            String sendBackTo = msg.getMessageFrom();
            msg.setElectionAwnser(clientName);
            serverCTR.sendOnly(sendBackTo, msg);
        } else {
            if (isLastNode(msg.getNodeSet())) {
                if (Starter.debugMode)
                    System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Ich bin der letzte Node im Baum. Sende Election Awnser zurück");
                //Falls Anfragen von anderen Nodes kommen um diese abzulehnen
                returnToSender.put(msg.getREQUEST_CREATOR(), new ReturnType(msg.getREQUEST_TIMESTAMP(), msg));
                answerElection(msg);


            } else {
                if (Starter.debugMode)
                    System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Nicht der letzte Node im Baum. Election Anfrage Weiterleiten!");
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
                HashSet<String> waitingFor = serverCTR.generateNodeSet();
                waitingFor.removeAll(msg.getNodeSet());
                sendBack.setElection(waitingFor, msg.getMessageFrom());
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);

                //Neue Liste für nächsten Node mit aktualisierter Liste
                //der nicht zu besuchenden Nodes
                HashSet<String> dontVisist = msg.getNodeSet();
                dontVisist.addAll(waitingFor);
                msg.setElectionRequest(serverCTR.getServerName(), dontVisist);
                serverCTR.sendAll(waitingFor, false, msg);
            }
        }
    }


    public synchronized void initNodeGraph() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeGraphRequest(clientName, dontVisit);

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setEchoRequest(dontVisit, clientName);
        returnToSender.put(clientName, retType);
        if (Starter.debugMode)
            System.err.println("#S: " + "Spannbaum Request generiert!");
        serverCTR.sendAll(new HashSet<String>(), true, msg);
    }

    public synchronized void answerNodeGraph(Message msg) {
        if (msg.isNodeGraph_2nd()) {
            if (returnToSender.containsKey(msg.getREQUEST_CREATOR())) { //TODO Node und TIMESTAMP Abfragen
                ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
                if (deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName())) {
                    deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
                }
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());

                if (deliveryInformations.waitingForAnswer.isEmpty()) {

                    Graph returnGraph = new Graph();
                    for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                        if (deliveryInformations.answers.get(count).getSpannBaum().getNodes() != null) {
                            returnGraph.addGraph(deliveryInformations.answers.get(count).getSpannBaum());
                        }
                    }
                    returnGraph.addNode(deliveryInformations.getSendBackTo());
                    returnGraph.addNode(clientName);
                    returnGraph.addConnection(clientName, deliveryInformations.getSendBackTo());

                    if (clientName.equals(msg.getREQUEST_CREATOR())) {
                        if (Starter.debugMode)
                            System.err.println("#S: " + "Ich bin der Node der gefragt hat. Spannbaum ausgeben!");
                        exportGraphToUDG(returnGraph);
                        System.out.println(returnGraph);
                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    } else {
                        if (Starter.debugMode)
                            System.err.println("<- Nachricht an " + msg.getMessageFrom() + ":" + "Antwort Graph zurückleiten an Request Node");
                        msg.setNodeGrapAnswer(clientName, returnGraph);
                        serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    }
                }
            }
        } else {
            if (Starter.debugMode)
                System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "Spannbaum genriert. Sende Spannbaum zurück!");
            String sendBackTo = msg.getMessageFrom();
            Graph returnGraph = new Graph();
            returnGraph.addNode(sendBackTo);
            returnGraph.addNode(clientName);
            returnGraph.addConnection(sendBackTo, clientName);
            msg.setNodeGrapAnswer(serverCTR.getServerName(), returnGraph);
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public synchronized void forwardNodeGraph(Message msg) {
        if (checkIfRequestExist(msg)) {
            if (Starter.debugMode)
                System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Request bereits von einem anderen Node erhalten, Sende leere Nachricht!");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeGrapAnswer(serverCTR.getServerName(), new Graph());
            serverCTR.sendOnly(sendBackTo, msg);
        } else {
            if (isLastNode(msg.getNodeSet())) {
                if (Starter.debugMode)
                    System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Ich bin der letzte Node in dem Baum. Generiere meinen Graph und Sende zurück");
                //Falls Anfragen von anderen Nodes kommen um diese abzulehnen
                returnToSender.put(msg.getREQUEST_CREATOR(), new ReturnType(msg.getREQUEST_TIMESTAMP(), msg));
                answerNodeGraph(msg);
            } else {
                if (Starter.debugMode)
                    System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Nicht der letzte Node im Baum. Anfrage Weiterleiten!");
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
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
        }
    }


    public synchronized void initNodeCount() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeCountRequest(dontVisit, clientName);

        HashSet<String> empty = new HashSet<String>();

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setEchoRequest(dontVisit, clientName);
        returnToSender.put(clientName, retType);

        serverCTR.sendAll(empty, true, msg);
    }

    public synchronized void answerNodeCount(Message msg) {
        if (msg.isNodeCount_2nd()) {
            if (Starter.debugMode)
                System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Message ist eine Antwort");
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if (deliveryInformations.waitingForAnswer.contains(serverCTR.getServerName()))
                deliveryInformations.waitingForAnswer.remove(serverCTR.getServerName());
            if (deliveryInformations != null) {
                if (Starter.debugMode)
                    System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Für die Message gibt es einen Eintrag");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if (deliveryInformations.waitingForAnswer.isEmpty()) {
                if (Starter.debugMode)
                    System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Keine ausstehenden Antworten");
                int retInt = 1;
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    if (deliveryInformations.answers.get(count) != null) {
                        retInt += deliveryInformations.answers.get(count).getNodeCount();
                    }
                }
                if (serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())) {
                    if (Starter.debugMode)
                        System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin der Knoten der gefragt hat");
                    System.out.println("Knoten die geantwortet haben: " + retInt);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                } else {
                    if (Starter.debugMode)
                        System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "answerNodeCount - Ich bin nicht der Knoten der gefragt hat");
                    msg.setNodeCountAnswer(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                }
            }
        } else {
            if (Starter.debugMode)
                System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "Antwort_6");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCountAnswer(1, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public synchronized void forwardNodeCount(Message msg) {
        if (!returnToSender.containsKey(msg.getREQUEST_CREATOR()) ||
                returnToSender.get(msg.getREQUEST_CREATOR()).REQUEST_TIMESTAMP != msg.getREQUEST_TIMESTAMP()) {
            if (isLastNode(msg.getNodeSet())) {
                if (Starter.debugMode)
                    System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "Zurück damit. Bin der Letzte Node");
                answerNodeCount(msg);
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);
            } else {
                if (Starter.debugMode)
                    System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "Nicht der letzte Node. Nachricht Weiterleiten!");
                //ReturnType erstellen mit Sender der Generator der alten Nachricht
                //und Liste(HashSet) der abzuwartenden Sender
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
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
            if (Starter.debugMode)
                System.err.println("Nachricht von " + msg.getMessageFrom() + ":" + "Der bekommt nur Müll zurück");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCountAnswer(0, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }

    public boolean isLastNode(HashSet<String> visited) {
        return visited.containsAll(serverCTR.generateNodeSet());
    }

    private void exportGraphToUDG(Graph graph) {
        String uDrawGraph = "[";
        HashSet<String> hashsetKanten = new HashSet<String>();

        if (graph != null && graph.getNodes() != null) {
            for (String startNode : graph.getNodes()) {
                // Node
                uDrawGraph += "l(\"" + startNode + "\",n(\"Module\",[a(\"COLOR\",\"#ffffff\"),a(\"OBJECT\",\"" +
                        startNode + "\"),a(\"_GO\",\"ellipse\")],[";
                // Connections
                for (String targetNode : graph.getNodes()) {
                    if (graph.isNodeConnected(startNode, targetNode) && !hashsetKanten.contains(startNode + ":" + targetNode)) {
                        hashsetKanten.add(startNode + ":" + targetNode);
//                        hashsetKanten.add(targetNode + ":" + startNode);
                        uDrawGraph += "l(\"" + startNode + "-" +
                                targetNode + "\",e(\"\",[a(\"_DIR\",\"none\"),a(\"EDGECOLOR\",\"" + "#000000" +
                                "\"),a(\"EDGEPATTERN\",\"normal\")],r(\"" + targetNode + "\"))),";
                    }
                }
                uDrawGraph += "])),";
            }
        }
        uDrawGraph += "]";
        try {
            FileWriter f = new FileWriter("Spannbaum_" + clientName + ".udg");
            f.write(uDrawGraph);
            f.close();
        } catch (Exception e) {
            System.err.println("Fehler beim Schreiben der Graphen: ");
            if (Starter.debugMode)
                e.printStackTrace();
        }
    }
}
