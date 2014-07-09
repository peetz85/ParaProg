package server;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message {

    public String test;
    public Integer i;
    public final boolean WAKEUP;


    public Message(boolean b){
        WAKEUP = b;
    }

    public String toString(){
        return test;
    }
}
