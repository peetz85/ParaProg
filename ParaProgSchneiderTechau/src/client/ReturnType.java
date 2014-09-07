package client;

import server.Message;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Pascal on 16.08.2014.
 */
public class ReturnType {

    public ReturnType(long timeStamp, Message msg){
        ORIGINAL_MESSAGE = msg;
        REQUEST_TIMESTAMP = timeStamp;
    }
    public final long REQUEST_TIMESTAMP;
    public final Message ORIGINAL_MESSAGE;


    private String sendBackTo;
    public String getSendBackTo() { return sendBackTo; }

    public HashSet<String> waitingForAnswer;
    public ArrayList<Message> answers;


    // - - - - - - - - - - - - - Echo Request - - - - - - - - - - - - -
    private boolean echoRequest;
    public boolean isEchoRequest() {
        return echoRequest;
    }
    public void setEchoRequest(HashSet<String> waitingForAnswer, String sendBackTo) {
        echoRequest = true;
        this.waitingForAnswer = waitingForAnswer;
        answers = new ArrayList<Message>();
        this.sendBackTo = sendBackTo;
    }

    // - - - - - - - - - - - - - Election - - - - - - - - - - - - -
    private boolean election_1st;
    public boolean isElection(){return election_1st;}
    public void setElection(HashSet<String> waitingForAnswer, String sendBackTo){
        election_1st = true;
        this.waitingForAnswer = waitingForAnswer;
        answers = new ArrayList<Message>();
        this.sendBackTo = sendBackTo;
    }
}
