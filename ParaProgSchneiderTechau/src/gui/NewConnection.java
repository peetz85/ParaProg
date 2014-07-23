package gui;

import server.ServerController;
import javax.swing.JButton;
import java.awt.BorderLayout;

/**
 * Created by Pascal on 23.07.2014.
 */
public class NewConnection extends IPConnector{
    public NewConnection(ServerController server) {
        super(server);
    }

    public void connect(){
    	server.connectToNode(textField.getText());
    }
}
