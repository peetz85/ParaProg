package gui.console;

import client.ClientController;
import server.Message;
import server.ServerChannel;
import server.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;


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

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String serverIP = (String) ip.getHostAddress();
		lblNewLabel = new JLabel("IP-Adresse: " + serverIP + " | Server-Name: " +serverCTR.getServerName());
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
        mnMenu.setMnemonic(KeyEvent.VK_ENTER);

        JMenuItem mntmAktiviereServer = new JMenuItem("Verbindung bereitstellen");
        mntmAktiviereServer.setAccelerator(KeyStroke.getKeyStroke('B', KeyEvent.CTRL_DOWN_MASK));
        mntmAktiviereServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                String port = String.valueOf(serverCTR.getNextFreePort());

                OpenConnection arg = new OpenConnection(serverCTR,port);

                serverCTR.openConnections.put(serverCTR.getServerName()+":"+port,arg);
                serverCTR.connectToNode(serverCTR.getServerName(),port);

                arg.setLocation(getLocationOnScreen().x+100,getLocationOnScreen().y+90);
                arg.setVisible(true);
            }
        });
        mnMenu.add(mntmAktiviereServer);

        JMenuItem mntmNeueVerbindung = new JMenuItem("Neue Verbindung");
        mntmNeueVerbindung.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
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
                if(!serverCTR.connections.isEmpty()) {
                    textArea.setText("");
                    serverCTR.printAllNodesFancy();
                } else {
                    noNodesConnected();
                }
            }
        });
        mnMenu.add(mntmShowConnections);
		
        JSeparator separator_ = new JSeparator();
        mnMenu.add(separator_);
		
		JMenuItem mntmCountNode = new JMenuItem("Knoten zählen");
        mntmCountNode.setAccelerator(KeyStroke.getKeyStroke('K', KeyEvent.CTRL_DOWN_MASK));
		mntmCountNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(!serverCTR.connections.isEmpty()) {
                    textArea.setText("");
                    serverCTR.clientCTR.initNodeCount();
                } else {
                    noNodesConnected();
                }
			}
		});
		mnMenu.add(mntmCountNode);

        JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
        mntmGeneriereGraph.setAccelerator(KeyStroke.getKeyStroke('G', KeyEvent.CTRL_DOWN_MASK));
        mntmGeneriereGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!serverCTR.connections.isEmpty()) {
                    textArea.setText("");
                    serverCTR.clientCTR.initNodeGraph();
                } else {
                    noNodesConnected();
                }
            }
        });
        mnMenu.add(mntmGeneriereGraph);

        JMenuItem mntmNachricht = new JMenuItem("Sende WakeUp");
        mntmNachricht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                textArea.setText("");
                if(clientCTR.uDrawGrphPrintJob) {
                    clientCTR.uDrawGrphPrintJob = false;
                } else {
                    clientCTR.uDrawGrphPrintJob = true;
                }
                System.out.println(clientCTR.uDrawGrphPrintJob);
            }
        });
        mnMenu.add(mntmNachricht);

        JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
        mntmBeenden.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_DOWN_MASK));
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                beenden();
			}
		});
		mnMenu.add(mntmBeenden);

	}

    public void beenden(){
        serverCTR.terminateConnections();
        System.exit(0);
    }

    public void noNodesConnected(){
        textArea.setText("");
        System.out.println("#S: I feel lonely");
        System.out.println("#S: Lo-lo-lo-lo-lonely");
    }

}
