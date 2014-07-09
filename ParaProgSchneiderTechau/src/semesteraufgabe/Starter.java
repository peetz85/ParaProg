package semesteraufgabe;

import client.ClientController;
import gui.Console;

import server.Message;
import server.ServerController;


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
    }
}
