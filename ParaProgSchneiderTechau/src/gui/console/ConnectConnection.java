package gui.console;

import server.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Pascal on 23.07.2014.
 */
public class ConnectConnection extends JDialog{
    public JTextField textFieldServer;
    public JTextField textFieldPort;
    public ServerController serverCTR;
    public JLabel label;


    public ConnectConnection(ServerController server) {
        serverCTR = server;
        setTitle("IP Eingeben");
        setSize(300, 120);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-100,Toolkit.getDefaultToolkit().getScreenSize().height/2-50);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new GridLayout(2, 2));

        label = new JLabel("Server:");
        panel_1.add(label);

        textFieldServer = new JTextField();
        textFieldServer.setText("Default_");
        textFieldServer.setColumns(15);
        panel_1.add(textFieldServer);
        textFieldServer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectConnection();
                    dispose();
                }
            }
        });
                
                        label = new JLabel("Port:");
                        panel_1.add(label);
        
                textFieldPort = new JTextField();
//                textFieldPort.setText(String.valueOf(serverCTR.getNextFreePort()));
                textFieldPort.setColumns(15);
                panel_1.add(textFieldPort);
                textFieldPort.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            connectConnection();
                            dispose();
                        }
                    }
                });
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        
        JPanel panel_3 = new JPanel();
        panel.add(panel_3);
        
                JButton btnNewButton_1 = new JButton("Verbindung aufbauen");
                panel_3.add(btnNewButton_1);
                btnNewButton_1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connectConnection();
                    }
                });

    }

    public void connectConnection(){

        serverCTR.connectToNode(textFieldServer.getText(), textFieldPort.getText(), false);
        dispose();
    }
}
