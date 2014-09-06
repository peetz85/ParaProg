package gui.console;

import client.ClientController;
import server.Message;
import server.ServerChannel;
import server.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Console extends JFrame{
	
	public JLabel lblNewLabel;
	public JTextArea textArea;
    public static ServerController serverCTR;
    public static ClientController clientCTR;

	
	
	public Console() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event1) {
                beenden();
            }
        });

        setTitle("Client");
        setSize(500,300);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-250,Toolkit.getDefaultToolkit().getScreenSize().height/2-150);

        clientCTR = new ClientController("Default");
        serverCTR = new ServerController("Default", clientCTR);
        clientCTR.setServerCTR(serverCTR);
        clientCTR.start();

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
        textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        MessageConsole mc = new MessageConsole(textArea);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

        JMenuItem mntmAktiviereServer = new JMenuItem("Verbindung bereitstellen");
        mntmAktiviereServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                OpenConnection arg = new OpenConnection(serverCTR);
                serverCTR.connectToNode(serverCTR.getServerName(), String.valueOf(serverCTR.getNextFreePort()));
                arg.setLocation(getLocationOnScreen().x+100,getLocationOnScreen().y+90);
                arg.setVisible(true);
            }
        });
        mnMenu.add(mntmAktiviereServer);

		JMenuItem mntmNeueVerbindung = new JMenuItem("Neue Verbindung");
		mntmNeueVerbindung.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ConnectConnection arg = new ConnectConnection(serverCTR);
                arg.setLocation(getLocationOnScreen().x+100,getLocationOnScreen().y+90);
                arg.setVisible(true);
            }
        });
		mnMenu.add(mntmNeueVerbindung);

        JMenuItem mntmShowConnections = new JMenuItem("Zeige Nachbarn");
        mntmShowConnections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                serverCTR.printAllNodesFancy();
            }
        });
        mnMenu.add(mntmShowConnections);
		
        JSeparator separator_ = new JSeparator();
        mnMenu.add(separator_);
		
		JMenuItem mntmCountNode = new JMenuItem("Knoten zählen");
		mntmCountNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                serverCTR.clientCTR.initNodeCount();
			}
		});
		mnMenu.add(mntmCountNode);

        JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
        mntmGeneriereGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverCTR.clientCTR.initNodeGraph();
            }
        });
        mnMenu.add(mntmGeneriereGraph);

        JMenuItem mntmNachricht = new JMenuItem("Sende WakeUp");
        mntmNachricht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!serverCTR.connections.isEmpty()) {
                    Message tmp = new Message(serverCTR.getServerName());
                    tmp.setI(11,serverCTR.getServerName());
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
                beenden();
			}
		});
		mnMenu.add(mntmBeenden);

	}

    public void beenden(){
        serverCTR.terminateConnections();

        try {
            Thread.sleep(500);
        }catch (Exception e){}

        System.exit(0);
    }

}
