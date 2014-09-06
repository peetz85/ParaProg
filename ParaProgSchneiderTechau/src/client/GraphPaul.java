package client;

import java.io.Serializable;
import java.util.HashSet;

public class GraphPaul implements Serializable {

    // ----------------------------------------------
    // Konstruktoren
    // ----------------------------------------------

    public GraphPaul() {
        nodes = null;
        nodeConnections = null;
    }

    public GraphPaul(String name) {
        addNode(name);
    }

    public GraphPaul(String[] knotenNamen) {
        nodeConnections = new boolean[knotenNamen.length][knotenNamen.length];
        for(String node : knotenNamen){
            addNode(node);
        }
    }

    // ----------------------------------------------
    // Member
    // ----------------------------------------------

    private boolean[][] nodeConnections;
    private String[] nodes;

    // ----------------------------------------------
    // Methoden
    // ----------------------------------------------

    // Gib alle Knotennamen als Feld zurück
    public String[] getKnotenNamen() {
        return nodes;
    }

    // Clone
    public GraphPaul clone() {
        GraphPaul graphClone = new GraphPaul();
        return graphClone.addGraph(this);
    }

    // Setze Verbindung zwischen Knoten mit Namen s1 und s2
    // (synchronized, weil Arbeiter-Thread zugreift)
    public void addConnection(String s1, String s2) {
        changeConnection(s1, s2, true);
    }

    public void deleteConnection(String s1, String s2) {
        changeConnection(s1, s2, false);
    }

    private void changeConnection(String s1, String s2, boolean arg){
        if (nodes != null) {
            int s1Index = -1, s2Index = -1;
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].equals(s1))
                    s1Index = i;
                if (nodes[i].equals(s2))
                    s2Index = i;
            }
            if (s1Index != -1 && s2Index != -1) {
                nodeConnections[s1Index][s2Index] = arg;
                nodeConnections[s2Index][s1Index] = arg;
            }
        }
    }


    public void addNodeSet(HashSet<String> nodeSet, String middelPoint) {
        String[] toConnect;
        int i = 0;
        if (nodeSet != null) {
            toConnect = new String[nodeSet.size()];
            for (String s : nodeSet){
                    addNode(s);
                    toConnect[i++] = s;
            }
            for (int c = 0; c < toConnect.length; c++) {
                    addConnection(toConnect[c], middelPoint);
                }
        }
    }


    // Teste Verbindung
    // (synchronized, weil Arbeiter-Thread zugreift)
    public boolean isNodeConnected(String s1, String s2) {
        // Graph noch leer
        if (nodes == null)
            return false;

        int s1Index = -1, s2Index = -1;
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i].equals(s1))
                s1Index = i;
            if (nodes[i].equals(s2))
                s2Index = i;
        }
        if (s1Index == -1 || s2Index == -1)
            return false;

        return (nodeConnections[s1Index][s2Index] && nodeConnections[s2Index][s1Index]);
    }

    public boolean isNodeInGraph(String s1) {
        if (nodes == null) {
            return false;
        }
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i].equals(s1)) {
                return true;
            }
        }
        return false;
    }

    public void addNode(String s1) {
        // Graph noch leer
        if (nodes == null) {
            nodeConnections = new boolean[1][1];
            nodes = new String[]{s1};
            nodeConnections[0][0] = true;
        }

        // Knoten schon drin? → Ende
        if (!isNodeInGraph(s1)) {
            boolean[][] newMatrix = new boolean[nodeConnections.length + 1][nodeConnections.length + 1];
            for (int zeile = 0; zeile < newMatrix.length; zeile++) {
                for (int spalte = 0; spalte < newMatrix.length; spalte++) {
                    newMatrix[zeile][spalte] = false;
                }
            }
            // Kopiere Matrix
            for (int zeile = 0; zeile < nodeConnections.length; zeile++) {
                for (int spalte = 0; spalte < nodeConnections.length; spalte++) {
                    newMatrix[zeile][spalte] = nodeConnections[zeile][spalte];
                }
            }


            String[] newKnotenName = new String[nodeConnections.length + 1];
            // Kopiere Namen
            for (int i = 0; i < nodeConnections.length; ++i) {
                newKnotenName[i] = nodes[i];
            }
            newKnotenName[newKnotenName.length - 1] = s1;
            newMatrix[newMatrix.length-1][newMatrix.length-1] = true;

            nodes = newKnotenName;
            nodeConnections = newMatrix;
        }
    }

    // Lösche einen Knoten mit Namen s1 aus Graph
    // (synchronized, weil Arbeiter-Thread zugreift)
