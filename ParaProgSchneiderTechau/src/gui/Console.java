package gui;

import client.ClientController;
import com.sun.corba.se.spi.activation.Server;
import server.ServerController;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class Console extends JFrame{
	
	public JLabel lblNewLabel;
	public JTextArea textArea;

    private ClientController clientCTR;
    private ServerController serverCTR;
	
	
	public Console(ClientController cc, ServerController sc) {
		clientCTR = cc;
        serverCTR = sc;


		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator_1 = new JSeparator();
		panel_1.add(separator_1, BorderLayout.NORTH);
		
		lblNewLabel = new JLabel("IP-Adresse: " + serverCTR.getIPAdress() + " | Server-Name: " +serverCTR.serverName);
		panel_1.add(lblNewLabel, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmNeueVerbindung = new JMenuItem("Neue Verbindung");
		mnMenu.add(mntmNeueVerbindung);
		
		JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
		mnMenu.add(mntmGeneriereGraph);
		
		JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mnMenu.add(mntmBeenden);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
