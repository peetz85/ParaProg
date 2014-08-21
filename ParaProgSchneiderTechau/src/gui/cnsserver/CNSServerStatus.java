package gui.cnsserver;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Pascal on 24.07.2014.
 */
public class CNSServerStatus extends JFrame{


    public CNSServerStatus() {


        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        setTitle("CNS Server: " + ip.getHostAddress()+":51526");
        setSize(350, 100);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 125, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 50);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JButton btnCnsServerStarten = new JButton("Kill Server");

        btnCnsServerStarten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        getContentPane().add(btnCnsServerStarten);
    }
}
