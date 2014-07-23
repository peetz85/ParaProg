package gui;

import server.ServerController;

public class StandardCNSServer extends IPConnector{

	public StandardCNSServer(ServerController server) {
		super(server);
	}

	public void connect_TRUE(){


        server.setCNSServer(textField.getText());
	}
	
}
