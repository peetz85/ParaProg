package server;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message implements Serializable {

    final private String REQUEST_CREATOR;
    public Message(String REQUEST_CREATOR) {
        this.REQUEST_CREATOR = REQUEST_CREATOR;
    }
    public String getREQUEST_CREATOR(){
        return REQUEST_CREATOR;
    }

    //Wird benötigt für:    Request Echo +
    //
    private String messageFrom;
    public String getMessageFrom() {
        return messageFrom;
    }

    //Channel löschen
    private boolean terminateSignal;
    private String terminateServerName;

    public boolean isTerminateSignal(){

        return terminateSignal;
    }
    public String getTerminateServerName(){

        return terminateServerName;
    }
    public void setTerminateSignal(String serverName){
        terminateServerName = serverName;
        terminateSignal = true;
    }

    //Test Integer
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

    //Handshake Singal
    private boolean handshake_1st;
    private boolean handshake_2nd;
    private String label;

    public void setLabel(String label, boolean handshake_2nd){
        this.label = label;
        handshake_1st = true;
        this.handshake_2nd = handshake_2nd;
    }
    public boolean isHandshake_1st(){

        return handshake_1st;
    }
    public boolean isHandshake_2nd(){
        return handshake_2nd;
    }

    public String getLabel(){

        return label;
    }

    //Request Echo
    private boolean echoRequest_1st; //Deklariert ob die Message eine Echo-Anfrage ist
    private boolean echoRequest_2nd; //Deklariert ob die Message eine Antwort auf eine Echo-Anfrage ist
    private HashSet<String> nodeSet;
    private int nodeCount;
    
    public boolean isEchoRequest_1st() {
		return echoRequest_1st;
	}
	public boolean isEchoRequest_2nd() {
		return echoRequest_2nd;
	}  
    public HashSet<String> getNodeSet() {
		return nodeSet;
	}
	public void setNodeSet(HashSet<String> nodeSet, String messageFrom) {
		echoRequest_1st = true;
		echoRequest_2nd = false;
		this.nodeSet = nodeSet;
        this.messageFrom = messageFrom;
	}
	public int getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(int nodeCount, String messageFrom) {
		echoRequest_1st = true;
		echoRequest_2nd = true;
        nodeSet = null;
		this.nodeCount = nodeCount;
        this.messageFrom = messageFrom;
	}
}
