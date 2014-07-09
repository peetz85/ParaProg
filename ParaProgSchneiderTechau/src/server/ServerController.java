package server;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by Pascal on 08.07.2014.
 */
public class ServerController {




    public void getIPAdress() throws SocketException{
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println ("InetAddress = " + ip);
        System.out.println ("Hostname = " + ip.getHostName());
        System.out.println ("IP-Adresse = " + ip.getHostAddress());

        /*


        Enumeration ifaces = NetworkInterface.getNetworkInterfaces();


        while (ifaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface)ifaces.nextElement();
            System.out.println(ni.getName() + ":");

            Enumeration addrs = ni.getInetAddresses();


            while (addrs.hasMoreElements()) {
                    InetAddress ia = (InetAddress) addrs.nextElement();
                    System.out.println(" " + ia.getHostAddress());
            }
        }*/
    }
}
