package gui.console;

import client.ClientController;
import semesteraufgabe.Starter;
import server.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartupConsole extends JDialog{

    public JTextField textField_StandardCNSServer;
    public JLabel label_StandardCNSServer;
    public JTextField textField_ServerName;
    public JLabel label_ServerName;


    public StartupConsole(final ServerController serverCTR, final ClientController clientCTR) {

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {  }

        setTitle("IP Eingeben");
        setSize(200, 175);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-100,Toolkit.getDefaultToolkit().getScreenSize().height/2-50);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label_StandardCNSServer = new JLabel("Standard CNS Server:");
        panel_1.add(label_StandardCNSServer);

        textField_StandardCNSServer = new JTextField();
        textField_StandardCNSServer.setText(ip.getHostAddress());

        textField_StandardCNSServer.setColumns(10);
        panel_1.add(textField_StandardCNSServer);
        textField_StandardCNSServer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connect_TRUE(serverCTR,clientCTR);
                    dispose();
                }
            }
        });

        label_ServerName = new JLabel("Server Name:");
        panel_1.add(label_ServerName);

        int random = (int) (Math.random()*1000);

        textField_ServerName = new JTextField();
        textField_ServerName.setText("Default_" + random);

        textField_ServerName.setColumns(10);
        panel_1.add(textField_ServerName);
        textField_ServerName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connect_TRUE(serverCTR,clientCTR);
                    dispose();
                }
            }
        });


        JButton btnNewButton = new JButton("Ok");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect_TRUE(serverCTR,clientCTR);
            }
        });
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Abbrechen");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect_FALSE();
            }
        });
        panel.add(btnNewButton_1);

    }
	public void connect_TRUE(ServerController serverCTR, ClientController clientCTR){

        clientCTR.setClientName(textField_ServerName.getText());
        serverCTR.setServerName(textField_ServerName.getText());
        serverCTR.setCNSServer(textField_StandardCNSServer.getText());
        dispose();
	}

    public void connect_FALSE(){
        dispose();
    }
	
}
