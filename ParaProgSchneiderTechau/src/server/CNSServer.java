package server;

import org.jcsp.net.*;
import org.jcsp.net.cns.CNS;

import java.io.IOException;

/**
 * Created by Pascal on 15.07.2014.
 */
public class CNSServer {
	

    public static void startCNS() {
        NodeKey key = null;
        NodeID localNodeID = null;

        // NetChannelServer initializierung
        try {
            //Initialize a Node that does not have a CNS client
            key = Node.getInstance().init(new XMLNodeFactory("ParaProgSchneiderTechau/src/server/nocns.xml"));
            // key = Node.getInstance().init(new XMLNodeFactory("src/server/nocns.xml"));
            localNodeID = Node.getInstance().getNodeID();
            //Initialize the CNS Server Process
            CNS.install(key);
            NodeAddressID cnsAddress = localNodeID.getAddresses()[0];

        } catch (NodeInitFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
