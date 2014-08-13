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
        setSize(300, 125);
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

        JButton btnNewButton = new JButton("Verbindung bereitstellen");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect_TRUE();
            }
        });
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Verbindung aufbauen");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect_FALSE();
            }
        });
        panel.add(btnNewButton_1);

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
