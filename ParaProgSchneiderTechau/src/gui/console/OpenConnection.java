package gui.console;

import javax.swing.JDialog;

import javax.swing.JLabel;

import server.ConnectionLabel;
import server.ServerChannel;
import server.ServerController;

import java.awt.Font;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class OpenConnection extends JDialog{
	
	private JLabel lblNewLabel_1;
	private ServerController serverCTR;
    private String port;
	
	public OpenConnection(ServerController serverCTR,String port) {
		this.port = port;
		this.serverCTR = serverCTR;
		setTitle("Offene Verbindung");
        setSize(300, 120);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Verbindung steht unter folgendem Namen bereit: ");
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Server: " +serverCTR.getServerName()+" Port: "+port);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Abbrechen");
		btnNewButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
                    beenden();
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();			}
		});
		panel_1.add(btnNewButton);


	}

    public void beenden(){

        dispose();

        serverCTR.openConnections.remove(serverCTR.getServerName() + ":" + port);
//        ServerChannel toRemove = serverCTR.incomingConnection.remove(serverCTR.getServerName()+":"+port);
//        try {
//            toRemove.interrupt();
//        } catch (Exception e){}
    }
}
