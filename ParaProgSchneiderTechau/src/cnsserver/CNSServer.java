package cnsserver;

import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;
import org.jcsp.net.tcpip.TCPIPAddressID;

import java.io.IOException;

/**
 * Created by Pascal on 15.07.2014.
 */
public class CNSServer{
	

    public static void startCNS() {
        NodeKey key = null;
        NodeID localNodeID = null;
        CNS cnsServer = null;

        // NetChannelServer initializierung
        try {
            //Initialize a Node that does not have a CNS client
            //TODO Netzwerk Test mit richtiger IP adresse eintragen
            TCPIPAddressID CNSServerAdresse = new TCPIPAddressID("127.0.0.1", 51526, false);
            key = Node.getInstance().init(CNSServerAdresse);

            localNodeID = Node.getInstance().getNodeID();
            //Initialize the CNS Server Process
            cnsServer = new CNS(key);

            cnsServer.start();
            NodeAddressID cnsAddress = localNodeID.getAddresses()[0];

        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
