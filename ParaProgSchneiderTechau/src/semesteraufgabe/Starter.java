package semesteraufgabe;

import client.ClientController;
import gui.Console;


import server.CNSServer;
import server.Message;
import server.ServerController;


import org.jcsp.lang.*;
import org.jcsp.net.*;
import org.jcsp.net.cns.*;
import org.jcsp.net.tcpip.*;

import java.io.IOException;

public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

        ClientController clientCTR = new ClientController();

        ServerController serverCTR = new ServerController("Hans");

        ServerController serverCTR_1 = new ServerController("Otto");


        Console c = new Console(clientCTR, serverCTR);
        c.setLocation(50, 50);
        c.setSize(500,300);
        c.setVisible(true);



        //CNSServer.startCNS();

        //serverCTR_1.startConnection("1");
        //serverCTR_1.start();


        serverCTR.connectConnection(serverCTR_1.serverName);
        serverCTR.start();
        serverCTR.connections.get(serverCTR_1.serverName).send(5);
//*/
    }

}