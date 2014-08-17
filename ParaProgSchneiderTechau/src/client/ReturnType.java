package client;

import server.Message;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Pascal on 16.08.2014.
 */
public class ReturnType {

    public ReturnType(){}

    private boolean echoRequest;



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
