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

    private boolean echoRequest;
    public final long REQUEST_TIMESTAMP;
    public final Message ORIGINAL_MESSAGE;
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

    public Message getORIGINAL_MESSAGE(){return ORIGINAL_MESSAGE;}

}
