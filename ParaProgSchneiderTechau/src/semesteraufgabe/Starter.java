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


        ServerChannel t1 = new ServerChannel(true);
        ServerChannel t2 = new ServerChannel(false);

        t2.setInputChannel(t1.o2oChannelOutPut);
        t1.setInputChannel(t2.o2oChannelOutPut);

        Parallel par = new Parallel();
        par.addProcess(t1);
        par.addProcess(t2);
        par.run();
	}

}
