package server;

import java.io.Serializable;

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
}
