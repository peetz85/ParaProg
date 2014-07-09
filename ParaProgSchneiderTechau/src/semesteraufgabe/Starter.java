package semesteraufgabe;

import client.ClientController;
import com.sun.corba.se.spi.activation.Server;
import gui.Console;

import org.jcsp.lang.*;
import server.Message;
import server.ServerChannel;
import server.ServerController;
import sun.org.mozilla.javascript.internal.EcmaError;

import java.net.SocketException;

public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        ClientController clientCTR = new ClientController();
        ServerController serverCTR = new ServerController("Hans");

        Console c = new Console(clientCTR, serverCTR);
        c.setLocation(50, 50);
        c.setSize(500,300);
        c.setVisible(true);

        serverCTR.start();

        System.out.println("Test");

        while(true) {
            serverCTR.t1.send(new Message(5));
        }

    }

}
