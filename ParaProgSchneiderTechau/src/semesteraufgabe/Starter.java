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
        //CNSServer.startCNS();


        ClientController clientCTR = new ClientController();

        ServerController serverCTR = new ServerController("Hans");

        ServerController serverCTR_1 = new ServerController("Otto");


        Console c = new Console(clientCTR, serverCTR_1);
        c.setLocation(50, 50);
        c.setSize(500,300);
        c.setVisible(true);




/*
        serverCTR_1.setCNSServer("127.0.0.1");
        serverCTR_1.startConnection("1");
        serverCTR_1.start();
//*/

/*
        serverCTR.setCNSServer("localhost");
        serverCTR.connectConnection(serverCTR_1.serverName);
        serverCTR.start();
        serverCTR.connections.get(serverCTR_1.serverName).send(5);
//*/
    }

}