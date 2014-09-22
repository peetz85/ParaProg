package semesteraufgabe;

import gui.startup.StartServerOrConnectServer;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Semesterarbeit Parallele Programmierung
 * von Nils Techau (29248)
 * und Pascal Schneider (30036)
 */

public class Starter {


    public static boolean debugMode = true;

    /**
     * @param args
     */
	public static void main(String[] args){


        new StartServerOrConnectServer().setVisible(true);

    }

    public static void printHash(HashSet<String> arg){
        Iterator iter = arg.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}