package gui;

import javax.swing.JDialog;

import java.awt.FlowLayout;

import javax.swing.JButton;

import server.CNSServer;
import server.ServerController;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartServerOrConnectServer extends JDialog{
	public StartServerOrConnectServer(final ServerController arg) {
		setTitle("CNS Server");
		setSize(200, 100);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(super.getLocation().x + 200, super.getLocation().y + 165);
		setModal(true);
		
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnCnsServerStarten = new JButton("CNS Server starten");
		btnCnsServerStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CNSServer.startCNS();
				dispose();
			}
		});
		getContentPane().add(btnCnsServerStarten);
		
		JButton btnStandardCnsServer = new JButton("Standard CNS Server eintragen");
		btnStandardCnsServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new StandardCNSServer(arg).setVisible(true);
				dispose();
			}
		});
		getContentPane().add(btnStandardCnsServer);
	}

}
