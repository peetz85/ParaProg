package server;

import java.io.Serializable;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message implements Serializable {
    public boolean WAKEUP;

    public boolean cInteger;



    private int i;
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
        cInteger = true;
    }
}
