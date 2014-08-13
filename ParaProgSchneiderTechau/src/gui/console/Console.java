package gui.console;

import client.ClientController;
import org.jcsp.lang.Parallel;
import semesteraufgabe.Starter;
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

        JMenuItem mntmAktiviereServer = new JMenuItem("Server Aktivieren");
        mntmAktiviereServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Starter.clientCTR.run();
                serverCTR.start();
            }
        });
        mnMenu.add(mntmAktiviereServer);

        JSeparator separator_ = new JSeparator();
        mnMenu.add(separator_);

		JMenuItem mntmNeueVerbindung = new JMenuItem("Neue Verbindung");
		mntmNeueVerbindung.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("test");
                new NewConnection(serverCTR).setVisible(true);
            }
        });
		mnMenu.add(mntmNeueVerbindung);
		
		JMenuItem mntmGeneriereGraph = new JMenuItem("Generiere Graph");
		mntmGeneriereGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                System.out.println("Nis ist Los");
			}
		});
		mnMenu.add(mntmGeneriereGraph);

        JMenuItem mntmNachricht = new JMenuItem("Neue Nachricht");
        mntmNachricht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("test");
                if(serverCTR.connections.isEmpty())
                    System.out.println("HashMap ist Leer!");

                Message tmp = new Message();
                tmp.setI(16);
                serverCTR.connections.get("localhost").send(tmp);
            }
        });
        mnMenu.add(mntmNachricht);

        JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnMenu.add(mntmBeenden);

	}
}