//    public synchronized boolean delKnoten(String sdel) {
//        // Graph noch leer → Ende
//        if (nodes == null)
//            return false;
//        // Suche Knoten in Graph
//        boolean treffer = false;
//        for (int i = 0; i < nodes.length; ++i)
//            if (nodes[i].equals(sdel)) {
//                treffer = true;
//                break;
//            }
//        // Knoten nicht drin? → Ende
//        if (treffer == false)
//            return false;
//        // Ist nur noch der zu löschende Knoten in Graph → Graph löschen
//        if (nodes.length == 1) {
//            nodes = null;
//            nodeConnections = null;
//            return true;
//        }
//        // Los gehts
//        boolean[][] matrixNeu = new boolean[nodeConnections.length - 1][nodeConnections.length - 1];
//        String[] knotenNamenNeu = new String[nodeConnections.length - 1];
//        // Kopiere Namen
//        int tweak = 0;
//        for (int i = 0; i < nodeConnections.length; ++i)
//            if (nodes[i].equals(sdel))
//                tweak = 1;
//            else
//                knotenNamenNeu[i - tweak] = nodes[i];
//        // Kopiere Matrix
//        int spalteKopie;
//        int zeileKopie;
//        zeileKopie = 0;
//        // Durchlaufe Zeilen des Originals
//        for (int zeileOrig = 0; zeileOrig < nodeConnections.length; ++zeileOrig) {
//            // Gehört die Zeile nicht zum zu löschenden Knoten → weiter
//            if (!nodes[zeileOrig].equals(sdel)) {
//                spalteKopie = 0;
//                // Durchlaufe Spalten des Originals
//                for (int spalteOrig = 0; spalteOrig < nodeConnections.length; ++spalteOrig) {
//                    // Gehört die Spalte nicht zum zu löschenden Knoten → weiter
//                    if (!nodes[spalteOrig].equals(sdel)) {
//                        // Kopiere Inhalte
//                        matrixNeu[zeileKopie][spalteKopie] = nodeConnections[zeileOrig][spalteOrig];
//                        ++spalteKopie; // in Kopie eine Spalte weiter
//                    }
//                }
//                ++zeileKopie; // in Kopie eine Spalte weiter
//            }
//        }
//        nodes = knotenNamenNeu;
//        nodeConnections = matrixNeu;
//        return true;
//    }

    // Füge einen anderen gesamten Graphen hinzu
    public synchronized GraphPaul addGraph(GraphPaul graph2) {
        GraphPaul newGraph = new GraphPaul();

        if (graph2 != null && graph2.nodes != null) {

            String[] toConnect = getKnotenNamen();
            for (int c = 0; c < toConnect.length; c++) {
                if (!newGraph.isNodeInGraph(toConnect[c])) {
                    newGraph.addNode(toConnect[c]);
                }
            }
            for (int c = 0; c < toConnect.length; c++) {
                for (int i = 0; i < toConnect.length; i++) {
                    if (isNodeConnected(toConnect[c], toConnect[i])) {
                        newGraph.addConnection(toConnect[c], toConnect[i]);
                    }
                }
            }

            toConnect = graph2.getKnotenNamen();
            for (int c = 0; c < toConnect.length; c++) {
                if (!newGraph.isNodeInGraph(toConnect[c])) {
                    newGraph.addNode(toConnect[c]);
                }
            }
            for (int c = 0; c < toConnect.length; c++) {
                for (int i = 0; i < toConnect.length; i++) {
                    if (graph2.isNodeConnected(toConnect[c], toConnect[i])) {
                        newGraph.addConnection(toConnect[c], toConnect[i]);
                    }
                }
            }
        }
        nodeConnections = newGraph.nodeConnections;
        nodes = newGraph.nodes;
        return this;
    }

    // To String
    public String toString() {
        return druckeGraphAlsMatrix();
    }

    // Graphen ausgeben (als Adjazenzmatrix)
    public String druckeGraphAlsMatrix() {
        // Ist der Graph leer ?
        if (nodes == null) {
            return "|leerer Graph|";
        }
        // Ermittle Länge des längsten Namen
        int maxStrLen = 0;
        for (int i = 0; i < nodes.length; ++i)
            maxStrLen = Math.max(maxStrLen, nodes[i].length());
        String out = "";
        // Alle Namen zentrieren
        String[] namenZentriert = new String[nodes.length];
        for (int i = 0; i < nodes.length; ++i) {
            namenZentriert[i] = "";
            int lenName = nodes[i].length();
            for (int j = 0; j < (maxStrLen - lenName) / 2; ++j) {
                namenZentriert[i] += " ";
            }
            namenZentriert[i] += nodes[i];
        }
        // Spaltenköpfe ausgeben
        out += String.format("%-" + maxStrLen + "s|", "");
        for (int nr = 0; nr < nodeConnections.length; ++nr)
            out += String.format("%-" + maxStrLen + "s|", namenZentriert[nr]);
        out += "\n";
        // Querstrich unter ersten Zeile ausgeben
        String nameStrich = "";
        for (int i = 0; i < maxStrLen; ++i)
            nameStrich += "-"; // Richtige länge für den Querstrich pro Namen
        out += String.format("%-" + maxStrLen + "s|", nameStrich);
        for (int nr = 0; nr < nodeConnections.length; ++nr)
            out += String.format("%-" + maxStrLen + "s|", nameStrich);
        out += "\n";
        // Zentriertes Zeichen für Verbunden erstellen
        String OK = "";
        for (int i = 0; i < maxStrLen / 2; ++i)
            OK += " "; // Halbe Maximale Wortlänge davor als Space einfügen
        OK += "x";
        // Zeilenweise die Tabelle ausgeben
        for (int zeile = 0; zeile < nodeConnections.length; ++zeile) {
            out += String
                    .format("%-" + maxStrLen + "s|", namenZentriert[zeile]);
            for (int spalte = 0; spalte < nodeConnections.length; ++spalte)
                out += (nodeConnections[zeile][spalte] ? String.format("%-"
                        + maxStrLen + "s|", OK) : String.format("%-"
                        + maxStrLen + "s|", " "));
            out += "\n";
        }
        return out;
    }

}
