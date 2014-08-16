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
    private HashSet<String> waitingForAnswer;
    private ArrayList<Message> answers;
    public boolean isEchoRequest() {
        return echoRequest;
    }
    public HashSet<String> getWaitingForAnswer(){
        return waitingForAnswer;
    }
    public void setEchoRequest(boolean echoRequest, HashSet<String> waitingForAnswer) {
        this.echoRequest = echoRequest;
        this.waitingForAnswer = waitingForAnswer;
        answers = new ArrayList<Message>();
    }
    public ArrayList<Message> getAnswers(){
        return answers;
    }


}
