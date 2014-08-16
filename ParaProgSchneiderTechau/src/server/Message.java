package server;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message implements Serializable {

    //Wakeup Signal
    private boolean wakeup;

    public boolean isWakeup() {
        return wakeup;
    }
    public void setWakeup(boolean WAKEUP) {
        wakeup = WAKEUP;
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

    public void setI(int i) {
        this.i = i;
        cInteger = true;
    }

    //Handshake Singal
    private boolean handshake;
    private boolean handshakeRequest;
    String label;

    public void setLabel(String label, boolean handshakeRequest){
        this.label = label;
        handshake = true;
        this.handshakeRequest = handshakeRequest;
    }
    public boolean isHandshake(){

        return handshake;
    }
    public boolean isHandshakeRequest(){
        return handshakeRequest;
    }

    public String getLabel(){

        return label;
    }

    //Request Echo
    private boolean echoRequest;
    private boolean echoAnswer;
    private HashSet<String> nodeSet;
    private int nodeCount;
    
    public boolean isEchoRequest() {
		return echoRequest;
	}
	public boolean isEchoAnswer() {
		return echoAnswer;
	}  
    public HashSet<String> getNodeSet() {
		return nodeSet;
	}
	public void setNodeSet(HashSet<String> nodeSet) {
		echoRequest = true;
		echoAnswer = false;
		this.nodeSet = nodeSet;
	}
	public int getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(int nodeCount) {
		echoRequest = true;
		echoAnswer = true;
		this.nodeCount = nodeCount;
	}
	
    
}
