package client;

import server.Message;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Pascal on 16.08.2014.
 */
public class ReturnType {

    public ReturnType(long timeStamp){
        REQUEST_TIMESTAMP = timeStamp;
    }

    private boolean echoRequest;
    public final long REQUEST_TIMESTAMP;
    private String sendBackTo;
    public HashSet<String> waitingForAnswer;
    public ArrayList<Message> answers;


    public boolean isEchoRequest() {
        return echoRequest;
    }
    public void setEchoRequest(HashSet<String> waitingForAnswer, String sendBackTo) {
        echoRequest = true;
        this.waitingForAnswer = waitingForAnswer;
        answers = new ArrayList<Message>();
        this.sendBackTo = sendBackTo;
    }
    public String getSendBackTo() {
        return sendBackTo;
    }

}
