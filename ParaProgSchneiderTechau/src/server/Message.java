package server;

import java.io.Serializable;

/**
 * Created by Pascal on 09.07.2014.
 */
public class Message implements Serializable {
    public boolean WAKEUP;

    private int i;
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
