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

    final public String serverName;

    public ServerController(String name) {
        serverName = name+"."+this.toString();
    }


    public String getIPAdress(){
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return (String) ip.getHostAddress();
    }
}
