package gui;

import server.ServerController;

public class StandardCNSServer extends IPConnector{

	public StandardCNSServer(ServerController server) {
		super(server);
	}

	public void connect() {
        server.setCNSServer(textField.getText());
    }

}
