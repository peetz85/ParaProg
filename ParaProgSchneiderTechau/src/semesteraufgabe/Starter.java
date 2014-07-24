package semesteraufgabe;

import client.ClientController;
import gui.startup.StartServerOrConnectServer;
import server.ServerController;

public class Starter {


	/**
	 * @param args
	 */
	public static void main(String[] args) {


        ClientController clientCTR = new ClientController();
        ServerController serverCTR = new ServerController("Hans");

        new StartServerOrConnectServer(serverCTR).setVisible(true);


        /*


        serverCTR.start();
        new Console(clientCTR, serverCTR).setVisible(true);
        */

    }

}