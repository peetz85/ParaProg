package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.ServerController;

public abstract class IPConnector extends JDialog{

	public JTextField textField;
	public ServerController server;


	public IPConnector(ServerController server) {
		this.server = server;
		setTitle("IP Eingeben");
		setSize(200, 100);
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

		textField = new JTextField();
        textField.setText("localhost");

		textField.setColumns(15);
		panel_1.add(textField);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					connect_TRUE();
					dispose();
				}
			}
		});

		JButton btnNewButton = new JButton("True");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				connect_TRUE();
				dispose();
			}
		});
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("False");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect_FALSE();
                dispose();
			}
		});
		panel.add(btnNewButton_1);

	}

	public void connect_TRUE() {	}

    public void connect_FALSE(){}
	
}
