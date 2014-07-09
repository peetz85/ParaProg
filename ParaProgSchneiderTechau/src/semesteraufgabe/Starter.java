package semesteraufgabe;

import gui.Console;

import org.jcsp.lang.*;
import server.ServerController;

import java.net.SocketException;

public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Console c = new Console();
		c.setLocation(50, 50);
		c.setSize(400,300);
		c.setVisible(true);


        ServerController tmp = new ServerController();
        try {
            tmp.getIPAdress();
        } catch (SocketException e){};

	}

}
