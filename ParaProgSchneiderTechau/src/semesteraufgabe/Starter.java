package semesteraufgabe;

import client.ClientController;
import gui.startup.StartServerOrConnectServer;
import server.ServerController;

public class Starter {

    public static ServerController serverCTR = new ServerController("Default");
    public static ClientController clientCTR  = new ClientController();

	/**
	 * @param args
	 */
	public static void main(String[] args) {


        new StartServerOrConnectServer().setVisible(true);



        serverCTR.start();
        /*
        new Console(clientCTR, serverCTR).setVisible(true);
        */

    }

}