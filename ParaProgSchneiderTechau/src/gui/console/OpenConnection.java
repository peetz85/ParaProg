package gui.console;

import javax.swing.JDialog;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import server.ServerController;

import java.awt.Font;

public class OpenConnection extends JDialog{
	
	private JLabel lblNewLabel_1;
	
	public OpenConnection(ServerController serverCTR) {
		
		setTitle("Offene Verbindung");
        setSize(300, 90);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Verbindung steht unter folgendem Namen bereit: ");
		getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Server: " +serverCTR.getServerName()+" Port: "+String.valueOf(serverCTR.getNextFreePort()));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(lblNewLabel_1);
        setVisible(true);
        serverCTR.connectToNode(serverCTR.getServerName(), String.valueOf(serverCTR.getNextFreePort()), true);
       	dispose();
	}
}
