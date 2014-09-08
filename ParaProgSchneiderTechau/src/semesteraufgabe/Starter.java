package semesteraufgabe;

import gui.startup.StartServerOrConnectServer;

import java.util.HashSet;
import java.util.Iterator;

public class Starter {


    public static boolean debugMode = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {


        new StartServerOrConnectServer().setVisible(true);



//        Graph gra1 = new Graph();
//        gra1.addNode("node1");
//        gra1.addNode("node2");
//        gra1.addNode("node3");
//        gra1.addNode("node4");
//        gra1.addConnection("node1","node2");
//        gra1.addConnection("node1","node3");
//        gra1.addConnection("node1","node4");
//
//
//        Graph gra2 = new Graph();
//        gra2.addNode("node2");
//        gra2.addNode("node1");
//        gra2.addConnection("node1","node2");
//
//        Graph gra3 = new Graph();
//        gra3.addNode("node3");
//        gra3.addNode("node1");
//        gra3.addConnection("node1","node3");
//
//
//        Graph gra4 = new Graph();
//        gra4.addNode("node4");
//        gra4.addNode("node1");
//        gra4.addConnection("node1","node4");
//
//        Graph gra5 = new Graph();
//        gra5.addNode("node5");
//        gra5.addNode("node4");
//        gra5.addConnection("node5","node4");
//
//        Graph gra6 = new Graph();
//        gra6.addNode("node6");
//        gra6.addNode("node2");
//        gra6.addNode("node4");
//        gra6.addConnection("node6","node4");
//        gra6.addConnection("node6","node2");
//
//        gra1.addGraph(gra2);
//        gra1.addGraph(gra3);
//        gra6.addGraph(gra2);
//        gra4.addGraph(gra1);
//        gra4.addGraph(gra6);
//        gra5.addGraph(gra4);
//
//        System.out.println(gra5);

    }

    public static void printHash(HashSet<String> arg){
        Iterator iter = arg.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}