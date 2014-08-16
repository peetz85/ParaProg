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
    private HashSet<String> waitingForAnswer;
    private ArrayList<Message> answers;
    public boolean isEchoRequest() {
        return echoRequest;
    }
    public HashSet<String> getWaitingForAnswer(){
        return waitingForAnswer;
    }
    public void setEchoRequest(HashSet<String> waitingForAnswer, String sendBackTo) {
        this.echoRequest = true;
        this.waitingForAnswer = waitingForAnswer;
        answers = new ArrayList<Message>();
        this.sendBackTo = sendBackTo;
    }
    public ArrayList<Message> getAnswers(){
        return answers;
    }
    public String getSendBackTo() {
        return sendBackTo;
    }

}
