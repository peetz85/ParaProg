package gui.startup;

import javax.swing.JDialog;

import java.awt.Toolkit;

import javax.swing.JButton;

import cnsserver.CNSServer;
import gui.cnsserver.CNSServerStatus;
import gui.console.Console;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartServerOrConnectServer extends JDialog{
	public StartServerOrConnectServer() {
		setTitle("Server Startup");
		setSize(200, 100);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 125, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 50);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnCnsServerStarten = new JButton("CNS Server starten");
		btnCnsServerStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                CNSServer.startCNS();
                new CNSServerStatus().setVisible(true);
				dispose();
			}
        });
		getContentPane().add(btnCnsServerStarten);
		
		JButton btnStandardCnsServer = new JButton("Node starten");
		btnStandardCnsServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                init();
			}
		});
		getContentPane().add(btnStandardCnsServer);
	}
//
    void init(){
        dispose();
        new Console().setVisible(true);
    }
}
