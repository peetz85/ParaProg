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
public class NewConnection extends JDialog{
    public JTextField textFieldServer;
    public JTextField textFieldPort;
    public ServerController serverCTR;
    public JLabel label;


    public NewConnection(ServerController server) {
        serverCTR = server;
        setTitle("IP Eingeben");
        setSize(300, 150);
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
        textFieldServer.setText(serverCTR.getServerName());
        textFieldServer.setColumns(15);
        panel_1.add(textFieldServer);
        textFieldServer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connect_TRUE();
                    dispose();
                }
            }
        });
                
                        label = new JLabel("Port:");
                        panel_1.add(label);
        
                textFieldPort = new JTextField();
                textFieldPort.setText(String.valueOf(serverCTR.getNextFreePort()));
                textFieldPort.setColumns(15);
                panel_1.add(textFieldPort);
                textFieldPort.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            connect_TRUE();
                            dispose();
                        }
                    }
                });
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        
        JPanel panel_2 = new JPanel();
        panel.add(panel_2);
        
                JButton btnNewButton = new JButton("Verbindung bereitstellen");
                panel_2.add(btnNewButton);
                btnNewButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connect_TRUE();
                    }
                });
        
        JPanel panel_3 = new JPanel();
        panel.add(panel_3);
        
                JButton btnNewButton_1 = new JButton("Verbindung aufbauen");
                panel_3.add(btnNewButton_1);
                btnNewButton_1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connect_FALSE();
                    }
                });

    }

    public void connect_TRUE(){

        serverCTR.connectToNode(textFieldServer.getText()+textFieldPort.getText(), true);
        dispose();
    }

    public void connect_FALSE(){

        serverCTR.connectToNode(textFieldServer.getText()+textFieldPort.getText(), false);
        dispose();
    }
}
