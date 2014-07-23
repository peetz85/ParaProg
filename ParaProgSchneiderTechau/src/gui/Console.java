package gui;

import client.ClientController;
import server.CNSServer;
import server.ServerController;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Console extends JFrame{
	
	public JLabel lblNewLabel;
	public JTextArea textArea;

    public ClientController clientCTR;
    public ServerController serverCTR;
	
	
	public Console(ClientController cc, ServerController sc) {
		clientCTR = cc;
        serverCTR = sc;
        setTitle("Client");

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
		mntmNeueVerbindung.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new NewConnection(serverCTR).setVisible(true);


            }
        });
		mnMenu.add(mntmNeueVerbindung);
		
		JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
		mntmGeneriereGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		mnMenu.add(mntmGeneriereGraph);
		
		JSeparator separator_2 = new JSeparator();
		mnMenu.add(separator_2);
		final JMenuItem mntmCnsServerStarten = new JMenuItem("CNS Server starten");
		mntmCnsServerStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                CNSServer.startCNS();
                setTitle("Client | CNS Server running!");
                mntmCnsServerStarten.setEnabled(false);
			}
		});
		mnMenu.add(mntmCnsServerStarten);

        final JMenuItem mntmCNSServer = new JMenuItem("Standard CNS Server eintragen");
        mntmCNSServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new StandardCNSServer(serverCTR).setVisible(true);

                //TODO mntmCNSServer.setEnabled(false); //Prüfen ob richtige IP Eingegeben wurde UND die Verbindung aufgebaut wurde DANN Ausblenden
            }
        });

        mnMenu.add(mntmCNSServer);
		
		JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnMenu.add(mntmBeenden);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
