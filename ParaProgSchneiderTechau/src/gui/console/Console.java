package gui.console;

import client.ClientController;
import server.Message;
import server.ServerChannel;
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
    public static ServerController serverCTR;
    public static ClientController clientCTR;

	
	
	public Console() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Client");
        setSize(500,300);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-250,Toolkit.getDefaultToolkit().getScreenSize().height/2-150);

        serverCTR = new ServerController("Default");
        clientCTR = new ClientController("Default");
        new StartupConsole(serverCTR, clientCTR).setVisible(true);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator_1 = new JSeparator();
		panel_1.add(separator_1, BorderLayout.NORTH);
		
		lblNewLabel = new JLabel("IP-Adresse: " + serverCTR.getServerIP() + " | Server-Name: " +serverCTR.getServerName());
		panel_1.add(lblNewLabel, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

        JMenuItem mntmAktiviereServer = new JMenuItem("Verbindung bereitstellen");
        mntmAktiviereServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new OpenConnection(serverCTR).setVisible(true);;
            }
        });
        mnMenu.add(mntmAktiviereServer);

		JMenuItem mntmNeueVerbindung = new JMenuItem("Neue Verbindung");
		mntmNeueVerbindung.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new ConnectConnection(serverCTR).setVisible(true);
            }
        });
		mnMenu.add(mntmNeueVerbindung);
		
        JSeparator separator_ = new JSeparator();
        mnMenu.add(separator_);
		
		JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
		mntmGeneriereGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                System.out.println("Nix ist Los");
			}
		});
		mnMenu.add(mntmGeneriereGraph);

        JMenuItem mntmNachricht = new JMenuItem("Sende WakeUp");
        mntmNachricht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!serverCTR.connections.isEmpty()) {
                    Message tmp = new Message();
                    tmp.setI(11);
                    for (ServerChannel value : serverCTR.connections.values()) {
                        value.send(tmp);
                    }
                }
            }
        });
        mnMenu.add(mntmNachricht);

        JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminate();
                System.exit(0);
			}
		});
		mnMenu.add(mntmBeenden);

	}

    public void terminate(){
        if(!serverCTR.connections.isEmpty()) {
            Message terminateSignal = new Message();
            terminateSignal.setTerminateSignal(serverCTR.getServerName());
            for (ServerChannel value : serverCTR.connections.values()) {
                value.send(terminateSignal);
            }
        }
    }
}
