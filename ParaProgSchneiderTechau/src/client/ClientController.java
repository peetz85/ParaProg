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

    public void initElection() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setElectionRequest(clientName, dontVisit);

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setElection(dontVisit, clientName);
        retType.waitingForAnswer.remove(clientName);
        returnToSender.put(clientName, retType);
        if (Starter.debugMode)
            System.err.println("#S: " + "Election Request generiert!");
        serverCTR.sendAll(new HashSet<String>(), true, msg);
    }
    public void answerElection(Message msg) {
        if (msg.isElection_2nd()) {
            if (checkIfRequestExist(msg)) {
                ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());

                if (deliveryInformations.waitingForAnswer.isEmpty()) {

                    for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                        deliveryInformations.answers.get(count);

                        //TODO
                        //TODO Auswertung aller gesammelten Nachrichten hier einfügen
                    }

                    if (clientName.equals(msg.getREQUEST_CREATOR())) {
                        if (Starter.debugMode)
                            System.err.println("#S: " + "ELECTION DEBUG MESSAGE FEHLT HIER !!!!!!!!!!!!!!!!!!!!!!!!!");

                        //TODO
                        //TODO Aufgabe für den Request Ersteller hier einfügen

                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    } else {
                        if (Starter.debugMode)
                            System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "ELECTION DEBUG MESSAGE FEHLT HIER !!!!!!!!!!!!!!!!!!!!!!!!!");

                        //TODO
                        //TODO Aufgabe für einen Knoten in der Mitte hier einfügen
                        //TODO Nicht der letzte Knoten und nicht der Request ersteller


                        serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                        returnToSender.remove(msg.getREQUEST_CREATOR());
                    }
                }
            }
        } else {
            if (Starter.debugMode)
                System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "ELECTION DEBUG MESSAGE FEHLT HIER !!!!!!!!!!!!!!!!!!!!!!!!!");
            String sendBackTo = msg.getMessageFrom();

            //TODO
            //TODO Aufgabe für den letzten Knoten im Baum hier enfügen

            serverCTR.sendOnly(sendBackTo, msg);
        }
    }
    public void forwardElection(Message msg) {
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


    public void initNodeGraph() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeGraphRequest(clientName, dontVisit);

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setEchoRequest(dontVisit, clientName);
        retType.waitingForAnswer.remove(clientName);
        returnToSender.put(clientName, retType);
        if (Starter.debugMode)
            System.err.println("#S: " + "Spannbaum Request generiert!");
        serverCTR.sendAll(new HashSet<String>(), true, msg);
    }
    public void answerNodeGraph(Message msg) {
        if (msg.isNodeGraph_2nd()) {
            if (checkIfRequestExist(msg)) {
                ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
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
                System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "Sende Spannbaum zurück!");
            String sendBackTo = msg.getMessageFrom();
            Graph returnGraph = new Graph();
            returnGraph.addNode(sendBackTo);
            returnGraph.addNode(clientName);
            returnGraph.addConnection(sendBackTo, clientName);
            msg.setNodeGrapAnswer(serverCTR.getServerName(), returnGraph);
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }
    public void forwardNodeGraph(Message msg) {
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


    public void initNodeCount() {
        Message msg = new Message(clientName);
        HashSet<String> dontVisit = new HashSet<String>(serverCTR.generateNodeSet());
        msg.setNodeCountRequest(dontVisit, clientName);

        HashSet<String> empty = new HashSet<String>();

        ReturnType retType = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
        retType.setEchoRequest(dontVisit, clientName);
        retType.waitingForAnswer.remove(clientName);
        returnToSender.put(clientName, retType);

        serverCTR.sendAll(empty, true, msg);
    }
    public void answerNodeCount(Message msg) {
        if (msg.isNodeCount_2nd()) {
            ReturnType deliveryInformations = returnToSender.get(msg.getREQUEST_CREATOR());
            if (deliveryInformations != null) {
                if (Starter.debugMode)
                    System.err.println("#S: Für die Antwort von " + msg.getMessageFrom() + " gibt es einen Eintrag");
                deliveryInformations.answers.add(msg);
                deliveryInformations.waitingForAnswer.remove(msg.getMessageFrom());
            }
            if (deliveryInformations.waitingForAnswer.isEmpty()) {
                if (Starter.debugMode)
                    System.err.println("#S: Keine ausstehenden Antworten. Nachrichten werden ausgewertet!");
                int retInt = 1;
                for (int count = 0; count < deliveryInformations.answers.size(); count++) {
                    if (deliveryInformations.answers.get(count) != null) {
                        retInt += deliveryInformations.answers.get(count).getNodeCount();
                    }
                }
                if (serverCTR.getServerName().equals(msg.getREQUEST_CREATOR())) {
                    if (Starter.debugMode)
                        System.err.println("#S: Ich bin der Knoten der die Anfrage erstellt hat");
                    System.out.println("#S: Knoten die geantwortet haben-> " + retInt + " (inkl. diesem Knoten)");
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                } else {
                    if (Starter.debugMode)
                        System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "Antwort zurück senden!");
                    msg.setNodeCountAnswer(retInt, serverCTR.getServerName());
                    serverCTR.sendOnly(deliveryInformations.getSendBackTo(), msg);
                    returnToSender.remove(msg.getREQUEST_CREATOR());
                }
            }
        } else {
            if (Starter.debugMode)
                System.err.println("<- Nachricht an " + msg.getMessageFrom() + ": " + "Keine weiteren Knoten vorhanden!");
            String sendBackTo = msg.getMessageFrom();
            msg.setNodeCountAnswer(1, serverCTR.getServerName());
            serverCTR.sendOnly(sendBackTo, msg);
        }
    }
    public void forwardNodeCount(Message msg) {
        if (!returnToSender.containsKey(msg.getREQUEST_CREATOR()) ||
                returnToSender.get(msg.getREQUEST_CREATOR()).REQUEST_TIMESTAMP != msg.getREQUEST_TIMESTAMP()) {
            if (isLastNode(msg.getNodeSet())) {
                ReturnType sendBack = new ReturnType(msg.getREQUEST_TIMESTAMP(), msg);
                returnToSender.put(msg.getREQUEST_CREATOR(), sendBack);
                answerNodeCount(msg);
            } else {
                if (Starter.debugMode)
                    System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Nicht der letzte Knoten im Baum. Anfrage Weiterleiten!");
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
                System.err.println("-> Nachricht von " + msg.getMessageFrom() + ": " + "Anfrage bereits von einem anderen Knoten erhalten, Sende leere Nachricht!");
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
