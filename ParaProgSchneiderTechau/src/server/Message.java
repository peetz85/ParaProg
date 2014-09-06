package server;

import java.io.Serializable;
import java.util.HashSet;
import client.GraphPaul;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message implements Serializable {

    final private String REQUEST_CREATOR;
    final private long REQUEST_TIMESTAMP;

    public Message(String REQUEST_CREATOR) {
        this.REQUEST_CREATOR = REQUEST_CREATOR;
        REQUEST_TIMESTAMP = System.currentTimeMillis();
    }
    public String getREQUEST_CREATOR(){
        return REQUEST_CREATOR;
    }
    public long getREQUEST_TIMESTAMP(){
        return REQUEST_TIMESTAMP;
    }

    /**
     * Benötigt von:    NodeCount
     *                  GraphNode
     *                  HandShakeSignal
     */
    private String messageFrom;
    public String getMessageFrom() { return messageFrom; }

    /**
     * Benötigt von:    NodeCount
     *                  GraphNode
     */
    private HashSet<String> nodeSet;
    public HashSet<String> getNodeSet() {
        return nodeSet;
    }

    //--------------------------Terminate Signal--------------------------
    // Dieses Signal wird vom ServerChannel verarbeitet und nicht in die
    // MessageBox weitergeleitet
    private boolean terminateSignal;
    public boolean isTerminateSignal(){ return terminateSignal; }
    public void setTerminateSignal(){ terminateSignal = true; }

    //--------------------------Handshake Singal--------------------------
    // Dieses Signal wird vom ServerChannel verarbeitet und nicht in die
    // MessageBox weitergeleitet
    private boolean handshake_1st;
    private boolean handshake_2nd;
    public void setLabel(String label, boolean handshake_2nd){
        messageFrom = label;
        handshake_1st = true;
        this.handshake_2nd = handshake_2nd;
    }
    public boolean isHandshake_1st() { return handshake_1st; }
    public boolean isHandshake_2nd(){
        return handshake_2nd;
    }


    //--------------------------Test Integer--------------------------
    //Debug Test ... Kann gelöscht werden
    //TODO
    private boolean cInteger;
    private int i;
    public boolean iscInteger(){

        return cInteger;
    }
    public int getI() {

        return i;
    }

    public void setI(int i, String from) {
        this.i = i;
        cInteger = true;
        messageFrom = from;
    }


    //--------------------------Request NodeCount--------------------------
    private boolean nodeCount_1st; //Deklariert ob die Message eine Node Count ist
    private boolean nodeCount_2nd; //Deklariert ob die Message eine Antwort auf eine Node Count ist
    private int nodeCount;
    //final String  MESSAGE_CREATOR;
    //private String messageFrom
    //private HashSet nodeSet;

    public boolean isNodeCount_1st() {
		return nodeCount_1st;
	}
	public boolean isNodeCount_2nd() {
		return nodeCount_2nd;
	}
    public int getNodeCount() { return nodeCount; }

	public void setNodeCountRequest(HashSet<String> nodeSet, String messageFrom) {
		nodeCount_1st = true;
		nodeCount_2nd = false;
		this.nodeSet = nodeSet;
        this.messageFrom = messageFrom;
	}
	public void setNodeCountAnswer(int nodeCount, String messageFrom) {
		nodeCount_1st = true;
		nodeCount_2nd = true;
        nodeSet = null;
		this.nodeCount = nodeCount;
        this.messageFrom = messageFrom;
	}

    //--------------------------Request NodeGraph "Spannbaum"--------------------------
    private boolean nodeGraph_1st;
    private boolean nodeGraph_2nd;
    //final String  MESSAGE_CREATOR;
    //private String messageFrom
    //private HashSet nodeSet
    private GraphPaul spannBaum;

    public boolean isNodeGraph_1st(){return nodeGraph_1st;}
    public boolean isNodeGraph_2nd(){return nodeGraph_2nd;}
    public GraphPaul getSpannBaum() {return spannBaum;}


    public void setNodeGraphRequest(String messageFrom, HashSet<String> nodeSet){
        nodeGraph_1st = true;
        nodeGraph_2nd = false;
        this.messageFrom = messageFrom;
        this.nodeSet = nodeSet;
    }
    public void setNodeGrapAnswer(String messageFrom, GraphPaul spannBaum){
        nodeGraph_1st = true;
        nodeGraph_2nd = true;
        this.messageFrom = messageFrom;
        this.spannBaum = spannBaum;
    }
}
