package semesteraufgabe;

import client.ClientController;
import gui.Console;
import server.ServerController;

public class Starter {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

        ClientController clientCTR = new ClientController();

        ServerController serverCTR = new ServerController("Hans");

        serverCTR.start();
        new Console(clientCTR, serverCTR).setVisible(true);

    }

}