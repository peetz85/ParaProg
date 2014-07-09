package semesteraufgabe;

import client.ClientController;
import com.sun.corba.se.spi.activation.Server;
import gui.Console;

import org.jcsp.lang.*;
import server.ServerController;

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



	}

}
