package semesteraufgabe;

import gui.startup.StartServerOrConnectServer;
import server.ConnectionLabel;

import java.util.HashSet;
import java.util.Iterator;

public class Starter {


	/**
	 * @param args
	 */
	public static void main(String[] args) {


        new StartServerOrConnectServer().setVisible(true);

    }

    public static void printHash(HashSet<String> arg){
        Iterator iter = arg.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}