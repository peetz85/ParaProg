package semesteraufgabe;

import client.ClientController;
import gui.startup.StartServerOrConnectServer;
import server.ServerController;

public class Starter {

    public static ServerController serverCTR;
    public static ClientController clientCTR;

	/**
	 * @param args
	 */
	public static void main(String[] args) {


        new StartServerOrConnectServer().setVisible(true);

    }

}